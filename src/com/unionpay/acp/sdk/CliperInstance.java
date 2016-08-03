package com.unionpay.acp.sdk;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CliperInstance
{

	private static ThreadLocal cipherTL = new ThreadLocal() {

		protected  Cipher initialValue()
		{
			try {
				return Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
			} catch (Exception e) {				
				e.printStackTrace();
				
			} 
			return null;			
		}

		

	};

	public CliperInstance()
	{
	}

	public static Cipher getInstance()
		throws NoSuchAlgorithmException, NoSuchPaddingException
	{
		return (Cipher)cipherTL.get();
	}

}