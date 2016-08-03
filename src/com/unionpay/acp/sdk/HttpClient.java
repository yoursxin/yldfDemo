
package com.unionpay.acp.sdk;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;

// Referenced classes of package com.unionpay.acp.sdk:
//			BaseHttpSSLSocketFactory, LogUtil

public class HttpClient
{

	private URL url;
	private int connectionTimeout;
	private int readTimeOut;
	private String result;

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}

	public HttpClient(String url, int connectionTimeout, int readTimeOut)
	{
		try
		{
			this.url = new URL(url);
			this.connectionTimeout = connectionTimeout;
			this.readTimeOut = readTimeOut;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}

	public int send(Map data, String encoding)
		throws Exception
	{
		HttpURLConnection httpURLConnection;
		httpURLConnection = createConnection(encoding);
		if (null == httpURLConnection)
			throw new Exception("Create httpURLConnection Failure");
		requestServer(httpURLConnection, getRequestParamString(data, encoding), encoding);
		result = response(httpURLConnection, encoding);
		LogUtil.writeLog((new StringBuilder()).append("Response message:[").append(result).append("]").toString());
		return httpURLConnection.getResponseCode();
		
	}

	private void requestServer(URLConnection connection, String message, String encoder)
		throws Exception
	{
		PrintStream out = null;		
		
		try
		{
			connection.connect();
			out = new PrintStream(connection.getOutputStream(), false, encoder);
			out.print(message);
			out.flush();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if (null != out)
				out.close();
		}
		
	}

	private String response(HttpURLConnection connection, String encoding)
		throws URISyntaxException, IOException, Exception
	{
		InputStream in;
		BufferedReader br;
		Exception exception;
		in = null;
		StringBuilder sb = new StringBuilder(1024);
		br = null;
		String s;
		try
		{
			if (200 == connection.getResponseCode())
			{
				in = connection.getInputStream();
				sb.append(new String(read(in), encoding));
			} else
			{
				in = connection.getErrorStream();
				sb.append(new String(read(in), encoding));
			}
			LogUtil.writeLog((new StringBuilder()).append("HTTP Return Status-Code:[").append(connection.getResponseCode()).append("]").toString());
			s = sb.toString();
		}
		catch (Exception e)
		{
			throw e;
		}
		finally
		{
			if (null != br)
				br.close();
			if (null != in)
				in.close();
			if (null != connection)
				connection.disconnect();
		}
		
		return s;	
		
	}

	public static byte[] read(InputStream in)
		throws IOException
	{
		byte buf[] = new byte[1024];
		int length = 0;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		while ((length = in.read(buf, 0, buf.length)) > 0) 
			bout.write(buf, 0, length);
		bout.flush();
		return bout.toByteArray();
	}

	private HttpURLConnection createConnection(String encoding)
		throws ProtocolException
	{
		HttpURLConnection httpURLConnection = null;
		try
		{
			httpURLConnection = (HttpURLConnection)url.openConnection();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		httpURLConnection.setConnectTimeout(connectionTimeout);
		httpURLConnection.setReadTimeout(readTimeOut);
		httpURLConnection.setDoInput(true);
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setRequestProperty("Content-type", (new StringBuilder()).append("application/x-www-form-urlencoded;charset=").append(encoding).toString());
		httpURLConnection.setRequestMethod("POST");
		if ("https".equalsIgnoreCase(url.getProtocol()))
		{
			HttpsURLConnection husn = (HttpsURLConnection)httpURLConnection;
			husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
			husn.setHostnameVerifier(new BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier());
			return husn;
		} else
		{
			return httpURLConnection;
		}
	}

	private String getRequestParamString(Map requestParam, String coder)
	{
		if (null == coder || "".equals(coder))
			coder = "UTF-8";
		StringBuffer sf = new StringBuffer("");
		String reqstr = "";
		if (null != requestParam && 0 != requestParam.size())
		{
			Iterator i$ = requestParam.entrySet().iterator();
			do
			{
				if (!i$.hasNext())
					break;
				java.util.Map.Entry en = (java.util.Map.Entry)i$.next();
				try
				{
					sf.append((new StringBuilder()).append((String)en.getKey()).append("=").append(null != en.getValue() && !"".equals(en.getValue()) ? URLEncoder.encode((String)en.getValue(), coder) : "").append("&").toString());
				}
				catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
					return "";
				}
			} while (true);
			reqstr = sf.substring(0, sf.length() - 1);
		}
		LogUtil.writeLog((new StringBuilder()).append("Request Message:[").append(reqstr).append("]").toString());
		return reqstr;
	}
}