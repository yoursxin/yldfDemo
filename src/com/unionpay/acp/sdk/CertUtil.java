// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   CertUtil.java

package com.unionpay.acp.sdk;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

// Referenced classes of package com.unionpay.acp.sdk:
//			SDKConfig, LogUtil, SDKUtil

public class CertUtil
{
	static class CerFilter
		implements FilenameFilter
	{

		public boolean isCer(String name)
		{
			return name.toLowerCase().endsWith(".cer");
		}

		public boolean accept(File dir, String name)
		{
			return isCer(name);
		}

		CerFilter()
		{
		}
	}


	private static KeyStore keyStore = null;
	private static X509Certificate encryptCert = null;
	private static X509Certificate encryptTrackCert = null;
	private static X509Certificate validateCert = null;
	private static Map certMap = new HashMap();
	private static final ThreadLocal certKeyStoreLocal = new ThreadLocal();
	private static final Map certKeyStoreMap = new ConcurrentHashMap();

	public CertUtil()
	{
	}

	public static void init()
	{
		if ("true".equals(SDKConfig.getConfig().getSingleMode()))
			initSignCert();
		initEncryptCert();
		initValidateCertFromDir();
	}

	public static void initSignCert()
	{
		if (null != keyStore)
			keyStore = null;
		try
		{
			keyStore = getKeyInfo(SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd(), SDKConfig.getConfig().getSignCertType());
			LogUtil.writeLog((new StringBuilder()).append("InitSignCert Successful. CertId=[").append(getSignCertId()).append("]").toString());
		}
		catch (IOException e)
		{
			LogUtil.writeErrorLog("InitSignCert Error", e);
		}
	}

	/**
	 * @deprecated Method initSignCert is deprecated
	 */

	public static void initSignCert(String certFilePath, String certPwd)
	{
		LogUtil.writeLog((new StringBuilder()).append("加载证书文件[").append(certFilePath).append("]和证书密码[").append(certPwd).append("]的签名证书开始.").toString());
		certKeyStoreLocal.remove();
		File files = new File(certFilePath);
		if (!files.exists())
		{
			LogUtil.writeLog("证书文件不存在,初始化签名证书失败.");
			return;
		}
		try
		{
			certKeyStoreLocal.set(getKeyInfo(certFilePath, certPwd, "PKCS12"));
		}
		catch (IOException e)
		{
			LogUtil.writeErrorLog("加载签名证书失败", e);
		}
		LogUtil.writeLog((new StringBuilder()).append("加载证书文件[").append(certFilePath).append("]和证书密码[").append(certPwd).append("]的签名证书结束.").toString());
	}

	public static void loadRsaCert(String certFilePath, String certPwd)
	{
		KeyStore keyStore = null;
		try
		{
			keyStore = getKeyInfo(certFilePath, certPwd, "PKCS12");
			certKeyStoreMap.put(certFilePath, keyStore);
			LogUtil.writeLog("LoadRsaCert Successful");
		}
		catch (IOException e)
		{
			LogUtil.writeErrorLog("LoadRsaCert Error", e);
		}
	}

	public static void initEncryptCert()
	{
		if (!SDKUtil.isEmpty(SDKConfig.getConfig().getEncryptCertPath()))
		{
			encryptCert = initCert(SDKConfig.getConfig().getEncryptCertPath());
			LogUtil.writeLog("LoadEncryptCert Successful");
		} else
		{
			LogUtil.writeLog("WARN: acpsdk.encryptCert.path is empty");
		}
		if (!SDKUtil.isEmpty(SDKConfig.getConfig().getEncryptTrackCertPath()))
		{
			encryptTrackCert = initCert(SDKConfig.getConfig().getEncryptTrackCertPath());
			LogUtil.writeLog("LoadEncryptTrackCert Successful");
		} else
		{
			LogUtil.writeLog("WARN: acpsdk.encryptTrackCert.path is empty");
		}
	}

