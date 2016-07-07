package com.emob.lib.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.params.HttpConnectionParams;

import com.emob.lib.log.EmobLog;
import com.emob.lib.util.SysHelper;

import android.content.Context;
import android.net.http.AndroidHttpClient;

public class HTTPFrame
{
	static public final String METHOD_GET 			= "GET";
	static public final String METHOD_POST 			= "POST";
	static public final String METHOD_HEAD 			= "HEAD";
	
	static public final int HTTP_RESULT_OK 			= 0;
	static public final int HTTP_RESULT_FAILED 		= 1;
	static public final int HTTP_RESULT_EXCEPTION 	= 2;
	static public final int HTTP_RESULT_URL_ERROR  	= 3;
	// static public final int HTTP_RESULT_NEED_STANDBY = 4;

	private Map<String,String> mHeaders = null;

	private byte[] mRespBody = null;
	private Context mContext = null;
	private String mRespHdrs = null;
	private String mFileName = null;
	private int mResponseCode = 200;

	private HttpResponse mResponse = null;
	private DefaultHttpClient mHttpClient = null;

	public HTTPFrame( Context context )
	{
		mContext = context;
		mHeaders = new HashMap<String,String>();
		// mHeaders.put( "User-Agent", "HttpEngine" );
	}

	private boolean shouldUnzip( HttpEntity entity )
	{
		Header hdr = entity.getContentEncoding();

		if( hdr != null )
		{
			String value = hdr.getValue();

			if( value != null && value.toLowerCase().contains( "gzip" ) )
			{
				return true;
			}
		}			

		return false;
	}

	public void setHeader( String name, String value )
	{
		mHeaders.put( name, value );
	}

	public int getResponseCode()
	{
		return mResponseCode;
	}

	public void reset()
	{
		mHeaders.clear();
		//mHeaders.put( "User-Agent", "HttpEngine" );

		mRespBody = null;
		mRespHdrs = null;
		mFileName = null;
		mResponseCode = 200;
		mResponse = null;
	}

	public byte[] getRespBody()
	{
		return mRespBody;
	}

	public String getRespHdrs()
	{
		if( mRespHdrs == null )
		{
			mRespHdrs = getRespHdr( mResponse );
		}

		return mRespHdrs;
	}

	public String getRespHeaderStr( String key )
	{
		Header hdr = mResponse.getFirstHeader( key );
		if( hdr != null )
		{
			String v = hdr.getValue();
			if( v != null )
			{
				return v.trim();
			}
		}

		return "";
	}

	public int getRespHeaderInt( String key, int def )
	{
		String v = getRespHeaderStr( key );
		if( v == null || v.length() == 0 )
		{
			return def;
		}

		return Integer.parseInt( v );
	}

	public void setToFile( String fileName )
	{
		mFileName = fileName;
	}
	
	private void downloadRange( HttpRequestBase req )
	{
		if( mFileName != null )
		{
			File file = new File( mFileName );
			if( file.exists() )
			{
				int len = (int)file.length();
				if( len > 0 )
				{
					req.setHeader( "Range", "bytes=" + len + "-" );
				}
			}
		}
	}
	
