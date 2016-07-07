package com.emob.lib.util;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * DESUtil 解密
 * 
 * @author liyang
 * @dateTime 2013-2-22 上午10:34:21
 * @description
 */
public class DESUtil {
	static public final String	DES_KEY	= StrUtils.deCrypt("l@u$c!k&");
	static public final String	DES_KIV	= StrUtils.deCrypt("e%@1$m3@");

	public static final boolean ENCRYPT = true;
	public static final boolean DECRYPT = false;
	
	public static byte[] DESTransform(byte[] data, boolean encrypt) {

		byte[] finalData = null;

		try {
			DESKeySpec keySpec = new DESKeySpec(DES_KEY.getBytes());
			AlgorithmParameterSpec aps = new IvParameterSpec(DES_KIV.getBytes());
			SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
			Key key = skf.generateSecret(keySpec);
			Cipher cpr = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cpr.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, key, aps);
			finalData = cpr.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalData;
	}

	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0) throw new IllegalArgumentException();
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;

	}

	private static String byte2hex(byte[] b) {
		StringBuilder hs = new StringBuilder();
		String stmp;
		for (int n = 0; b != null && n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1) hs.append('0');
			hs.append(stmp);
		}
		return hs.toString().toUpperCase();

	}

	public final static String encrypt(String data) {
		if (data != null) try {
			return byte2hex(DESTransform(data.getBytes(), true));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;

	}

	public final static String decrypt(String data) {
		return new String(DESTransform(hex2byte(data.getBytes()), false));
	}
}
