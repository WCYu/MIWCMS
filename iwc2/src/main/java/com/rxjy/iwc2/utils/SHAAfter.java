package com.rxjy.iwc2.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


public class SHAAfter
{
	private static final char[] CH_HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
        '9', 'A', 'B', 'C', 'D', 'E', 'F'};

	public static String SHA1(String value)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHAAfter"); // SHA1
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
	
	
	
	
	
	
	
	
	
	
	/**
	 * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 sign
	 * 
	 */
	public static String createSign(Map<String, Object> map, String apikey) {
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		for (Map.Entry<String, Object> m : map.entrySet()) {
			packageParams.put(m.getKey(), m.getValue().toString());
		}

		StringBuffer sb = new StringBuffer();
		Set<?> es = packageParams.entrySet();
		Iterator<?> it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (!TextUtils.isEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + apikey);
		String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
		return sign;
	}
	
	
	
}
