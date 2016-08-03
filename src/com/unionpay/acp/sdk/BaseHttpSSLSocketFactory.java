// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   BaseHttpSSLSocketFactory.java

package com.unionpay.acp.sdk;

import java.io.IOException;
import java.net.*;
import javax.net.ssl.*;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class BaseHttpSSLSocketFactory extends SSLSocketFactory {
	
	public static class MyX509TrustManager implements X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(X509Certificate ax509certificate[], String s) {
		}

		public void checkServerTrusted(X509Certificate ax509certificate[], String s) {
		}

		public MyX509TrustManager() {
		}

	}

	public static class TrustAnyHostnameVerifier implements HostnameVerifier {

		public boolean verify(String hostname, SSLSession session) {
			return true;
		}

		public TrustAnyHostnameVerifier() {
		}
	}

	static MyX509TrustManager manger = new MyX509TrustManager();

	public BaseHttpSSLSocketFactory() {
	}

	private SSLContext getSSLContext() {
		return createEasySSLContext();
	}

	public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3) throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
	}

	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3)
			throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
	}

	public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
	}

	public Socket createSocket(String arg0, int arg1) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
	}

	public String[] getSupportedCipherSuites() {
		return null;
	}

	public String[] getDefaultCipherSuites() {
		return null;
	}

	public Socket createSocket(Socket arg0, String arg1, int arg2, boolean arg3) throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
	}

	private SSLContext createEasySSLContext() {
		SSLContext context = null;
		try {
			context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[] { manger }, null);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return context;

	}
}