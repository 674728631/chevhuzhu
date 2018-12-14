package com.zccbh.demand.controller.weChat.withdrawBank;


import java.security.PublicKey;
import java.util.SortedMap;
import java.util.TreeMap;

public class MainTest {

    public static void main(String[] args) throws Exception {
        String filepath="G:/tmp/";

        //RSAEncrypt.genKeyPair(filepath);


        System.out.println("--------------公钥加密私钥解密过程-------------------");
        String plainText="ihep";
        System.out.println("plainText = " + plainText.getBytes());
        //公钥加密过程
        byte[] cipherData= RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(filepath)),plainText.getBytes());
        String cipher=Base64.encode(cipherData);
        //私钥解密过程
        byte[] res= RSAEncrypt.decrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(filepath)), Base64.decode(cipher));
        String restr=new String(res);
        System.out.println("原文："+plainText);
        System.out.println("加密："+cipher);
        System.out.println("解密："+restr);
        System.out.println();

        System.out.println("--------------私钥加密公钥解密过程-------------------");
        plainText="ihep_私钥加密公钥解密";
        //私钥加密过程
        cipherData= RSAEncrypt.encrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(filepath)),plainText.getBytes());
        cipher=Base64.encode(cipherData);
        //公钥解密过程
        res= RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(filepath)), Base64.decode(cipher));
        restr=new String(res);
        System.out.println("原文："+plainText);
        System.out.println("加密："+cipher);
        System.out.println("解密："+restr);
        System.out.println();

        System.out.println("---------------私钥签名过程------------------");
        String content="ihep_这是用于签名的原始数据";
        String signstr= RSASignature.sign(content, RSAEncrypt.loadPrivateKeyByFile(filepath));
        System.out.println("签名原串："+content);
        System.out.println("签名串："+signstr);
        System.out.println();

        System.out.println("---------------公钥校验签名------------------");
        System.out.println("签名原串："+content);
        System.out.println("签名串："+signstr);

        System.out.println("验签结果："+ RSASignature.doCheck(content, signstr, RSAEncrypt.loadPublicKeyByFile(filepath)));
        System.out.println();

        //银行账号
        String bankAcctNo = "";
        //支行名字
        String bankAcctName = "";
        //订单号
        String partnerTradeNo = "";
        //用户编号
        String bankCode = "";

        String url = "https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank";

        String encBankAcctNo = ""; //加密的银行账号
        String encBankAcctName = ""; //加密的银行账户名

        String keyfile = "/usr/local/cert/ps8.pem"; //读取PKCS8密钥文件
        PublicKey pub= RSAUtil.getPubKey(keyfile,"RSA");
        String rsa ="RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";
        byte[] estr= RSAUtil.encrypt(bankAcctNo.getBytes(),pub,2048, 11,rsa);   //对银行账号进行加密
        encBankAcctNo =Base64.encode(estr);//并转为base64格式
        estr= RSAUtil.encrypt(bankAcctName.getBytes("UTF-8"),pub,2048, 11,rsa);
        encBankAcctName = Base64.encode(estr); //对银行账户名加密并转为base64


        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("mch_id", "微信支付商户号");//商户号
        parameters.put("partner_trade_no", partnerTradeNo);//结算单号，比如日结的汇总单的rowId，测试随便赋值一个
       // parameters.put("nonce_str", UUIDCreator.getUUID());//随机字符串
        parameters.put("enc_bank_no",encBankAcctNo); //加密的银行账号并Base64转码
        parameters.put("enc_true_name",encBankAcctName); //加密的银行账户名并Base64转码
        parameters.put("bank_code",bankCode); //银行编号
        parameters.put("amount",String.valueOf(10));//支付金额以分为单位
        parameters.put("desc",10);//以分为单位
        parameters.put("account_type", "1");//微信文档的参数表格没写这个参数，但xml示例中写了，去掉这个参数好像调用出现签名错误，必须加上，但含义不明确
        parameters.put("bank_note", "招商银行北京大运村支行");




//        String sign = CommonUtil.createSign("UTF-8", acctEnt.getAccountId(),parameters);//  我个人产品的微信签名工具，这个不存在问题
//        parameters.put("sign", sign);//设置签名
//        xml = CommonUtil.getRequestXml(parameters);//我个人产品的微信xml打包工具，生成xml串。
//        System.out.println("xml:::");
//        System.out.println(xml);
//        String xml1 = RedBagUtil.httpsRequest(url,"POST",xml,acctEnt.getAccountId(),acctEnt.getPartnerId());
    }
}
