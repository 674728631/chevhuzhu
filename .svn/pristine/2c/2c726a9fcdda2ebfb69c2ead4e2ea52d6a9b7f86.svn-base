package com.zccbh.util.collect;

import java.security.MessageDigest;

public class MD5Util {
	private static final String encryModel="MD5";
	/**
     * 32λmd5.
     * 32位小写md5加密
     * @param str
     * @return
     */
    public  static String md5(String str) {
        return encrypt(encryModel, str);
    }
    public static String getMD5Code(String strObj) {
        String resultString = null;
        try {
            if(strObj!=null){
                resultString = new String(strObj);
                MessageDigest md = MessageDigest.getInstance("MD5");
                // md.digest() 该函数返回值为存放哈希值结果的byte数组
                resultString = byteToString(md.digest(strObj.getBytes("UTF-8")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }
    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String encrypt(String s, String charsetName) {
        if (s == null) return "";
        try {
            byte[] buff = s.getBytes(charsetName);
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(buff);
            byte[] result = messagedigest.digest();
            return byte2Hex(result);
        } catch (Exception e) {
            return "";
        }
    }
    private static String byte2Hex(byte[] b) {
        if (b == null) return "";
        StringBuffer tmp = new StringBuffer();
        int len = b.length;
        for (int i = 0; i < len; ++i) {
            tmp.append((char)hexBase[((b[i] & 0xF0) >> 4)]);
            tmp.append((char)hexBase[(b[i] & 0xF)]);
        }
        for (; tmp.length() < 16; tmp.append("00"));
        return tmp.toString();
    }
    private static byte[] hexBase = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};

}
