// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SDKConfig.java

package com.unionpay.acp.sdk;

import java.io.*;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

// Referenced classes of package com.unionpay.acp.sdk:
//			LogUtil, SDKUtil

public class SDKConfig {

	public static final String FILE_NAME = "acp_sdk.properties";
	private String frontRequestUrl;
	private String backRequestUrl;
	private String singleQueryUrl;
	private String batchQueryUrl;
	private String batchTransUrl;
	private String fileTransUrl;
	private String signCertPath;
	private String signCertPwd;
	private String signCertType;
	private String encryptCertPath;
	private String validateCertDir;
	private String signCertDir;
	private String encryptTrackCertPath;
	private String cardRequestUrl;
	private String appRequestUrl;
	private String singleMode;
	public static final String SDK_FRONT_URL = "acpsdk.frontTransUrl";
	public static final String SDK_BACK_URL = "acpsdk.backTransUrl";
	public static final String SDK_SIGNQ_URL = "acpsdk.singleQueryUrl";
	public static final String SDK_BATQ_URL = "acpsdk.batchQueryUrl";
	public static final String SDK_BATTRANS_URL = "acpsdk.batchTransUrl";
	public static final String SDK_FILETRANS_URL = "acpsdk.fileTransUrl";
	public static final String SDK_CARD_URL = "acpsdk.cardTransUrl";
	public static final String SDK_APP_URL = "acpsdk.appTransUrl";
	public static final String SDK_SIGNCERT_PATH = "acpsdk.signCert.path";
	public static final String SDK_SIGNCERT_PWD = "acpsdk.signCert.pwd";
	public static final String SDK_SIGNCERT_TYPE = "acpsdk.signCert.type";
	public static final String SDK_ENCRYPTCERT_PATH = "acpsdk.encryptCert.path";
	public static final String SDK_ENCRYPTTRACKCERT_PATH = "acpsdk.encryptTrackCert.path";
	public static final String SDK_VALIDATECERT_DIR = "acpsdk.validateCert.dir";
	public static final String SDK_CVN_ENC = "acpsdk.cvn2.enc";
	public static final String SDK_DATE_ENC = "acpsdk.date.enc";
	public static final String SDK_PAN_ENC = "acpsdk.pan.enc";
	public static final String SDK_SINGLEMODE = "acpsdk.singleMode";
	private static SDKConfig config;
	private Properties properties;

	public static SDKConfig getConfig() {
		if (null == config){
			config = new SDKConfig();
		}
			
		return config;
	}

	public void loadPropertiesFromPath(String rootPath) {
		File file = null;
		InputStream in = null;
		if (!StringUtils.isNotBlank(rootPath))
			return;
		file = new File(
				(new StringBuilder()).append(rootPath).append(File.separator).append("acp_sdk.properties").toString());

		if (!file.exists())
			return;
		try {
			in = new FileInputStream(file);
			BufferedReader bf = new BufferedReader(new InputStreamReader(in, "utf-8"));
			properties = new Properties();
			properties.load(bf);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			if (null != in) {
				try {
					in.close();
				}
				// Misplaced declaration of an exception variable
				catch (Exception e) {

				}
			}
		}
		loadProperties(properties);

		loadPropertiesFromSrc();
	}

	public void loadPropertiesFromSrc() {

		InputStream in = SDKConfig.class.getClassLoader().getResourceAsStream("acp_sdk.properties");
		if (null != in) {			
			try {
				BufferedReader bf = new BufferedReader(new InputStreamReader(in, "utf-8"));
				properties = new Properties();
				properties.load(bf);
				loadProperties(properties);
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				LogUtil.writeErrorLog("acp_sdk.properties文件不存在!");
			} finally {
				try {
					in.close();
				}
				// Misplaced declaration of an exception variable
				catch (IOException e) {
					e.printStackTrace();
				}
			}		

		}

	}

