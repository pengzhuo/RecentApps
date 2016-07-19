package com.emob.lib.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.List;
import java.util.Random;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.webkit.WebView;

public class SysHelper
{
	public static byte[] getRandomData( int length )
	{
		if( length < 0 )
		{
			length = 0;
		}

		byte[] data = new byte[length];

		if( length > 0 )
		{
			Random n = new Random();

			n.setSeed( System.currentTimeMillis() );

			n.nextBytes( data );
		}

		return data;
	}

	/**
	 * 把long值做转换后返回字节数组 - 与toLong( byte[] data, int start, int byteCount )对应
	 * @param value
	 * @param byteCount
	 * @return
	 */
	public static byte[] toByteArray( long value, int byteCount )
	{
		if( byteCount <= 0 || byteCount > 8 )
		{
			byteCount = 8;
		}		

		byte[] bV = new byte[byteCount];

		for( int i = 0; i < byteCount; ++i )
		{
			bV[i] = ( byte )( value & 0xFF );
			value >>= 8;
		}

		return bV;
	}

	/**
	 * 把字节还原为long值  - 与toByteArray( long value, int byteCount )
	 * @param data 字节
	 * @param start 起始字节
	 * @param byteCount 读取的字节数
	 * @return
	 * @throws Exception
	 */
	public static long toLong( byte[] data, int start, int byteCount ) throws Exception
	{
		if( data == null || data.length == 0 || start < 0 )
		{
			throw new Exception(  );
		}

		if( byteCount > data.length )
		{
			byteCount = data.length;
		}

		long value = 0;

		for( int i = start + byteCount - 1; i >= start; --i )
		{
			value <<= 8;
			value |= ( data[i] & 0xFF );				
		}

		return value;
	}

	public static void writeString( OutputStream os, String value ) throws Exception
	{
		byte[] data = null;

		int len = 0;

		if( value != null )
		{
			data = value.trim().getBytes();
			len = data.length;
		}

		writeInt( os, len );

		if( data != null )
		{
			os.write( data );
		}
	}

	public static String readString( InputStream is ) throws Exception
	{
		int len = readInt( is );

		if( len > 0 )
		{
			byte[] data = new byte[len];
			is.read( data );
			return ( new String( data ) );
		}

		return null;
	}

	public static void writeInt( OutputStream os, int value ) throws Exception
	{
		os.write( toByteArray( value, 4 ) );
	}

	public static int readInt( InputStream is ) throws Exception
	{
		byte[] value = new byte[4];
		is.read( value );
		return (int)toLong( value, 0, 4 );
	}

	public static byte[] getHashCodeRaw( String type, byte[] data )
	{
		byte[] digest = null;

		try
		{
			MessageDigest md = MessageDigest.getInstance( type );			
			md.update( data );
			digest = md.digest();
		}
		catch( Exception e )
		{

		}

		return digest;
	}

	public static String toHexString( byte[] data )
	{
		if( data == null )
		{
			return "null";
		}

		StringBuilder sb = new StringBuilder();

		for( byte b : data )
		{
			int item = b & 0xFF;

			String hexValue = Integer.toHexString( item );

			if( hexValue.length() < 2 )
			{
				sb.append( 0 );
			}

			sb.append( hexValue );
		}

		return sb.toString();
	}

	public static String getHashCode( String type, byte[] data )
	{
		return toHexString( getHashCodeRaw( type, data ) );
	}

	static public boolean isNetworkEnabled( Context context )
	{
		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );
		
