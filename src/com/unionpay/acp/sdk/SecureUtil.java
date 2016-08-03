// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SecureUtil.java

package com.unionpay.acp.sdk;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

// Referenced classes of package com.unionpay.acp.sdk:
//			LogUtil, CliperInstance

public class SecureUtil
{

	private static final String ALGORITHM_MD5 = "MD5";
	private static final String ALGORITHM_SHA1 = "SHA-1";
	private static final String BC_PROV_ALGORITHM_SHA1RSA = "SHA1withRSA";

	public SecureUtil()
	{
	}

	public static byte[] md5(byte datas[])
	{
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			LogUtil.writeErrorLog("MD5计算失败", e);
			return null;
		}
		md.reset();
		md.update(datas);
		return md.digest();		
		
	}

	public static byte[] sha1(byte data[])
	{
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			LogUtil.writeErrorLog("SHA1计算失败", e);
			return null;
		}
		md.reset();
		md.update(data);
		return md.digest();	
		
	}

	public static byte[] md5X16(String datas, String encoding)
	{
		StringBuilder md5StrBuff;
		byte bytes[] = md5(datas, encoding);
		md5StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++)
			if (Integer.toHexString(0xff & bytes[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xff & bytes[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xff & bytes[i]));

		try {
			return md5StrBuff.toString().getBytes(encoding);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		
		
	}

	public static byte[] sha1X16(String data, String encoding)
	{
		StringBuilder sha1StrBuff;
		byte bytes[] = sha1(data, encoding);
		sha1StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++)
			if (Integer.toHexString(0xff & bytes[i]).length() == 1)
				sha1StrBuff.append("0").append(Integer.toHexString(0xff & bytes[i]));
			else
				sha1StrBuff.append(Integer.toHexString(0xff & bytes[i]));

		try {
			return sha1StrBuff.toString().getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			
		}
		return null;
		
	}

	public static byte[] md5(String datas, String encoding)
	{
		try {
			return md5(datas.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeErrorLog("MD5计算失败", e);
			
		}
		return null;
	}

	public static byte[] sha1(String datas, String encoding)
	{
		try {
			return sha1(datas.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			LogUtil.writeErrorLog("SHA1计算失败", e);
		}		
		return null;
	}

	public static byte[] signBySoft(PrivateKey privateKey, byte data[])
		throws Exception
	{
		byte result[] = null;
		Signature st = Signature.getInstance("SHA1withRSA");
		st.initSign(privateKey);
		st.update(data);
		result = st.sign();
		return result;
	}

	public static boolean validateSignBySoft(PublicKey publicKey, byte signData[], byte srcData[])
		throws Exception
	{
		Signature st = Signature.getInstance("SHA1withRSA");
		st.initVerify(publicKey);
		st.update(srcData);
		return st.verify(signData);
	}

	public static byte[] inflater(byte inputByte[])
		throws IOException
	{
		Inflater compresser;
		ByteArrayOutputStream o;
		byte result[];
		int compressedDataLength = 0;
		compresser = new Inflater(false);
		compresser.setInput(inputByte, 0, inputByte.length);
		o = new ByteArrayOutputStream(inputByte.length);
		result = new byte[1024];
		do
		{
			if (compresser.finished())
				break;
			try {
				compressedDataLength = compresser.inflate(result);
			} catch (DataFormatException e) {
				System.err.println("Data format error!\n");
				e.printStackTrace();
				o.close();	
				return null;
			}
			if (compressedDataLength == 0)
				break;
			o.write(result, 0, compressedDataLength);
		} while (true);
		
		
		compresser.end();
		return o.toByteArray();
		//待修改
	}

	public static byte[] deflater(byte inputByte[])
		throws IOException
	{
		Deflater compresser;
		ByteArrayOutputStream o;
		byte result[];
		int compressedDataLength = 0;
		compresser = new Deflater();
		compresser.setInput(inputByte);
		compresser.finish();
		o = new ByteArrayOutputStream(inputByte.length);
		result = new byte[1024];
		
		try{
		for (; !compresser.finished(); o.write(result, 0, compressedDataLength))
			compressedDataLength = compresser.deflate(result);
		}catch(Exception e){
			o.close();
			return null;
		}

		compresser.end();
		result=o.toByteArray();		
		o.close();
		return result;
		
		//待修改
		
	}

	public static String EncryptPin(String pin, String card, String encoding, PublicKey key)
	{
		byte pinBlock[];
		byte data[];
		pinBlock = pin2PinBlockWithCardNO(pin, card);
		data = null;
		try {
			data = encryptedPin(key, pinBlock);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			return new String(base64Encode(data), encoding);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		return "";
	}

	public static String EncryptData(String dataString, String encoding, PublicKey key)
	{
		byte data[] = null;
		try {
			data = encryptedPin(key, dataString.getBytes(encoding));
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			return new String(base64Encode(data), encoding);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		return "";
	}

	public static String DecryptedData(String dataString, String encoding, PrivateKey key)
	{
		byte data[] = null;
		try {
			data = decryptedPin(key, dataString.getBytes(encoding));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			return new String(data, encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}

	public static byte[] base64Decode(byte inputByte[])
		throws IOException
	{
		return Base64.decodeBase64(inputByte);
	}

	public static byte[] base64Encode(byte inputByte[])
		throws IOException
	{
		
		return Base64.encodeBase64(inputByte);
	}

	public byte[] Str2Hex(String str)
	{
		char ch[] = str.toCharArray();
		byte b[] = new byte[ch.length / 2];
		for (int i = 0; i < ch.length && ch[i] != 0; i++)
		{
			if (ch[i] >= '0' && ch[i] <= '9')
			{
				ch[i] = (char)(ch[i] - 48);
				continue;
			}
			if (ch[i] >= 'A' && ch[i] <= 'F')
				ch[i] = (char)((ch[i] - 65) + 10);
		}

		for (int i = 0; i < b.length; i++)
			b[i] = (byte)((ch[2 * i] << 4 & 0xf0) + (ch[2 * i + 1] & 0xf));

		return b;
	}

	public static String Hex2Str(byte b[])
	{
		StringBuffer d = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++)
		{
			char hi = Character.forDigit(b[i] >> 4 & 0xf, 16);
			char lo = Character.forDigit(b[i] & 0xf, 16);
			d.append(Character.toUpperCase(hi));
			d.append(Character.toUpperCase(lo));
		}

		return d.toString();
	}

	public static String ByteToHex(byte bytes[])
	{
		StringBuffer sha1StrBuff = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
			if (Integer.toHexString(0xff & bytes[i]).length() == 1)
				sha1StrBuff.append("0").append(Integer.toHexString(0xff & bytes[i]));
			else
				sha1StrBuff.append(Integer.toHexString(0xff & bytes[i]));

		return sha1StrBuff.toString();
	}

	public static String Hex2Str(byte b[], int len)
	{
		String str = "";
		char ch[] = new char[len * 2];
		for (int i = 0; i < len; i++)
		{
			if ((b[i] >> 4 & 0xf) < 10 && (b[i] >> 4 & 0xf) >= 0)
				ch[i * 2] = (char)((b[i] >> 4 & 0xf) + 48);
			else
				ch[i * 2] = (char)(((b[i] >> 4 & 0xf) + 65) - 10);
			if ((b[i] & 0xf) < 10 && (b[i] & 0xf) >= 0)
				ch[i * 2 + 1] = (char)((b[i] & 0xf) + 48);
			else
				ch[i * 2 + 1] = (char)(((b[i] & 0xf) + 65) - 10);
		}

		str = new String(ch);
		return str;
	}

	public String byte2hex(byte b[])
	{
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++)
		{
			stmp = Integer.toHexString(b[n] & 0xff);
			if (stmp.length() == 1)
				hs = (new StringBuilder()).append(hs).append("0").append(stmp).toString();
			else
				hs = (new StringBuilder()).append(hs).append(stmp).toString();
			if (n < b.length - 1)
				hs = (new StringBuilder()).append(hs).append(":").toString();
		}

		return hs.toUpperCase();
	}

	public String genmac(byte inputByte[], byte inputkey[])
		throws Exception
	{
		String strMac;
		Mac mac = Mac.getInstance("HmacMD5");
		SecretKey key = new SecretKeySpec(inputkey, "DES");
		mac.init(key);
		byte macCode[] = mac.doFinal(inputByte);
		strMac = byte2hex(macCode);
		return strMac;
		
	}

	public boolean checkmac(byte inputByte[], byte inputkey[], String inputmac)
		throws Exception
	{
		String strMacCode;
		Mac mac = Mac.getInstance("HmacMD5");
		SecretKey key = new SecretKeySpec(inputkey, "DES");
		mac.init(key);
		byte macCode[] = mac.doFinal(inputByte);
		strMacCode = byte2hex(macCode);
		if (strMacCode.equals(inputmac))
			return true;
		return false;
	
	}

	public static String fillString(String string, char filler, int totalLength, boolean atEnd)
	{
		byte tempbyte[] = string.getBytes();
		int currentLength = tempbyte.length;
		int delta = totalLength - currentLength;
		for (int i = 0; i < delta; i++)
			if (atEnd)
				string = (new StringBuilder()).append(string).append(filler).toString();
			else
				string = (new StringBuilder()).append(filler).append(string).toString();

		return string;
	}

	public static byte[] encryptedPin(PublicKey publicKey, byte plainPin[])
		throws Exception
	{
		byte raw[];
		Cipher cipher = CliperInstance.getInstance();
		cipher.init(1, publicKey);
		int blockSize = cipher.getBlockSize();
		int outputSize = cipher.getOutputSize(plainPin.length);
		int leavedSize = plainPin.length % blockSize;
		int blocksSize = leavedSize == 0 ? plainPin.length / blockSize : plainPin.length / blockSize + 1;
		raw = new byte[outputSize * blocksSize];
		for (int i = 0; plainPin.length - i * blockSize > 0; i++)
			if (plainPin.length - i * blockSize > blockSize)
				cipher.doFinal(plainPin, i * blockSize, blockSize, raw, i * outputSize);
			else
				cipher.doFinal(plainPin, i * blockSize, plainPin.length - i * blockSize, raw, i * outputSize);

		return raw;
		
	}

	public byte[] encryptedData(PublicKey publicKey, byte plainData[])
		throws Exception
	{
		byte raw[];
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
		cipher.init(1, publicKey);
		int blockSize = cipher.getBlockSize();
		int outputSize = cipher.getOutputSize(plainData.length);
		int leavedSize = plainData.length % blockSize;
		int blocksSize = leavedSize == 0 ? plainData.length / blockSize : plainData.length / blockSize + 1;
		raw = new byte[outputSize * blocksSize];
		for (int i = 0; plainData.length - i * blockSize > 0; i++)
			if (plainData.length - i * blockSize > blockSize)
				cipher.doFinal(plainData, i * blockSize, blockSize, raw, i * outputSize);
			else
				cipher.doFinal(plainData, i * blockSize, plainData.length - i * blockSize, raw, i * outputSize);

		return raw;
		
	}

	public static byte[] decryptedPin(PrivateKey privateKey, byte cryptPin[])
		throws Exception
	{
		byte pinData[];
		byte pinBlock[] = base64Decode(cryptPin);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
		cipher.init(2, privateKey);
		int blockSize = cipher.getBlockSize();
		int outputSize = cipher.getOutputSize(pinBlock.length);
		int leavedSize = pinBlock.length % blockSize;
		int blocksSize = leavedSize == 0 ? pinBlock.length / blockSize : pinBlock.length / blockSize + 1;
		pinData = new byte[outputSize * blocksSize];
		for (int i = 0; pinBlock.length - i * blockSize > 0; i++)
			if (pinBlock.length - i * blockSize > blockSize)
				cipher.doFinal(pinBlock, i * blockSize, blockSize, pinData, i * outputSize);
			else
				cipher.doFinal(pinBlock, i * blockSize, pinBlock.length - i * blockSize, pinData, i * outputSize);

		return pinData;
		
	
		//LogUtil.writeErrorLog("解密失败", e);
		
	}

	private static byte[] pin2PinBlock(String aPin)
	{
		int tTemp = 1;
		int tPinLen = aPin.length();
		byte tByte[] = new byte[8];
		try
		{
			tByte[0] = (byte)Integer.parseInt(Integer.toString(tPinLen), 10);
			if (tPinLen % 2 == 0)
			{
				for (int i = 0; i < tPinLen; i += 2)
				{
					String a = aPin.substring(i, i + 2);
					tByte[tTemp] = (byte)Integer.parseInt(a, 16);
					if (i == tPinLen - 2 && tTemp < 7)
					{
						for (int x = tTemp + 1; x < 8; x++)
							tByte[x] = -1;

					}
					tTemp++;
				}

			} else
			{
				for (int i = 0; i < tPinLen - 1; i += 2)
				{
					String a = aPin.substring(i, i + 2);
					tByte[tTemp] = (byte)Integer.parseInt(a, 16);
					if (i == tPinLen - 3)
					{
						String b = (new StringBuilder()).append(aPin.substring(tPinLen - 1)).append("F").toString();
						tByte[tTemp + 1] = (byte)Integer.parseInt(b, 16);
						if (tTemp + 1 < 7)
						{
							for (int x = tTemp + 2; x < 8; x++)
								tByte[x] = -1;

						}
					}
					tTemp++;
				}

			}
		}
		catch (Exception e) { }
		return tByte;
	}

	private static byte[] formatPan(String aPan)
	{
		int tPanLen = aPan.length();
		byte tByte[] = new byte[8];
		int temp = tPanLen - 13;
		try
		{
			tByte[0] = 0;
			tByte[1] = 0;
			for (int i = 2; i < 8; i++)
			{
				String a = aPan.substring(temp, temp + 2);
				tByte[i] = (byte)Integer.parseInt(a, 16);
				temp += 2;
			}

		}
		catch (Exception e) { }
		return tByte;
	}

	public static byte[] pin2PinBlockWithCardNO(String aPin, String aCardNO)
	{
		byte tPinByte[] = pin2PinBlock(aPin);
		if (aCardNO.length() == 11)
			aCardNO = (new StringBuilder()).append("00").append(aCardNO).toString();
		else
		if (aCardNO.length() == 12)
			aCardNO = (new StringBuilder()).append("0").append(aCardNO).toString();
		byte tPanByte[] = formatPan(aCardNO);
		byte tByte[] = new byte[8];
		for (int i = 0; i < 8; i++)
			tByte[i] = (byte)(tPinByte[i] ^ tPanByte[i]);

		return tByte;
	}

	private static byte[] addPKCS1Padding(byte aBytesText[], int aBlockSize)
	{
		if (aBytesText.length > aBlockSize - 3)
			return null;
		SecureRandom tRandom = new SecureRandom();
		byte tAfterPaddingBytes[] = new byte[aBlockSize];
		tRandom.nextBytes(tAfterPaddingBytes);
		tAfterPaddingBytes[0] = 0;
		tAfterPaddingBytes[1] = 2;
		int i;
		for (i = 2; i < aBlockSize - 1 - aBytesText.length; i++)
			if (tAfterPaddingBytes[i] == 0)
				tAfterPaddingBytes[i] = (byte)tRandom.nextInt();

		tAfterPaddingBytes[i] = 0;
		System.arraycopy(aBytesText, 0, tAfterPaddingBytes, i + 1, aBytesText.length);
		return tAfterPaddingBytes;
	}

	public String assymEncrypt(String tPIN, String iPan, RSAPublicKey publicKey)
	{
		System.out.println((new StringBuilder()).append("SampleHashMap::assymEncrypt([").append(tPIN).append("])").toString());
		System.out.println((new StringBuilder()).append("SampleHashMap::assymEncrypt(PIN =[").append(tPIN).append("])").toString());
		String tEncryptPIN = null;
		int tKeyLength = 1024;
		int tBlockSize = tKeyLength / 8;
		byte tTemp[] = null;
		tTemp = pin2PinBlockWithCardNO(tPIN, iPan);
		tTemp = addPKCS1Padding(tTemp, tBlockSize);
		BigInteger tPlainText = new BigInteger(tTemp);
		BigInteger tCipherText = tPlainText.modPow(publicKey.getPublicExponent(), publicKey.getModulus());
		byte tCipherBytes[] = tCipherText.toByteArray();
		int tCipherLength = tCipherBytes.length;
		if (tCipherLength > tBlockSize)
		{
			byte tTempBytes[] = new byte[tBlockSize];
			System.arraycopy(tCipherBytes, tCipherLength - tBlockSize, tTempBytes, 0, tBlockSize);
			tCipherBytes = tTempBytes;
		} else
		if (tCipherLength < tBlockSize)
		{
			byte tTempBytes[] = new byte[tBlockSize];
			for (int i = 0; i < tBlockSize - tCipherLength; i++)
				tTempBytes[i] = 0;

			System.arraycopy(tCipherBytes, 0, tTempBytes, tBlockSize - tCipherLength, tCipherLength);
			tCipherBytes = tTempBytes;
		}
		try {
			tEncryptPIN = new String(base64Encode(tCipherBytes));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println((new StringBuilder()).append("SampleHashMap::assymEncrypt(EncryptCardNo =[").append(tEncryptPIN).append("])").toString());
		return tEncryptPIN;	
	}

	public static String trace(byte inBytes[])
	{
		int j = 0;
		byte temp[] = new byte[76];
		bytesSet(temp, ' ');
		StringBuffer strc = new StringBuffer("");
		strc.append("----------------------------------------------------------------------------\n");
		for (int i = 0; i < inBytes.length; i++)
		{
			if (j == 0)
			{
				System.arraycopy(String.format("%03d: ", new Object[] {
					Integer.valueOf(i)
				}).getBytes(), 0, temp, 0, 5);
				System.arraycopy(String.format(":%03d", new Object[] {
					Integer.valueOf(i + 15)
				}).getBytes(), 0, temp, 72, 4);
			}
			System.arraycopy(String.format("%02X ", new Object[] {
				Byte.valueOf(inBytes[i])
			}).getBytes(), 0, temp, j * 3 + 5 + (j <= 7 ? 0 : 1), 3);
			if (inBytes[i] == 0)
				temp[j + 55 + (j <= 7 ? 0 : 1)] = 46;
			else
				temp[j + 55 + (j <= 7 ? 0 : 1)] = inBytes[i];
			if (++j == 16)
			{
				strc.append(new String(temp)).append("\n");
				bytesSet(temp, ' ');
				j = 0;
			}
		}

		if (j != 0)
		{
			strc.append(new String(temp)).append("\n");
			bytesSet(temp, ' ');
		}
		strc.append("----------------------------------------------------------------------------\n");
		return strc.toString();
	}

	private static void bytesSet(byte inBytes[], char fill)
	{
		if (inBytes.length == 0)
			return;
		for (int i = 0; i < inBytes.length; i++)
			inBytes[i] = (byte)fill;

	}
}