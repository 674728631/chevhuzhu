package com.zccbh.demand.controller.alipay;

import java.security.MessageDigest;

/**
 * 
 */

/**
 * @author luoyg  加解密工具类
 *
 */
public class SignToSHA256Util {
	private SignToSHA256Util() {
	}

	/**
	 * @author: luoyg
	 * @Description: sha256加密
	 * @date: 2017年12月13日
	 * @param 
	 * @return String 
	 */
	public static String getSHA256(String str) {
		MessageDigest messageDigest;
		String encodeStr = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(str.getBytes("UTF-8"));//处理数据
			encodeStr = byte2Hex(messageDigest.digest());//获取处理后的结果-->byte数组;
		} catch (Exception e) {
			System.out.println("getSHA256 is error" + e.getMessage());
		}
		return encodeStr;
	}

	/**
	 * @author: luoyg
	 * @Description: byte数组转换成16进制
	 * @date: 2017年12月13日
	 * @param 
	 * @return String 
	 */
	private static String byte2Hex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		String temp;
		for (int i = 0; i < bytes.length; i++) {
			temp = Integer.toHexString(bytes[i] & 0xFF); //位运算,解决高位报错问题;
			if (temp.length() == 1) {
				builder.append("0");
			}
			builder.append(temp);
		}
		return builder.toString();
	}
}