		return ( connMgr.getActiveNetworkInfo() != null );
	}

	static public int getRandomInt( int min, int max )
	{
		if( min == max )
		{
			return min;
		}

		Random n = new Random();

		n.setSeed( System.currentTimeMillis() );

		return n.nextInt( Math.abs( max - min ) ) + Math.min( min, max );
	}
	
	
	/*static 	public JSONObject buildNetworkInfo( Context context ) throws Exception
	{
		JSONObject item = new JSONObject();	

		ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );

		NetworkInfo ni = connMgr.getActiveNetworkInfo();

		if( ni != null )
		{
			item.put( "type", "" + ni.getTypeName() );
			item.put( "subtype", "" + ni.getSubtypeName() );
			item.put( "apn", "" + ni.getExtraInfo() );
		}
		else
		{
			item.put( "type", "Unknown" );
			item.put( "subtype", "Unknown" );
			item.put( "apn", "Unknown" );
		}

		return item;
	}*/

	static public void removeFile( String fileName )
	{
		File file = new File( fileName );

		for( int index = 0; index < 3; ++index )
		{
			if( !file.exists() || file.delete() )
			{
				break;
			}

			SystemClock.sleep( 1000 * 3 );
		}
	}

	static public boolean fileExist( String name )
	{
		return ( new File( name ) ).exists();		
	}

	/*static public Object[] socketCommand( String cmd )
	{
		Object[] value = new Object[]{ false, null };

		try
		{
			Socket socket = new Socket( "127.0.0.1", 63912 );

			DataOutputStream dos = new DataOutputStream( socket.getOutputStream() );			
			dos.writeUTF( cmd );
			dos.flush();

			DataInputStream dis = new DataInputStream( socket.getInputStream() );			
			value[1] = dis.readUTF();
			value[0] = true;
			
			socket.close();
		}
		catch( Exception e )
		{
			if( e != null )
			{
				value[1] = e.toString();
			}
		}

		return value; 
	}*/

	static public Object[] execCommand( String...cmd )
	{ 
		Object[] result = new Object[]{ false, "" };

		Process process = null;

		try
		{
			ProcessBuilder pb = new ProcessBuilder( cmd );

			pb.redirectErrorStream( true );

			process = pb.start();

			InputStream is = process.getInputStream();

			//	Change from DataInputStream to BufferedReader due to the DataInputStream.readLine is deprecated.
			//	DataInputStream dis = new DataInputStream( is );

			BufferedReader br = new BufferedReader( new InputStreamReader( is ) );

			StringBuffer output = new StringBuffer();

			String line = null;

			while( ( line = br.readLine() ) != null )
			{
				output.append( line + "\r\n" );
			}

			br.close();
			is.close();

			result[1] = output.toString();

			int code = process.waitFor();

			if( code == 0 )
			{
				result[0] = true;
			}
		}
		catch( Exception e )
		{
			result[1] = ( e == null ? null : e.getMessage() );
		}

		if( process != null )
		{
			process.destroy();
		}

		return result;
	}

	// [start_other_ver]------------------------------------------------
	public static Object[] execCommandRoo( String rootpath, String cmd )
	{
		Object[] result = new Object[]{ false, "" };
		
		Process process = null;
		OutputStream f = null;
		BufferedReader br = null;
		
		try
		{
			ProcessBuilder processBuilder = new ProcessBuilder( new String[] { rootpath } ).redirectErrorStream( true );
			processBuilder.directory( new File("/") );
			process = processBuilder.start();
			
			f = process.getOutputStream();
			f.write( cmd.getBytes() );
			f.write(10);
			f.flush();
			f.close(); // 一定要关闭，否则会一直readLine
			
			br = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
			StringBuffer sb = new StringBuffer();
			String line = null;
			while( ( line = br.readLine() ) != null )
			{
				sb.append( line + "\r\n" );
			}
			result[1] = sb.toString();
			
			int code = process.waitFor(); 
			if( code == 0 )
			{
				result[0] = true;
			}
		}
		catch( Exception v0_1 )
		{
			result[1] = ( v0_1 == null ? null : v0_1.getMessage() );
		}
		finally
		{
			try
			{
				if( f != null )
				{
					f.close();
				}
			}
			catch( IOException e )
			{
				
			}
			
			try
			{
				if( br != null )
				{
					br.close();
				}
			}
			catch( IOException e )
			{
				
			}
		}
		
		if( process != null )
		{
			process.destroy();
		}
		
		return result;
	}
	// [end_other_ver]--------------------------------------------------------

	
	static public List<RunningServiceInfo> getActivityService( Context ctx )
	{	
		final ActivityManager activityManager = (ActivityManager)ctx.getSystemService( Context.ACTIVITY_SERVICE );
		
		return activityManager.getRunningServices( Integer.MAX_VALUE );
	}

	static public int isServiceRunning( Context context, String...actions )
	{
		List<RunningServiceInfo> services4 = SysHelper.getActivityService( context );

		for( RunningServiceInfo runningServiceInfo : services4 )
		{
			String name = runningServiceInfo.service.getClassName();

			for( int index = 0; index < actions.length; ++index )
			{
				if( actions[index].equalsIgnoreCase( name ) )
				{
					return index;
				}
			}
		}

		return -1;
	}


	/*static public void startService( Context context, String...actions )
	{
		for( String action : actions )
		{
			Intent intent = new Intent( action );
			context.startService( intent );
		}
	}*/

	/*static public Object[] startSocketServ( Context context )
	{
		Object[] ret = SysHelper.socketCommand( "<req><cmd>serv.ver</cmd></req>" );

		if( (Boolean)ret[0] )
		{
			return ret;
		}

		int idx = -1;
		boolean stop = true;
		
		for( int index = 0; index < 10; ++index )
		{
			SysHelper.isServiceRunning( context, SHDServiceAction.SHD_SERVICE_INFO[0][1],
					   							 SHDServiceAction.SHD_SERVICE_INFO[1][1],
					   							 SHDServiceAction.SHD_SERVICE_INFO[2][1],
					   							 SHDServiceAction.SHD_SERVICE_INFO[3][1] );
			
			if( idx >= 0 )
			{
				if( stop )
				{
					Intent it = new Intent( SHDServiceAction.SHD_SERVICE_INFO[idx][1] );				
					context.stopService( it );
				}
				else
				{
					break;
				}
			}
			else
			{
				stop = false;
				
				SysHelper.startService( context, SHDServiceAction.SHD_SERVICE_INFO[0][0],
												 SHDServiceAction.SHD_SERVICE_INFO[1][0],
												 SHDServiceAction.SHD_SERVICE_INFO[2][0],
												 SHDServiceAction.SHD_SERVICE_INFO[3][0] );
			}
			
			SystemClock.sleep( 1000 * 5 );
		}
		
		for( int index = 0; index < 12; ++index )
		{
			ret = SysHelper.socketCommand( "<req><cmd>serv.ver</cmd></req>" );
			if( (Boolean)ret[0] )
			{
				return ret;
			}
		
			SystemClock.sleep( 1000 * 10 );
		}
		
		return ret;
	}*/
	
	public static boolean ensurePathExists( String fileName, boolean all )
	{
		String name = null;
		
		if( all )
		{
			name = fileName;
		}
		else
		{
			int idx = fileName.lastIndexOf( '/' );
			if( idx <= 0 )
			{
				name = fileName;
			}
			else
			{
				name = fileName.substring( 0, idx );
			}
		}
		
		boolean ret = true;
		
		File file = new File( name );
		if( !file.exists() )
		{
			ret = file.mkdirs();
		}
	
		return ret;
	}
	
	/**
	 * 是否处于锁屏状态
	 * @param context
	 * @return
	 */
	public static boolean isLockScreen( Context context )
	{
		KeyguardManager mKeyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
		
		return mKeyguardManager.inKeyguardRestrictedInputMode();
	}
	
	/*static public String getTopComponentInfo(Context mContext) 
	{
		ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTask_list = activityManager.getRunningTasks(1);
		RunningTaskInfo runningTask = runningTask_list.get(0);

		String pkgName = runningTask.topActivity.getPackageName();
		String activityName = runningTask.topActivity.getClassName();

		return pkgName + "/" + activityName;
	}*/
	
	/**
	 * 得到顶层应用的包名
	 * @param mContext
	 * @return
	 */
	static public String getTopComponentInfo( Context mContext ) 
	{
		ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
		
		List<RunningTaskInfo> runningTask_list = activityManager.getRunningTasks(1);
		
		RunningTaskInfo runningTask = runningTask_list.get(0);

		return runningTask.topActivity.getPackageName();
	}
	
	/**
	 * 模拟Home键
	 * @param context
	 */