	public void loadProperties(Properties pro) {
		String value = null;
		value = pro.getProperty("acpsdk.singleMode");
		if (SDKUtil.isEmpty(value) || "true".equals(value)) {
			singleMode = "true";
			LogUtil.writeLog(
					(new StringBuilder()).append("SingleCertMode:[").append(singleMode).append("]").toString());
			value = pro.getProperty("acpsdk.signCert.path");
			if (!SDKUtil.isEmpty(value))
				signCertPath = value.trim();
			value = pro.getProperty("acpsdk.signCert.pwd");
			if (!SDKUtil.isEmpty(value))
				signCertPwd = value.trim();
			value = pro.getProperty("acpsdk.signCert.type");
			if (!SDKUtil.isEmpty(value))
				signCertType = value.trim();
		} else {
			singleMode = "false";
			LogUtil.writeLog((new StringBuilder()).append("SingleMode:[").append(singleMode).append("]").toString());
		}
		value = pro.getProperty("acpsdk.encryptCert.path");
		if (!SDKUtil.isEmpty(value))
			encryptCertPath = value.trim();
		value = pro.getProperty("acpsdk.validateCert.dir");
		if (!SDKUtil.isEmpty(value))
			validateCertDir = value.trim();
		value = pro.getProperty("acpsdk.frontTransUrl");
		if (!SDKUtil.isEmpty(value))
			frontRequestUrl = value.trim();
		value = pro.getProperty("acpsdk.backTransUrl");
		if (!SDKUtil.isEmpty(value))
			backRequestUrl = value.trim();
		value = pro.getProperty("acpsdk.batchQueryUrl");
		if (!SDKUtil.isEmpty(value))
			batchQueryUrl = value.trim();
		value = pro.getProperty("acpsdk.batchTransUrl");
		if (!SDKUtil.isEmpty(value))
			batchTransUrl = value.trim();
		value = pro.getProperty("acpsdk.fileTransUrl");
		if (!SDKUtil.isEmpty(value))
			fileTransUrl = value.trim();
		value = pro.getProperty("acpsdk.singleQueryUrl");
		if (!SDKUtil.isEmpty(value))
			singleQueryUrl = value.trim();
		value = pro.getProperty("acpsdk.cardTransUrl");
		if (!SDKUtil.isEmpty(value))
			cardRequestUrl = value.trim();
		value = pro.getProperty("acpsdk.appTransUrl");
		if (!SDKUtil.isEmpty(value))
			appRequestUrl = value.trim();
		value = pro.getProperty("acpsdk.encryptTrackCert.path");
		if (!SDKUtil.isEmpty(value))
			encryptTrackCertPath = value.trim();
	}

	public String getFrontRequestUrl() {
		return frontRequestUrl;
	}

	public void setFrontRequestUrl(String frontRequestUrl) {
		this.frontRequestUrl = frontRequestUrl;
	}

	public String getBackRequestUrl() {
		return backRequestUrl;
	}

	public void setBackRequestUrl(String backRequestUrl) {
		this.backRequestUrl = backRequestUrl;
	}

	public String getSignCertPath() {
		return signCertPath;
	}

	public void setSignCertPath(String signCertPath) {
		this.signCertPath = signCertPath;
	}

	public String getSignCertPwd() {
		return signCertPwd;
	}

	public void setSignCertPwd(String signCertPwd) {
		this.signCertPwd = signCertPwd;
	}

	public String getSignCertType() {
		return signCertType;
	}

	public void setSignCertType(String signCertType) {
		this.signCertType = signCertType;
	}

	public String getEncryptCertPath() {
		return encryptCertPath;
	}

	public void setEncryptCertPath(String encryptCertPath) {
		this.encryptCertPath = encryptCertPath;
	}

	public String getValidateCertDir() {
		return validateCertDir;
	}

	public void setValidateCertDir(String validateCertDir) {
		this.validateCertDir = validateCertDir;
	}

	public String getSingleQueryUrl() {
		return singleQueryUrl;
	}

	public void setSingleQueryUrl(String singleQueryUrl) {
		this.singleQueryUrl = singleQueryUrl;
	}

	public String getBatchQueryUrl() {
		return batchQueryUrl;
	}

	public void setBatchQueryUrl(String batchQueryUrl) {
		this.batchQueryUrl = batchQueryUrl;
	}

	public String getBatchTransUrl() {
		return batchTransUrl;
	}

	public void setBatchTransUrl(String batchTransUrl) {
		this.batchTransUrl = batchTransUrl;
	}

	public String getFileTransUrl() {
		return fileTransUrl;
	}

	public void setFileTransUrl(String fileTransUrl) {
		this.fileTransUrl = fileTransUrl;
	}

	public String getSignCertDir() {
		return signCertDir;
	}

	public void setSignCertDir(String signCertDir) {
		this.signCertDir = signCertDir;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getCardRequestUrl() {
		return cardRequestUrl;
	}

	public void setCardRequestUrl(String cardRequestUrl) {
		this.cardRequestUrl = cardRequestUrl;
	}

	public String getAppRequestUrl() {
		return appRequestUrl;
	}

	public void setAppRequestUrl(String appRequestUrl) {
		this.appRequestUrl = appRequestUrl;
	}

	public String getEncryptTrackCertPath() {
		return encryptTrackCertPath;
	}

	public void setEncryptTrackCertPath(String encryptTrackCertPath) {
		this.encryptTrackCertPath = encryptTrackCertPath;
	}

	public String getSingleMode() {
		return singleMode;
	}

	public void setSingleMode(String singleMode) {
		this.singleMode = singleMode;
	}

	public SDKConfig() {
	}
}