package com.zccbh.util.qrcode;

/**
 * @Author: luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-03-19 14:12
 **/

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.swetake.util.Qrcode;
import com.zccbh.util.uploadImg.UploadFileUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * 二维码生成工具类
 * @author Cloud
 * @data   2016-12-15
 * QRCode
 */

public class QRCodeUtil {

    //二维码颜色
    private static final int BLACK = 0xFF000000;
    //二维码颜色
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * <span style="font-size:18px;font-weight:blod;">ZXing 方式生成二维码</span>
     * @param text    <a href="javascript:void();">二维码内容</a>
     * @param width    二维码宽
     * @param height    二维码高
     * @param outPutPath    二维码生成保存路径
     * @param imageType        二维码生成格式
     */
    public static void zxingCodeCreate(String text, int width, int height, String outPutPath, String imageType){
        Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
        //设置编码字符集
        his.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            //1、生成二维码
            BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, his);

            //2、获取二维码宽高
            int codeWidth = encode.getWidth();
            int codeHeight = encode.getHeight();

            //3、将二维码放入缓冲流
            BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < codeWidth; i++) {
                for (int j = 0; j < codeHeight; j++) {
                    //4、循环将二维码内容定入图片
                    image.setRGB(i, j, encode.get(i, j) ? BLACK : WHITE);
                }
            }
            File outPutImage = new File(outPutPath);
            //如果图片不存在创建图片
            if(!outPutImage.exists())
                outPutImage.createNewFile();
            //5、将二维码写入图片
            ImageIO.write(image, imageType, outPutImage);
        } catch (WriterException e) {
            e.printStackTrace();
            System.out.println("二维码生成失败");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("生成二维码图片失败");
        }
    }

    /**
     * <span style="font-size:18px;font-weight:blod;">二维码解析</span>
     * @param analyzePath    二维码路径
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Object zxingCodeAnalyze(String analyzePath) throws Exception{
        MultiFormatReader formatReader = new MultiFormatReader();
        Object result = null;
        try {
            File file = new File(analyzePath);
            if (!file.exists())
            {
                return "二维码不存在";
            }
            BufferedImage image = ImageIO.read(file);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            result = formatReader.decode(binaryBitmap, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * <span style="font-size:18px;font-weight:blod;">QRCode 方式生成二维码</span>
     * @param content    二维码内容
     * @param imgPath    二维码生成路径
     * @param version    二维码版本
     */
    public static void QRCodeCreate(String content, String imgPath, int version, String logoPath){
        try {
            Qrcode qrcodeHandler = new Qrcode();
            //设置二维码排错率，可选L(7%) M(15%) Q(25%) H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qrcodeHandler.setQrcodeErrorCorrect('M');
            //N代表数字,A代表字符a-Z,B代表其他字符
            qrcodeHandler.setQrcodeEncodeMode('B');
            //版本1为21*21矩阵，版本每增1，二维码的两个边长都增4；所以版本7为45*45的矩阵；最高版本为是40，是177*177的矩阵
            qrcodeHandler.setQrcodeVersion(version);
            //根据版本计算尺寸
            int imgSize = 67 + 12 * (version - 1) ;
            byte[] contentBytes = content.getBytes("gb2312");
            BufferedImage bufImg = new BufferedImage(imgSize , imgSize ,BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = bufImg.createGraphics();
            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, imgSize , imgSize);
            // 设定图像颜色 > BLACK
            gs.setColor(Color.BLACK);
            // 设置偏移量 不设置可能导致解析出错
            int pixoff = 2;
            // 输出内容 > 二维码
            if (contentBytes.length > 0 && contentBytes.length < 130) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                for (int i = 0; i < codeOut.length; i++) {
                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                        }
                    }
                }
            } else {
                System.err.println("QRCode content bytes length = " + contentBytes.length + " not in [ 0,130 ]. ");
            }
           /* 判断是否需要添加logo图片 */
            if(logoPath != null){
                File icon = new File(logoPath);
                if(icon.exists()){
                    int width_4 = imgSize / 4;
                    int width_8 = width_4 / 2;
                    int height_4 = imgSize / 4;
                    int height_8 = height_4 / 2;
                    Image img = ImageIO.read(icon);
                    gs.drawImage(img, width_4 + width_8, height_4 + height_8,width_4,height_4, null);
                    gs.dispose();
                    bufImg.flush();
                }else{
                    System.out.println("Error: login图片不存在！");
                }

            }


            gs.dispose();
            bufImg.flush();
            //创建二维码文件
            File imgFile = new File(imgPath);
            if(!imgFile.exists())
                imgFile.createNewFile();
            //根据生成图片获取图片
            String imgType = imgPath.substring(imgPath.lastIndexOf(".") + 1, imgPath.length());
            // 生成二维码QRCode图片
            ImageIO.write(bufImg, imgType, imgFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <span style="font-size:18px;font-weight:blod;">QRCode二维码解析</span>
     * @param codePath    二维码路径
     * @return    解析结果
     */
    public static String QRCodeAnalyze(String codePath) {
        File imageFile = new File(codePath);
        String decodedData = null;
        try {
            if(!imageFile.exists())
                return "二维码不存在";
            MultiFormatReader reader=new MultiFormatReader();//需要详细了解MultiFormatReader的小伙伴可以点我一下官方去看文档
            BufferedImage image=ImageIO.read(imageFile);
            BinaryBitmap bb=new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            HashMap map =new HashMap();
            map.put(EncodeHintType.CHARACTER_SET, "utf-8");
            Result result = reader.decode(bb,map);
            System.out.println("解析结果："+result.toString());
            System.out.println("二维码格式类型："+result.getBarcodeFormat());
            System.out.println("二维码文本内容："+result.getText());
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception dfe) {
            System.out.println("Error: " + dfe.getMessage());
            dfe.printStackTrace();
        }
        return decodedData;
    }

    /**
     * 生成商家二维码 保存到阿里云
     * @param contents 二维码包含的内容
     * @param logoPath logo所在路径
     * @return 图片名称
     * @throws Exception
     */
    public static String saveQrcode(String contents,String logoPath,String aliyunPath) throws Exception{
        Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
        //设置编码字符集
        his.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //1、生成二维码
        BitMatrix encode = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, 430, 430, his);

        //2、获取二维码宽高
        int codeWidth = encode.getWidth();
        int codeHeight = encode.getHeight();

        //3、将二维码放入缓冲流
        BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < codeWidth; i++) {
            for (int j = 0; j < codeHeight; j++) {
                //4、循环将二维码内容定入图片
                image.setRGB(i, j, encode.get(i, j) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        Graphics2D gs = image.createGraphics();
        //读取logo
        File logoPic = new File(logoPath);
        BufferedImage logo = ImageIO.read(logoPic);
        //设置logo的大小
        int widthLogo = logo.getWidth(null) > image.getWidth() * 15 / 100 ? (image.getWidth() * 15 / 100) : logo.getWidth(null);
        int heightLogo = logo.getHeight(null) > image.getHeight() * 15 / 100 ? (image.getHeight() * 15 / 100) : logo.getWidth(null);
        //logo放在中心
        int x = (image.getWidth() - widthLogo) / 2;
        int y = (image.getHeight() - heightLogo) / 2;
        //开始绘制图片
        gs.drawImage(logo, x, y, widthLogo, heightLogo, null);
        gs.dispose();
        image.flush();
        //将二维码图片转为bytep[]
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image,"png",out);
        byte[] content = out.toByteArray();
        //新的图片文件名 = 获取时间戳+"."+图片扩展名
        String uuid = UUID.randomUUID().toString().replace("-","");
        String newFileName = uuid + ".png";
        //保存图片到阿里云
        UploadFileUtil.saveImg(aliyunPath,newFileName,content);
        return newFileName;
    }

    /**
     * 生成商家活动海报 保存到阿里云
     * @param contents 二维码包含的内容
     * @param logoPath logo所在路径
     * @return 图片名称
     * @throws Exception
     */
    public static String savePoster(String contents,String logoPath,String backgroundPath,String aliyunPath) throws Exception{
        Map<EncodeHintType, String> his = new HashMap<EncodeHintType, String>();
        //设置编码字符集
        his.put(EncodeHintType.CHARACTER_SET, "utf-8");
        //1、生成二维码
        BitMatrix encode = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, 170, 170, his);

        //2、获取二维码宽高
        int codeWidth = encode.getWidth();
        int codeHeight = encode.getHeight();

        //3、将二维码放入缓冲流
        BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < codeWidth; i++) {
            for (int j = 0; j < codeHeight; j++) {
                //4、循环将二维码内容定入图片
                image.setRGB(i, j, encode.get(i, j) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        Graphics2D gs = image.createGraphics();
        //读取logo
        File logoPic = new File(logoPath);
        BufferedImage logo = ImageIO.read(logoPic);
        //设置logo的大小
        int widthLogo = logo.getWidth(null) > image.getWidth() * 15 / 100 ? (image.getWidth() * 15 / 100) : logo.getWidth(null);
        int heightLogo = logo.getHeight(null) > image.getHeight() * 15 / 100 ? (image.getHeight() * 15 / 100) : logo.getWidth(null);
        //logo放在中心
        int x = (image.getWidth() - widthLogo) / 2;
        int y = (image.getHeight() - heightLogo) / 2;
        //开始绘制图片
        gs.drawImage(logo, x, y, widthLogo, heightLogo, null);
        gs.dispose();
        image.flush();
        //与背景图合成
        File backgroundPic = new File(backgroundPath);
        BufferedImage background = ImageIO.read(backgroundPic);
        Graphics g = background.getGraphics();
        g.drawImage(image, 550, 590,image.getWidth(), image.getHeight(), null);
        //将二维码图片转为bytep[]
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(background,"png",out);
        byte[] content = out.toByteArray();
        //新的图片文件名 = 获取时间戳+"."+图片扩展名
        String uuid = UUID.randomUUID().toString().replace("-","");
        String newFileName = uuid + ".png";
        //保存图片到阿里云
        UploadFileUtil.saveImg(aliyunPath,newFileName,content);
        return newFileName;
    }

    public static void main(String[] args) throws Exception {
        /**
         *    QRcode 二维码有图片生成测试
         */
            QRCodeUtil.QRCodeCreate("https://mp.weixin.qq.com/mp/profile_ext?action=home&__biz=MzUzNzc1MzI4OQ==&scene=124&id=1", "D://qrcode1.jpg", 15, "D://20180330154526.png");
            /**
             *     QRcode 二维码解析测试
             *    String qrcodeAnalyze = QRCodeUtil.QRCodeAnalyze("E://qrcode.jpg");
             */
            String qrcodeAnalyze = QRCodeUtil.QRCodeAnalyze("D://qrcode1.jpg");
            /**
             * ZXingCode 二维码无图片生成测试
             */
//        QRCodeUtil.zxingCodeCreate("https://www.baidu.com/", 300, 300, "D://zxingcode.jpg", "jpg");
            /**
             * ZxingCode 二维码解析
             */
//        String zxingAnalyze =  QRCodeUtil.zxingCodeAnalyze("D://zxingcode.jpg").toString();
            System.out.println("qrcodeAnalyze = " + qrcodeAnalyze);
            System.out.println("success");

    }
}