package com.rxjy.iwc2.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA
{
	private static final char[] CH_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
        '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	public static String SHA1(String value)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA"); // SHA1
			md.update(value.getBytes());
			String sha1 = byteArrayToHex(md.digest());
			return sha1.toUpperCase();
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 将字节数组转为十六进制字符串
	 *
	 * @param bytes
	 * @return 返回16进制字符串
	 */
	private static String byteArrayToHex(byte[] bytes)
	{
		// 一个字节占8位，一个十六进制字符占4位；十六进制字符数组的长度为字节数组长度的两倍
		char[] chars = new char[bytes.length * 2];
		int index = 0;
		for (byte b : bytes)
		{
			// 取字节的高4位
			chars[index++] = CH_HEX[b >>> 4 & 0xf];
			// 取字节的低4位
			chars[index++] = CH_HEX[b & 0xf];
		}
		return new String(chars);
	}
}
