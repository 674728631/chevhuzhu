package com.zccbh.util.base;

/**
 * @Author:                     luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-07-11 10:16
 **/
import net.sf.json.JSONArray;

import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 3DES加密
 *
 * @author SHANHY(365384722@QQ.COM)
 * @date 2015-8-18
 */
public class ThreeDES {
    public static final byte[] keyBytes = {0x11, (byte) 0xDD,0x22,(byte) 0xCB, 0x58, (byte) 0x88, 0x79, 0x11,(byte) 0xDD,0x10,  (byte) 0xDD,0x4F,0x38, (byte) 0x98, 0x25,(byte) 0x98,  0x51, (byte) 0xCB,0x4F, (byte) 0xDD, 0x55,  (byte) 0xCB,0x77, (byte) 0x98};
    private static final String Algorithm = "DESede"; // 定义 加密算法,可用
    // DES,DESede,Blowfish

    /**
     * 加密方法
     *
     * @param keybyte 加密密钥，长度为24字节
     * @param src     被加密的数据缓冲区（源）
     * @return
     * @author SHANHY
     * @date 2015-8-18
     */
    public static byte[] encryptMode(byte[] keybyte, byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

            // 加密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param keybyte 加密密钥，长度为24字节
     * @param src     加密后的缓冲区
     * @return
     * @author SHANHY
     * @date 2015-8-18
     */
    public static byte[] decryptMode(byte[] keybyte, byte[] src) throws Exception{
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

            // 解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
    }

    /*
         * 把16进制字符串转换成字节数组 @param hex @return
         */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 测试
     *
     * @param args
     * @author SHANHY
     * @date 2015-8-18
     */
    public static void main(String[] args)throws Exception {
        // 添加新安全算法,如果用JCE就要把它添加进去
        Security.addProvider(new com.sun.crypto.provider.SunJCE());

        // 24字节的密钥（我们可以取apk签名的指纹的前12个byte和后12个byte拼接在一起为我们的密钥）
//            String szSrc = "{\"merchantCode\":\"100001\",\"mobileNumber\":\"13456789888\",\"licensePlateNumber\":\"川A7FKJS\" ,\"money\":\"9\"}";
            String szSrc = "{\"mobileNumber\":\"18683062043\",\"licensePlateNumber\":\"川EA89U3\",\"merchantCode\":\"100001\",\"isNaturalUser\":\"0\"}";

        System.out.println("加密前的字符串:" + szSrc);

        byte[] encoded = encryptMode(keyBytes, szSrc.getBytes());
        String carDetail = ThreeDES.bytesToHexString(encoded);
        System.out.println("carDetail = " + carDetail);
        String s = "19201171B0D87B6361A21D8B7CEDFD10F93E52EAB1E2106EDC4CD73F7967F8BA8644BE757D6879035876AECA539D56AA5FE4BA5ABDC526774C66242E0AA08B25F6DD5616577E5B936322FA6368E1FC62B6ABE552C0C4A5AAB198B6179CB7F705";
        System.out.println("s = " + s);
        byte[] srcBytes = decryptMode(keyBytes, hexStringToByte(s));
        System.out.println("解密后的字符串:1" + (new String(srcBytes)));
//        Map map = JsonUtils.json2Map(new String(srcBytes));
//        Object token = map.get("token");
//        System.out.println("token = " + token);

    }
}