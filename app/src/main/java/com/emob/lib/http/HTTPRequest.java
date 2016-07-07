package com.emob.lib.http;

import com.emob.lib.util.DESUtil;
import com.emob.lib.util.SysHelper;

import android.content.Context;
import android.os.SystemClock;

public class HTTPRequest 
{
	static public final String REQUEST_GPS = "GPS";
	static public final String REQUEST_GPN = "GPN";
	static public final String REQUEST_TASKNOTIFY = "TASKNOTIFY";
	static public final String REQUEST_NTN = "NTN";
	static public final String REQUEST_ENOT = "ENOT";
	
	// dlt=gps nt=tasknotify dtn=gpn ntn=ntn 均用此函数
	public static String httpRequest( Context context, String[] urlpool, byte[] body, String type )
	{
		HTTPFrame frame = new HTTPFrame( context );

		frame.setHeader( "Content-Type", "application/octet-stream" );
		frame.setHeader( "ReqType", type );
		
		// 增加Header gzip
		frame.setHeader("Accept-Encoding", "gzip"); 
		// 不加密
		frame.setHeader("noEncy", "true");		
		// added by dinglin from vercode = 1008.
//		if( type.equals(REQUEST_GPS) )
//		{
//			frame.setHeader("CompGZ", "true");
//		}
		//frame.setHeader("Accept-Encoding","gzip");
		
		if(urlpool == null)
		{
			return null;
		}
		
		int result = HTTPFrame.HTTP_RESULT_OK;

		out:for( int j=0; j<urlpool.length; ++j ) // 每个地址尝试两次
		{	
			String url = urlpool[j];
			for( int index = 0; index < 1; ++index )
			{
				result = frame.submit( url, HTTPFrame.METHOD_POST, body );

				if( result == HTTPFrame.HTTP_RESULT_OK || !SysHelper.isNetworkEnabled( context ) )
				{
					break out;
				}

				SystemClock.sleep( 1000 * 5 );
			}
		}

		String respBody = null;
		
		if( result == HTTPFrame.HTTP_RESULT_OK )
		{
			body = frame.getRespBody();
			if( body != null )
			{
				respBody = new String(body);//DESUtil.decrypt(new String(body));
			}
		}
		
		return respBody;
	}

}
