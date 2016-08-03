// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SDKUtil.java

package com.unionpay.acp.sdk;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.commons.lang.StringUtils;

// Referenced classes of package com.unionpay.acp.sdk:
//			HttpClient, LogUtil, CertUtil, SecureUtil

public class SDKUtil
{

	protected static char letter[] = {
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
		'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 
		'u', 'v', 'w', 'x', 'y', 'z'
	};
	protected static final Random random = new Random();

	public SDKUtil()
	{
		
	}

	public static String send(String url, Map data, String encoding, int connectionTimeout, int readTimeout)
	{
		HttpClient hc = new HttpClient(url, connectionTimeout, readTimeout);
		String res = "";
		try
		{
			int status = hc.send(data, encoding);
			if (200 == status)
				res = hc.getResult();
		}
		catch (Exception e)
		{
			LogUtil.writeErrorLog("通信异常", e);
		}
		return res;
	}

	public static boolean sign(Map data, String encoding)
	{
		
		
		String stringData;
		if (isEmpty(encoding))
			encoding = "UTF-8";
		data.put("certId", CertUtil.getSignCertId());
		stringData = coverMap2String(data);		
		byte signDigest[] = SecureUtil.sha1X16(stringData, encoding);
		byte byteSign[];
		try {
			byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(CertUtil.getSignCertPrivateKey(), signDigest));
			String stringSign = new String(byteSign);
			data.put("signature", stringSign);
			return true;
		} catch (IOException e) {
			LogUtil.writeErrorLog("签名异常", e);
		} catch (Exception e) {
			LogUtil.writeErrorLog("签名异常", e);
		}
		