	private static X509Certificate initCert(String path)
	{
		X509Certificate encryptCertTemp;
		FileInputStream in = null;
		encryptCertTemp = null;		
		
		CertificateFactory cf = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
		} catch (CertificateException e) {
			LogUtil.writeErrorLog("InitCert Error", e);
		}
		try {
			in = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			LogUtil.writeErrorLog("InitCert Error File Not Found", e);
		}
		try {
			encryptCertTemp = (X509Certificate)cf.generateCertificate(in);
		} catch (CertificateException e) {
			LogUtil.writeErrorLog(e.toString());
		}
		LogUtil.writeLog((new StringBuilder()).append("[").append(path).append("][CertId=").append(encryptCertTemp.getSerialNumber().toString()).append("]").toString());
				
		return encryptCertTemp;
	}

	public static void initValidateCertFromDir()
	{
		String dir;
		
		certMap.clear();
		dir = SDKConfig.getConfig().getValidateCertDir();
		if (SDKUtil.isEmpty(dir))
		{
			LogUtil.writeLog("ERROR: acpsdk.validateCert.dir is empty");
			return;
		}
		
		CertificateFactory cf = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
		} catch (CertificateException e) {
			LogUtil.writeErrorLog("LoadVerifyCert Error", e);
		}
		File fileDir = new File(dir);
		File files[] = fileDir.listFiles(new CerFilter());
		for (int i = 0; i < files.length; i++)
		{
			File file = files[i];
			FileInputStream in = null;
			try {
				in = new FileInputStream(file.getAbsolutePath());
			} catch (FileNotFoundException e) {
				LogUtil.writeErrorLog("LoadVerifyCert Error File Not Found", e);
			}
			try {
				validateCert = (X509Certificate)cf.generateCertificate(in);
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			certMap.put(validateCert.getSerialNumber().toString(), validateCert);
			LogUtil.writeLog((new StringBuilder()).append("[").append(file.getAbsolutePath()).append("][CertId=").append(validateCert.getSerialNumber().toString()).append("]").toString());
		}

		LogUtil.writeLog("LoadVerifyCert Successful");		
		
	}

	public static PrivateKey getSignCertPrivateKey()
	{
		PrivateKey privateKey = null;
		Enumeration aliasenum = null;
		try {
			aliasenum = keyStore.aliases();
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKey Error", e);
		}
		String keyAlias = null;
		if (aliasenum.hasMoreElements())
			keyAlias = (String)aliasenum.nextElement();
		try {
			privateKey = (PrivateKey)keyStore.getKey(keyAlias, SDKConfig.getConfig().getSignCertPwd().toCharArray());
		} catch (UnrecoverableKeyException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKey Error", e);
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKey Error", e);
		} catch (NoSuchAlgorithmException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKey Error", e);
		}
		return privateKey;
		
		///LogUtil.writeErrorLog("getSignCertPrivateKey Error", e);
		
	}

	/**
	 * @deprecated Method getSignCertPrivateKeyByThreadLocal is deprecated
	 */

	public static PrivateKey getSignCertPrivateKeyByThreadLocal(String certPath, String certPwd)
	{
		if (null == certKeyStoreLocal.get())
			initSignCert(certPath, certPwd);
		PrivateKey privateKey = null;
		Enumeration aliasenum = null;
		try {
			aliasenum = ((KeyStore)certKeyStoreLocal.get()).aliases();
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog((new StringBuilder()).append("获取[").append(certPath).append("]的签名证书的私钥失败").toString(), e);
		}
		String keyAlias = null;
		if (aliasenum.hasMoreElements())
			keyAlias = (String)aliasenum.nextElement();
		try {
			privateKey = (PrivateKey)((KeyStore)certKeyStoreLocal.get()).getKey(keyAlias, certPwd.toCharArray());
		} catch (UnrecoverableKeyException e) {
			LogUtil.writeErrorLog((new StringBuilder()).append("获取[").append(certPath).append("]的签名证书的私钥失败").toString(), e);
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog((new StringBuilder()).append("获取[").append(certPath).append("]的签名证书的私钥失败").toString(), e);
		} catch (NoSuchAlgorithmException e) {
			LogUtil.writeErrorLog((new StringBuilder()).append("获取[").append(certPath).append("]的签名证书的私钥失败").toString(), e);
		}
		return privateKey;
		
		//LogUtil.writeErrorLog((new StringBuilder()).append("获取[").append(certPath).append("]的签名证书的私钥失败").toString(), e);
		
	}

	public static PrivateKey getSignCertPrivateKeyByStoreMap(String certPath, String certPwd)
	{
		if (!certKeyStoreMap.containsKey(certPath))
			loadRsaCert(certPath, certPwd);
		PrivateKey privateKey = null;
		Enumeration aliasenum = null;
		try {
			aliasenum = ((KeyStore)certKeyStoreMap.get(certPath)).aliases();
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKeyByStoreMap Error", e);
		}
		String keyAlias = null;
		if (aliasenum.hasMoreElements())
			keyAlias = (String)aliasenum.nextElement();
		try {
			privateKey = (PrivateKey)((KeyStore)certKeyStoreMap.get(certPath)).getKey(keyAlias, certPwd.toCharArray());
		} catch (UnrecoverableKeyException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKeyByStoreMap Error", e);
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKeyByStoreMap Error", e);
		} catch (NoSuchAlgorithmException e) {
			LogUtil.writeErrorLog("getSignCertPrivateKeyByStoreMap Error", e);
		}
		return privateKey;
		
	}

	public static PublicKey getEncryptCertPublicKey()
	{
		if (null == encryptCert)
		{
			String path = SDKConfig.getConfig().getEncryptCertPath();
			if (!SDKUtil.isEmpty(path))
			{
				encryptCert = initCert(path);
				return encryptCert.getPublicKey();
			} else
			{
				LogUtil.writeLog("ERROR: acpsdk.encryptCert.path is empty");
				return null;
			}
		} else
		{
			return encryptCert.getPublicKey();
		}
	}

	public static PublicKey getEncryptTrackCertPublicKey()
	{
		if (null == encryptTrackCert)
		{
			String path = SDKConfig.getConfig().getEncryptTrackCertPath();
			if (!SDKUtil.isEmpty(path))
			{
				encryptTrackCert = initCert(path);
				return encryptTrackCert.getPublicKey();
			} else
			{
				LogUtil.writeLog("ERROR: acpsdk.encryptTrackCert.path is empty");
				return null;
			}
		} else
		{
			return encryptTrackCert.getPublicKey();
		}
	}

	public static PublicKey getValidateKey()
	{
		if (null == validateCert)
			return null;
		else
			return validateCert.getPublicKey();
	}

	public static PublicKey getValidateKey(String certId)
	{
		X509Certificate cf = null;
		if (certMap.containsKey(certId))
		{
			cf = (X509Certificate)certMap.get(certId);
			return cf.getPublicKey();
		}
		initValidateCertFromDir();
		if (certMap.containsKey(certId))
		{
			cf = (X509Certificate)certMap.get(certId);
			return cf.getPublicKey();
		} else
		{
			LogUtil.writeErrorLog((new StringBuilder()).append("缺少certId=[").append(certId).append("]对应的验签证书.").toString());
			return null;
		}
	}

	public static String getSignCertId()
	{
		
		X509Certificate cert = null;
		Enumeration aliasenum = null;
		try {
			aliasenum = keyStore.aliases();
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("getSignCertId Error", e);
		}
		String keyAlias = null;
		if (aliasenum.hasMoreElements())
			keyAlias = (String)aliasenum.nextElement();
		try {
			cert = (X509Certificate)keyStore.getCertificate(keyAlias);
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("getSignCertId Error", e);
		}
		return cert.getSerialNumber().toString();		
	
	}

	public static String getEncryptCertId()
	{
		if (null == encryptCert)
		{
			String path = SDKConfig.getConfig().getEncryptCertPath();
			if (!SDKUtil.isEmpty(path))
			{
				encryptCert = initCert(path);
				return encryptCert.getSerialNumber().toString();
			} else
			{
				LogUtil.writeLog("ERROR: acpsdk.encryptCert.path is empty");
				return null;
			}
		} else
		{
			return encryptCert.getSerialNumber().toString();
		}
	}

	public static String getEncryptTrackCertId()
	{
		if (null == encryptTrackCert)
		{
			String path = SDKConfig.getConfig().getEncryptTrackCertPath();
			if (!SDKUtil.isEmpty(path))
			{
				encryptTrackCert = initCert(path);
				return encryptTrackCert.getSerialNumber().toString();
			} else
			{
				LogUtil.writeLog("ERROR: acpsdk.encryptTrackCert.path is empty");
				return null;
			}
		} else
		{
			return encryptTrackCert.getSerialNumber().toString();
		}
	}

	public static PublicKey getSignPublicKey()
	{
		PublicKey pubkey;
		Enumeration aliasenum = null;
		try {
			aliasenum = keyStore.aliases();
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog(e.toString());
		}
		String keyAlias = null;
		if (aliasenum.hasMoreElements())
			keyAlias = (String)aliasenum.nextElement();
		Certificate cert = null;
		try {
			cert = keyStore.getCertificate(keyAlias);
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog(e.toString());
		}
		pubkey = cert.getPublicKey();
		return pubkey;
	
	}

	public static KeyStore getKeyInfo(String pfxkeyfile, String keypwd, String type)
		throws IOException
	{
		FileInputStream fis = null;
		KeyStore keystore1;
		KeyStore ks = null;
		if ("JKS".equals(type))
			try {
				ks = KeyStore.getInstance(type);
			} catch (KeyStoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		else
		if ("PKCS12".equals(type))
		{
			String jdkVendor = System.getProperty("java.vm.vendor");
			String javaVersion = System.getProperty("java.version");
			LogUtil.writeLog((new StringBuilder()).append("java.vm.vendor=[").append(jdkVendor).append("]").toString());
			LogUtil.writeLog((new StringBuilder()).append("java.version=[").append(javaVersion).append("]").toString());
			if (null != jdkVendor && jdkVendor.startsWith("IBM"))
			{
				Security.insertProviderAt(new BouncyCastleProvider(), 1);
				printSysInfo();
			} else
			{
				Security.addProvider(new BouncyCastleProvider());
			}
			try {
				ks = KeyStore.getInstance(type);
			} catch (KeyStoreException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		LogUtil.writeLog((new StringBuilder()).append("Load RSA CertPath=[").append(pfxkeyfile).append("],Pwd=[").append(keypwd).append("]").toString());
		fis = new FileInputStream(pfxkeyfile);
		char nPassword[] = null;
		nPassword = null != keypwd && !"".equals(keypwd.trim()) ? keypwd.toCharArray() : null;
		if (null != ks)
			try {
				ks.load(fis, nPassword);
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CertificateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		keystore1 = ks;
		if (null != fis)
			fis.close();
		return keystore1;
		
		
		//KeyStore keystore;
		//if (Security.getProvider("BC") == null)
			//LogUtil.writeLog("BC Provider not installed.");
		//LogUtil.writeErrorLog("getKeyInfo Error", e);
		//if ((e instanceof KeyStoreException) && "PKCS12".equals(type))
			//Security.removeProvider("BC");
		//keystore = null;
		//if (null != fis)
			//fis.close();
		//return keystore;
		
		
		//if (null != fis)
			//fis.close();
		
	}

	public static void printSysInfo()
	{
		LogUtil.writeLog("================= SYS INFO begin====================");
		LogUtil.writeLog((new StringBuilder()).append("os_name:").append(System.getProperty("os.name")).toString());
		LogUtil.writeLog((new StringBuilder()).append("os_arch:").append(System.getProperty("os.arch")).toString());
		LogUtil.writeLog((new StringBuilder()).append("os_version:").append(System.getProperty("os.version")).toString());
		LogUtil.writeLog((new StringBuilder()).append("java_vm_specification_version:").append(System.getProperty("java.vm.specification.version")).toString());
		LogUtil.writeLog((new StringBuilder()).append("java_vm_specification_vendor:").append(System.getProperty("java.vm.specification.vendor")).toString());
		LogUtil.writeLog((new StringBuilder()).append("java_vm_specification_name:").append(System.getProperty("java.vm.specification.name")).toString());
		LogUtil.writeLog((new StringBuilder()).append("java_vm_version:").append(System.getProperty("java.vm.version")).toString());
		LogUtil.writeLog((new StringBuilder()).append("java_vm_name:").append(System.getProperty("java.vm.name")).toString());
		LogUtil.writeLog((new StringBuilder()).append("java.version:").append(System.getProperty("java.version")).toString());
		printProviders();
		LogUtil.writeLog("================= SYS INFO end=====================");
	}

	public static void printProviders()
	{
		LogUtil.writeLog("Providers List:");
		Provider providers[] = Security.getProviders();
		for (int i = 0; i < providers.length; i++)
			LogUtil.writeLog((new StringBuilder()).append(i + 1).append(".").append(providers[i].getName()).toString());

	}

	/**
	 * @deprecated Method getCertIdByThreadLocal is deprecated
	 */

	public static String getCertIdByThreadLocal(String certPath, String certPwd)
	{
		initSignCert(certPath, certPwd);
		X509Certificate cert = null;
		Enumeration aliasenum = null;
		try {
			aliasenum = ((KeyStore)certKeyStoreLocal.get()).aliases();
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("获取签名证书的序列号失败", e);
		}
		String keyAlias = null;
		if (aliasenum.hasMoreElements())
			keyAlias = (String)aliasenum.nextElement();
		try {
			cert = (X509Certificate)((KeyStore)certKeyStoreLocal.get()).getCertificate(keyAlias);
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("获取签名证书的序列号失败", e);
		}
		return cert.getSerialNumber().toString();
		
	
	}

	public static String getCertIdByKeyStoreMap(String certPath, String certPwd)
	{
		if (!certKeyStoreMap.containsKey(certPath))
			loadRsaCert(certPath, certPwd);
		return getCertIdIdByStore((KeyStore)certKeyStoreMap.get(certPath));
	}

	private static String getCertIdIdByStore(KeyStore keyStore)
	{
		Enumeration aliasenum = null;
		X509Certificate cert = null;
		try {
			aliasenum = keyStore.aliases();
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("getCertIdIdByStore Error", e);
		}
		String keyAlias = null;
		if (aliasenum.hasMoreElements())
			keyAlias = (String)aliasenum.nextElement();
		try {
			cert = (X509Certificate)keyStore.getCertificate(keyAlias);
		} catch (KeyStoreException e) {
			LogUtil.writeErrorLog("getCertIdIdByStore Error", e);
		}
		return cert.getSerialNumber().toString();
		
	}

	public static Map getCertMap()
	{
		return certMap;
	}

	public static void setCertMap(Map certMap)
	{
		certMap = certMap;
	}

	public static PublicKey getPublicKey(String modulus, String exponent)
	{
		KeyFactory keyFactory = null;
		RSAPublicKeySpec keySpec;
		BigInteger b1 = new BigInteger(modulus);
		BigInteger b2 = new BigInteger(exponent);
		try {
			keyFactory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			LogUtil.writeErrorLog((new StringBuilder()).append("构造RSA公钥失败：").append(e).toString());
		}
		keySpec = new RSAPublicKeySpec(b1, b2);
		try {
			return keyFactory.generatePublic(keySpec);
		} catch (InvalidKeySpecException e) {
			LogUtil.writeErrorLog((new StringBuilder()).append("构造RSA公钥失败：").append(e).toString());
		}
		return null;
		
	}

	public static PublicKey getEncryptTrackCertPublicKey(String modulus, String exponent)
	{
		if (SDKUtil.isEmpty(modulus) || SDKUtil.isEmpty(exponent))
		{
			LogUtil.writeErrorLog("[modulus] OR [exponent] invalid");
			return null;
		} else
		{
			return getPublicKey(modulus, exponent);
		}
	}

	static 
	{
		init();
	}
}