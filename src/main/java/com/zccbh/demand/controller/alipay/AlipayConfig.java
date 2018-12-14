package com.zccbh.demand.controller.alipay;

import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2018032302434940";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCSIyg4f/WDvdLWjd4HU6lFwUtZvdhJWKhiCcp6YN/71xz9K6/QwYD4YK9b0xBD4XMzB9oEm1XkzDoG33fSLQaPw8B8APNVp9T43YcojWLbSN8lUY4Bs+f8TVrxFigrzE7BfhTuA3LWKLXhxs6EMBNkt+JWIyI2Dl0AHmKvlt7vU2SyuPyUOdJqwnrqMGkb7WnxFRc+Un8Pm/KTyXXa2dbjQ42w0Hcl19zAEegzs8VkhJ9K7xekKuw9fcVsIa5sFGgM7ipT4AtHji8+8J4vYLnec5INPeRVTAvqE1Y1066nVwFvhFvLFMooNm2wCed4przPi9IATaQIai8YuaiVFMJjAgMBAAECggEAJW6x9773EXbHEcQnEjNg0YhQl70kMK1v/CTL44r69JRGTacqEsvuEDEb2eZ3bRvLmU+yjamIV+HkHyyQkZqM6rhWjqzjp5Yk3FZKL53/q3HADtwUBuWhT6Q03joxsf5UUgXMsKbbfHXTGPEGpx91aEAdzriTI8uMEOK3noiDtEOCDVQdhcIdgh5eZnmEzST7i2uCVRkujmvwJHcm7Gpb9sJ+yLHNkrP6kPAC+UnEHNXRuV+d142n0wQiFq2PSPNAivoY01PuEMqI6YckOZ3em0qqWatcjLiw1xME1jUrD0YPQvNHmTcbxPSyBcHuog9qz21rWmrVf378yQ9UFinUwQKBgQDHohKJ+gL536X1z25iSN+3WbnqmDrB/ckCvjSgeUEM2H+IUDH1CCsmgmKjyptnZFDHf7jTHZ8wUOH4HP3a7EuVFqHkOZt9i1HE5P8s2xTRblNERLMOrabBmd8ZTv9TdYTzMDEQFTrGxT6d04a3Mg1dPGqo+lp0lV/pv/+Tu85IIQKBgQC7ZkqCIPPdQIaqowwm9gpBgdKN6yq63Uu5ATja//KLWREhpdhBawtRcL9rEH9MQYi7B5DdMI8wpubn+AXMuG38+NmktNwV+2wjCrgXczjL40I9hZmEU26cYGbO0xDJmNy81+CIKyxshLV9kOd3R4dRxqPivSyxqcejydlPnUSqAwKBgBZw0pKuBv2dtWMvYSItPPC4ZlzHpfs+kwT66qGLX9OY1qU1FpiLxYX7rCjrUpLJxQbYBae9sbJVtUEVyTd9znVA6QvlqePv8E4oy8J4EhwEwdlvu40P2inzKRAIQE1abYqExU8gg83qSEKnj4IyXyw26Wp7UgW+8Nwu2AT8XJDhAoGAV8DpyQ/nQkcyCHu7eA8k9FH3qA9kfgacq/B/kEXkbQ5zMD96CgA3V5alfFxIUAwsG/DKDFnt44l0fVGuXZRLpPde63rpyL1ogOxHEmwZP5iW7zo9jjKHjGC0I8vAg7a6PxAUC4+0CMkIN9mAGmH7YvbSzZ1qbXOkkfU2dsqdWcMCgYEAxA+38Xstr7gfCpye4dsAO51nTMZjUoLN9DrKmCX7WiATY5wW8h1gZhSmRmSy7SgEb9CT+9JB8SW9piI3qi39lhp7NbG/z073vxgBUaERrwBIHQBXPVLa19Pp4rk3/ncx1xkvjfvxsOFlqAYU6t3v7nwhWxv72Iofifmb8/GCuEE="; //配置你的私钥;

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhtMLYPBJx4424HUDMx1YeIOdwiQYPwhj49u17pQRH/AKmZZQ8p8eGXzCzHWKBnHH67NVUCQmcAci3Tg+bXbzLjvTze5mvl9YaKowWZ21FFq0U8BrQEB65Yo+6nMlygr5OtKKTpWqo6rFKj0px3Y9q8KCDZhF5nSSqBhOg5qRGoDyzaYb0QhFAwa0OrTMtYm9MsIhXXW8FBsaqMGH1D/6VGUYvPGNw7tdBaRUZHBGYWtJFZ100bb3WWz2c5L958zIFw5aqyFrG+a6DMsbvEAt8tOD2dQbFzTgKiHliI2bYIEKtaEuc1YujbvvY/Nc96JqVcpgpzNrvPG/2oyWUTOltQIDAQAB"; //配置支付宝公钥;

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//	public static String notify_url ="http://www.chevdian.com/pay/notify";

    //public static String notify_url =""; //配置和前台跳转地址

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
//    public static String return_url = "http://www.chevdian.com"; //配置后台通知地址;

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "UTF-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipay.com/gateway.do"; //开发环境是这个,不用改都行;
    // 支付宝网关
//	public static String log_path = "/opt/apache-tomcat-7.0.75/logs";
    public static String log_path = "D:/logs"; //日志输出目录;本地就写本地,服务器写服务器;

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