		return false;
	}

	public static boolean signByCertInfo(Map data, String encoding, String certPath, String certPwd)
	{
		String stringData;
		if (isEmpty(encoding))
			encoding = "UTF-8";
		if (isEmpty(certPath) || isEmpty(certPwd))
		{
			LogUtil.writeLog((new StringBuilder()).append("Invalid Parameter:CertPath=[").append(certPath).append("],CertPwd=[").append(certPwd).append("]").toString());
			return false;
		}
		data.put("certId", CertUtil.getCertIdByKeyStoreMap(certPath, certPwd));
		stringData = coverMap2String(data);
		
		
		byte signDigest[] = SecureUtil.sha1X16(stringData, encoding);
		byte byteSign[];
		try {
			byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(CertUtil.getSignCertPrivateKeyByStoreMap(certPath, certPwd), signDigest));
			String stringSign = new String(byteSign);
			data.put("signature", stringSign);
			return true;
		} catch (IOException e) {
			LogUtil.writeErrorLog("签名异常", e);		
		} catch (Exception e) {
			LogUtil.writeErrorLog("签名异常", e);			
		}
		return false;
		
	}

	public static boolean validate(Map resData, String encoding)
	{
		String stringSign;
		String certId;
		String stringData;
		LogUtil.writeLog("验签处理开始.");
		if (isEmpty(encoding))
			encoding = "UTF-8";
		stringSign = (String)resData.get("signature");
		certId = (String)resData.get("certId");
		stringData = coverMap2String(resData);
		try {
			return SecureUtil.validateSignBySoft(CertUtil.getValidateKey(certId), SecureUtil.base64Decode(stringSign.getBytes(encoding)), SecureUtil.sha1X16(stringData, encoding));
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
		} catch (IOException e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
		} catch (Exception e) {
			LogUtil.writeErrorLog(e.getMessage(), e);
		}		
		return false;
	}

	public static String coverMap2String(Map data)
	{
		TreeMap tree = new TreeMap();
		Iterator it = data.entrySet().iterator();
		do
		{
			if (!it.hasNext())
				break;
			java.util.Map.Entry en = (java.util.Map.Entry)it.next();
			if (!"signature".equals(((String)en.getKey()).trim()))
				tree.put(en.getKey(), en.getValue());
		} while (true);
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		java.util.Map.Entry en;
		for (; it.hasNext(); sf.append((new StringBuilder()).append((String)en.getKey()).append("=").append((String)en.getValue()).append("&").toString()))
			en = (java.util.Map.Entry)it.next();

		return sf.substring(0, sf.length() - 1);
	}

	public static Map coverResultString2Map(String result)
	{
		return convertResultStringToMap(result);
	}

	public static Map convertResultStringToMap(String result)
	{
		Map map = null;
		try
		{
			if (StringUtils.isNotBlank(result))
			{
				if (result.startsWith("{") && result.endsWith("}"))
					result = result.substring(1, result.length() - 1);
				map = parseQString(result);
			}
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return map;
	}

	public static Map parseQString(String str)
		throws UnsupportedEncodingException
	{
		Map map = new HashMap();
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		String key = null;
		boolean isKey = true;
		boolean isOpen = false;
		char openName = '\0';
		if (len > 0)
		{
			for (int i = 0; i < len; i++)
			{
				char curChar = str.charAt(i);
				if (isKey)
				{
					if (curChar == '=')
					{
						key = temp.toString();
						temp.setLength(0);
						isKey = false;
					} else
					{
						temp.append(curChar);
					}
					continue;
				}
				if (isOpen)
				{
					if (curChar == openName)
						isOpen = false;
				} else
				{
					if (curChar == '{')
					{
						isOpen = true;
						openName = '}';
					}
					if (curChar == '[')
					{
						isOpen = true;
						openName = ']';
					}
				}
				if (curChar == '&' && !isOpen)
				{
					putKeyValueToMap(temp, isKey, key, map);
					temp.setLength(0);
					isKey = true;
				} else
				{
					temp.append(curChar);
				}
			}

			putKeyValueToMap(temp, isKey, key, map);
		}
		return map;
	}

	private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map map)
		throws UnsupportedEncodingException
	{
		if (isKey)
		{
			key = temp.toString();
			if (key.length() == 0)
				throw new RuntimeException("QString format illegal");
			map.put(key, "");
		} else
		{
			if (key.length() == 0)
				throw new RuntimeException("QString format illegal");
			map.put(key, temp.toString());
		}
	}

	public static String encryptPin(String card, String pwd, String encoding)
	{
		return SecureUtil.EncryptPin(pwd, card, encoding, CertUtil.getEncryptCertPublicKey());
	}

	public static String encryptCvn2(String cvn2, String encoding)
	{
		return SecureUtil.EncryptData(cvn2, encoding, CertUtil.getEncryptCertPublicKey());
	}

	public static String decryptCvn2(String base64cvn2, String encoding)
	{
		return SecureUtil.DecryptedData(base64cvn2, encoding, CertUtil.getSignCertPrivateKey());
	}

	public static String encryptAvailable(String date, String encoding)
	{
		return SecureUtil.EncryptData(date, encoding, CertUtil.getEncryptCertPublicKey());
	}

	public static String decryptAvailable(String base64Date, String encoding)
	{
		return SecureUtil.DecryptedData(base64Date, encoding, CertUtil.getSignCertPrivateKey());
	}

	public static String encryptPan(String pan, String encoding)
	{
		return SecureUtil.EncryptData(pan, encoding, CertUtil.getEncryptCertPublicKey());
	}

	public static String decryptPan(String base64Pan, String encoding)
	{
		return SecureUtil.DecryptedData(base64Pan, encoding, CertUtil.getSignCertPrivateKey());
	}

	public static String encryptTrack(String trackData, String encoding)
	{
		return SecureUtil.EncryptData(trackData, encoding, CertUtil.getEncryptTrackCertPublicKey());
	}

	public static String encryptTrack(String trackData, String encoding, String modulus, String exponent)
	{
		return SecureUtil.EncryptData(trackData, encoding, CertUtil.getEncryptTrackCertPublicKey(modulus, exponent));
	}

	public static String encryptTrack(String trackData, String encoding, PublicKey publicKey)
	{
		return SecureUtil.EncryptData(trackData, encoding, publicKey);
	}

	public static boolean isEmpty(String s)
	{
		return null == s || "".equals(s.trim());
	}

	public static String generateTxnTime()
	{
		return (new SimpleDateFormat("yyyyMMddHHmmss")).format(new Date());
	}

	public static String generateOrderId()
	{
		StringBuilder sb = new StringBuilder();
		int len = random.nextInt(18);
		for (int i = 0; i < len; i++)
			sb.append(letter[i]);

		return (new StringBuilder()).append(generateTxnTime()).append(sb.toString()).toString();
	}

	public static String createAutoSubmitForm(String url, Map data)
	{
		StringBuffer sf = new StringBuffer();
		sf.append((new StringBuilder()).append("<form id = \"sform\" action=\"").append(url).append("\" method=\"post\">").toString());
		if (null != data && 0 != data.size())
		{
			Set set = data.entrySet();
			String key;
			String value;
			for (Iterator it = set.iterator(); it.hasNext(); sf.append((new StringBuilder()).append("<input type=\"hidden\" name=\"").append(key).append("\" id=\"").append(key).append("\" value=\"").append(value).append("\"/>").toString()))
			{
				java.util.Map.Entry ey = (java.util.Map.Entry)it.next();
				key = (String)ey.getKey();
				value = (String)ey.getValue();
			}

		}
		sf.append("</form>");
		sf.append("</body>");
		sf.append("<script type=\"text/javascript\">");
		sf.append("document.getElementById(\"sform\").submit();\n");
		sf.append("</script>");
		return sf.toString();
	}

	public static void main(String args[])
	{
		System.out.println(encryptTrack("12", "utf-8"));
	}

}