	// g.do=dlt=gps  dda=ddata  d.do=nt=tasknotify dtn=gpn ntn=ntn
	public int submit( String url, String method, byte[] body )
	{
		if( url == null )
		{
			return HTTP_RESULT_URL_ERROR;
		}
		
		int result = HTTP_RESULT_OK;

		mRespHdrs = null;
		mResponse = null;
		mResponseCode = -9999; // 先置为特殊值，避免异常时，该值是上一次的值
		try
		{
			if( mHttpClient == null )
			{
				mHttpClient = new DefaultHttpClient();

				mHttpClient.getParams().setParameter( HttpConnectionParams.CONNECTION_TIMEOUT, 45 * 1000 );			
				mHttpClient.getParams().setParameter( HttpConnectionParams.SO_TIMEOUT, 45 * 1000 );
//				mHttpClient.getParams().setParameter( HttpConnectionParams.CONNECTION_TIMEOUT, 5 * 1000 );			
//				mHttpClient.getParams().setParameter( HttpConnectionParams.SO_TIMEOUT, 5 * 1000 );
			}

			boolean post = method.equalsIgnoreCase( METHOD_POST );

			String[] proxyInfo = SysHelper.getProxyInfo( mContext );

			HttpRequestBase request = post ? ( new HttpPost( url ) ) : ( new HttpGet( url ) );

			Set<String> keySet = mHeaders.keySet();

			for( String key : keySet )
			{
				request.setHeader( key, mHeaders.get( key ) );
			}
			
			if( proxyInfo != null && proxyInfo[0] != null )
			{
				mHttpClient.getParams().setParameter( ConnRouteParams.DEFAULT_PROXY, new HttpHost( proxyInfo[0], Integer.parseInt( proxyInfo[1] ) ) );
			}
			else
			{
				mHttpClient.getParams().removeParameter( ConnRouteParams.DEFAULT_PROXY );
			}
			
			DefaultHttpRequestRetryHandler retryhandler = new DefaultHttpRequestRetryHandler(5, true);
			mHttpClient.setHttpRequestRetryHandler(retryhandler);
			if( post )
			{	
				// modify by dinglin from versionCode200。 When repeat, this class maybe throw ClientProtocolException - NonRepeatableRequestException
				/*ByteArrayInputStream bais = new ByteArrayInputStream( body );
				InputStreamEntity ise = new InputStreamEntity( bais, body.length );*/
//				ByteArrayEntity ise = new ByteArrayEntity(body);
//				OutputStream zipper = new GZIPOutputStream(ise);  
//	            zipper.write(data);  
//	            zipper.close();  
//				ise.setContentEncoding("gzip");  
//				AndroidHttpClient.getCompressedEntity( body, null );
				
				//update by liuqingfeng 2016.05.25
				
				//((HttpPost)request).setEntity( AndroidHttpClient.getCompressedEntity( body, null ));
				//request.removeHeaders( "Content-Length" );
				
				StringEntity entity = new StringEntity(new String(body));
	            ((HttpPost)request).setEntity(entity);
			
			}

			downloadRange( request );

			try {
				mResponse = mHttpClient.execute( request );
			} catch (Exception e2) {
				result = HTTP_RESULT_EXCEPTION; 
				return result;
			}

			mResponseCode = mResponse.getStatusLine().getStatusCode();

			EmobLog.i("ResCode=" + mResponseCode);
			
			HttpEntity entity = mResponse.getEntity();

			mRespHdrs = getRespHdr( mResponse );

			if( mResponseCode >= 200 && mResponseCode < 400 )
			{
				InputStream content = null;
				try {
					content = entity.getContent();
				}
				catch (Exception e1) {
					result = HTTP_RESULT_EXCEPTION; 
					return result;
				}

				
				if( shouldUnzip( entity ) )
				{
					try {
						content = new GZIPInputStream( content );
					} catch (Exception e) {
						result = HTTP_RESULT_EXCEPTION; 
						return result;
					}
				}
								
				if( mFileName != null )
				{
					mRespBody = null;
					
					try {
						downloadToFile( content );
					} catch (IOException e) {
						result = HTTP_RESULT_EXCEPTION; 
						return result;
					} // 断点续传
				}
				else
				{
					OutputStream baos = new ByteArrayOutputStream();					

					byte[] temp = new byte[4096];
					
					int count = -1;
					try {
						count = content.read( temp );
					} catch (IOException e) {
						result = HTTP_RESULT_EXCEPTION; 
						return result;
					}

					while( count >= 0 )
					{
						try {
							baos.write( temp, 0, count );
						} catch (IOException e) {
							result = HTTP_RESULT_EXCEPTION; 
							return result;
						}
						try {
							count = content.read( temp );
						} catch (IOException e) {
							result = HTTP_RESULT_EXCEPTION; 
							return result;
						}
					}
				
					mRespBody = ((ByteArrayOutputStream)baos).toByteArray();
					
					try {
						baos.close();
					} catch (IOException e) {
						result = HTTP_RESULT_EXCEPTION; 
						return result;
					}
				}
			
				try {
					content.close();
				} catch (IOException e) {
					result = HTTP_RESULT_EXCEPTION; 
					return result;
				}
			}
			else
			{
				try {
					entity.consumeContent();
				} catch (IOException e) {
					result = HTTP_RESULT_EXCEPTION; 
					return result;
				}
				
				result = HTTP_RESULT_FAILED;
			}
		}
		catch( Exception e )
		{			
			result = HTTP_RESULT_EXCEPTION; 
		}

		return result;
	}
	
	
	private void downloadToFile( InputStream is ) throws IOException
	{
		RandomAccessFile raf = new RandomAccessFile( mFileName, "rw" );
		
		long len = raf.length();
		
		if( len > 0 )
		{
			raf.seek( len );
		}

		byte[] temp = new byte[4096];

		int count = is.read( temp );

		while( count >= 0 )
		{
			raf.write( temp, 0, count );
			count = is.read( temp );
		}

		raf.close();
		
		raf = null;
	}
	
	static private String getRespHdr( HttpResponse response )
	{
		StringBuffer sb = new StringBuffer();

		String v = response.getStatusLine().toString();

		sb.append( v );
		sb.append( "\r\n" );

		Header[] hdrs = response.getAllHeaders();

		for( Header hdr : hdrs )
		{
			sb.append( hdr.getName() + ": " );
			sb.append( hdr.getValue() + "\r\n" );
		}

		return sb.toString();
	}
	
	private static String readTextFile( String path )
	{
		StringBuilder sb = new StringBuilder();

		BufferedReader br = null;

		try
		{
			File file = new File(path);

			if( file.exists() )
			{
				br = new BufferedReader(new FileReader(file));

				String data = br.readLine();// 一次读入一行，直到读入null为文件结束

				while( data != null )
				{
					sb.append(data + "$$$");

					data = br.readLine(); // 接着读下一行
				}

				sb.delete(sb.length() - 3, sb.length());
			}
		}
		catch( Exception e )
		{
			sb.append("");
		}
		finally
		{
			try
			{
				if( br != null )
				{
					br.close();
				}
			}
			catch( Exception e )
			{
			}
		}

		return sb.toString();
	}
}