//	public static void simulateHome( Context context ) 
//	{	
//		INLog.i("simulateHome");
//		
//		Intent intent2 = new Intent(Intent.ACTION_MAIN);  
//		
//        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
//        
//        intent2.addCategory(Intent.CATEGORY_HOME);  
//        
//        context.startActivity(intent2);
//	}
	
	
	/**
	 * 得到UA
	 * @return
	 */
	public static String getUserAgent(Context context)
	{
		String v = null;
		
		try
		{
			v = new WebView(context).getSettings().getUserAgentString();
		}
		catch( Exception e )
		{
		}
		
		return v;
	}
	
	static public String[] getProxyInfo( Context context )
	{
		ConnectivityManager mgr = (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE );

		NetworkInfo ni = mgr.getActiveNetworkInfo();

		if( ni == null )
		{
			return null;
		}

		String apn = ni.getExtraInfo();

		if( apn == null || apn.trim().length() == 0 )
		{
			return null;
		}

		apn = apn.trim().toLowerCase();

		if( !apn.endsWith( "wap" ) )
		{
			return null;
		}

		if( apn.equals( "ctwap" ) )
		{
			return new String[]{ "10.0.0.200", "80", null };
		}

		return new String[]{ "10.0.0.172", "80", null };
	}

	public static void clearLocalUrl( Context context )
	{
//		PrefUtils.remove( context, HASer.HA_KEY_GPS );
//		PrefUtils.remove( context, HASer.HA_KEY_NT );
//		PrefUtils.remove( context, HASer.HA_KEY_DTN );
//		PrefUtils.remove( context, HASer.HA_KEY_NTN );
//		PrefUtils.remove( context, HASer.HA_KEY_TBZ );
	}
	
//	public static void writeIDFile( File uid, String id ) throws IOException
//	{
//		FileOutputStream out = new FileOutputStream(uid);
//		// String id = UUID.randomUUID().toString();
//		out.write(id.getBytes());
//		out.close();
//	}
//	public static String readIDFile( File uid ) 
//	{
//		byte[] bytes = null;
//		try
//		{
//			RandomAccessFile f = new RandomAccessFile(uid, "r");
//			bytes = new byte[(int) f.length()];
//			f.readFully(bytes);
//			f.close();
//		}
//		catch( Exception e )
//		{
//			return "";
//		}
//		return new String(bytes);
//	}

	public static String getManifestApplicationMetaData(Context context, String name){
		try{
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			return appInfo.metaData.getString(name);
		}catch (Exception e){
			e.toString();
		}
		return "";
	}
}