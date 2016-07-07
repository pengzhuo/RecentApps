package com.emob.lib.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Compress 
{
	// 压缩字节最小长度，小于这个长度的字节数组不适合压缩，压缩完会更大
	//public static final int BYTE_MIN_LENGTH = 50;

	public static byte[] byteCompress( byte[] data ) throws Exception 
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		compress(bais, baos); // 压缩

		byte[] output = baos.toByteArray();

		baos.flush();
		
		baos.close();

		bais.close();

		return output;
	}

	/**
	 * 数据解压缩
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] byteDecompress( byte[] data ) throws Exception 
	{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		decompress( bais, baos ); // 解压缩

		data = baos.toByteArray();

		baos.flush();
		
		baos.close();

		bais.close();

		return data;
	}
	
	/**
	 * 数据压缩
	 * 
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void compress(InputStream is, OutputStream os) throws Exception 
	{
		GZIPOutputStream gos = new GZIPOutputStream(os);

		int count;
		
		byte data[] = new byte[1024];
		
		while( (count = is.read(data, 0, data.length)) != -1) 
		{
			gos.write(data, 0, count);
		}

		gos.finish();

		gos.flush();
		
		gos.close();
	}

	/**
	 * 数据解压缩
	 * 
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void decompress(InputStream is, OutputStream os) throws Exception 
	{
		GZIPInputStream gis = new GZIPInputStream(is);

		int count = 0;
		
		byte[] data = new byte[1024];
		
		while( (count=gis.read(data, 0, data.length)) != -1 ) 
		{
			os.write(data, 0, count);
		}

		gis.close();
	}

	
}