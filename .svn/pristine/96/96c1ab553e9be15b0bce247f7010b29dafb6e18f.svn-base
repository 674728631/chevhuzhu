package com.zccbh.util.uploadImg;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.UploadUdfImageRequest;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @author Administrator
 *
 */
public class UploadFileUtil {
	static final 	String endpoint = Constant.toReadPro("endpoint");
	static final	String accessKeyId =  Constant.toReadPro("accessKeyId");
	static final	String accessKeySecret = Constant.toReadPro("accessKeySecret");
	static final	String bucketName = Constant.toReadPro("bucketName");

	/**
	 * @param request
	 * @return		   图片上传的名字
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public static Map<String,Object> uploadFile(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status",false);
		resultMap.put("resultMessage","");
		Boolean flag = false;
		StringBuffer buffer = new StringBuffer();
		StringBuffer maintenance = new StringBuffer();
		StringBuffer logo = new StringBuffer();
		StringBuffer maintenanceshopImg = new StringBuffer();
		StringBuffer employeeimg = new StringBuffer();
		StringBuffer poster = new StringBuffer();
		StringBuffer businessLicense = new StringBuffer();
		StringBuffer feedback = new StringBuffer();
		StringBuffer insuranceAssert = new StringBuffer();
		StringBuffer insuranceRepair = new StringBuffer();
		StringBuffer portrait = new StringBuffer();


		//图片新名字
		String newFileName = null;
		//图片旧名字
		String oldFileName = null;
		//添加文件类型
		List fileTypes = new ArrayList();
		fileTypes.add("jpg");
		fileTypes.add("jpeg");
		fileTypes.add("bmp");
		fileTypes.add("gif");
		fileTypes.add("png");
		// 创建一个通用的多部分解析器
		String delete = request.getParameter("delete");
		if (Constant.toEmpty(delete)) {
			String[] split = delete.split("_");
			String url = split[0];
			String fileName = split[1];
			/**
			 *PHOTOGRAPHS4_URL 		身份证照片正面-->身份证照片反面-->行驶证照片-->行驶证副本
			 */
			switch (fileName){
				case "assert":
					deleteImg(CommonField.EVENT_URL[1], newFileName);
					break;
				case "maintenance":
					deleteImg(CommonField.EVENT_URL[2], newFileName);
					break;
				case "logo":
					deleteImg(CommonField.MAINTENANCESHOP_URL[0], newFileName);
					break;
				case "maintenanceshopImg":
					deleteImg(CommonField.MAINTENANCESHOP_URL[1], newFileName);
					break;
				case "employeeimg":
					deleteImg(CommonField.MAINTENANCESHOP_URL[2], newFileName);
					break;
				case "poster":
					deleteImg(CommonField.MAINTENANCESHOP_URL[3], newFileName);
					break;
				default:
			}
		}
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter =multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile imgFile = multiRequest.getFile(iter.next());
				if (imgFile != null) {
					// 取得当前上传文件的文件名称
					oldFileName = imgFile.getOriginalFilename();
					//获取上传文件类型的扩展名,先得到.的位置，再截取从.的下一个位置到文件的最后，最后得到扩展名
					String ext = oldFileName.substring(oldFileName.lastIndexOf(".")+1);
					//String oldName = oldFileName.substring(0,oldFileName.lastIndexOf("."));
					ext = ext.toLowerCase();//对扩展名进行小写转换
					String uuid = UUID.randomUUID().toString().replace("-","");

					//新的图片文件名 = 获取时间戳+"."+图片扩展名
					newFileName = uuid + "." + ext;
					//String.valueOf(System.currentTimeMillis())
					String fileName = imgFile.getName();
					InputStream inputStream = imgFile.getInputStream();
					byte[] bytes = readInputStream(inputStream);
					/**
					 *PHOTOGRAPHS4_URL 		定损-维修后 logo-->商铺照片-->员工-->海报
					 */
					if (fileName.contains("assert")){
						saveImg(CommonField.EVENT_URL[1], newFileName, bytes);
						buffer.append(newFileName+"_");
						flag=true;
					}
					if (fileName.contains("maintenance")){
						saveImg(CommonField.EVENT_URL[2], newFileName, bytes);
						maintenance.append(newFileName+"_");
						flag=true;
					}
					if (fileName.contains("logo")){
						saveImg(CommonField.MAINTENANCESHOP_URL[0], newFileName, bytes);
						logo.append(newFileName+"_");
						flag=true;
					}
					if (fileName.contains("maintenanceshopImg")){
						saveImg(CommonField.MAINTENANCESHOP_URL[1], newFileName, bytes);
						maintenanceshopImg.append(newFileName+"_");
						flag=true;
					}
					if (fileName.contains("employeeimg")){
						saveImg(CommonField.MAINTENANCESHOP_URL[2], newFileName, bytes);
						employeeimg.append(newFileName+"_");
						flag=true;
					}
					if (fileName.contains("poster")){
						saveImg(CommonField.MAINTENANCESHOP_URL[3], newFileName, bytes);
						poster.append(newFileName+"_");
						flag=true;
					}
					if (fileName.contains("businessLicense")){
						saveImg(CommonField.MAINTENANCESHOP_URL[4], newFileName, bytes);
						businessLicense.append(newFileName+"_");
						flag=true;
					}
					if (fileName.contains("feedback")){
						saveImg("feedback/", newFileName, bytes);
						feedback.append(newFileName+"_");
						flag=true;
					}
					if (fileName.contains("portrait")){
						saveImg("poster/", newFileName, bytes);
						portrait.append(newFileName+"_");
						flag=true;
					}

					/**
					 *商铺(保险理赔) 		理赔事件  申请理赔--定损-维修后 投诉
					 */
//					{ORDER+"accident/",ORDER+"assert/",ORDER+"repair/",ORDER+"complaint/"};
					if (fileName.contains("insuranceAssert")){
						saveImg(CommonField.ORDER_URL[1], newFileName, bytes);
						insuranceAssert.append(newFileName+"_");
						flag=true;
					}
					if (fileName.contains("insuranceRepair")){
						saveImg(CommonField.ORDER_URL[2], newFileName, bytes);
						insuranceRepair.append(newFileName+"_");
						flag=true;
					}

				}
			}
		}
		if (flag){
			resultMap.put("assert",getStringName(buffer));
			resultMap.put("maintenance",getStringName(maintenance));
			resultMap.put("logo",getStringName(logo));
			resultMap.put("maintenanceshopImg",getStringName(maintenanceshopImg));
			resultMap.put("employeeimg",getStringName(employeeimg));
			resultMap.put("poster",getStringName(poster));
			resultMap.put("businessLicense",getStringName(businessLicense));
			resultMap.put("feedback",getStringName(feedback));
			resultMap.put("portrait",getStringName(portrait));

			resultMap.put("insuranceAssert",getStringName(insuranceAssert));
			resultMap.put("insuranceRepair",getStringName(insuranceRepair));
			resultMap.put("status",true);
			System.out.println("*********************图片上传成功");
			return resultMap;
		}
		resultMap.put("resultMessage","图片上传失败");
		return resultMap;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		//创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		//每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		//使用一个输入流从buffer里把数据读取出来
		while( (len=inStream.read(buffer)) != -1 ){
			//用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		//关闭输入流
		inStream.close();
		//把outStream里的数据写入内存
		return outStream.toByteArray();
	}

	/**
	 *	获取保存在服务器的路径
	 * @param folderName	文件夹名
	 * @param fileName		图片名字
	 * @return
	 * @throws Exception
	 */
	public static Boolean saveImg(String folderName,String fileName,byte[] content) throws Exception {
		try {
			OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			String key = folderName+fileName;
			ossClient.putObject(bucketName, key, new ByteArrayInputStream(content));
			// 关闭client
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * @param oldName 原有图片
	 * @param arrI 路径
	 * @param mf 修改图片
	 * @throws Exception
	 */
	public static String saveImg(String oldName,int arrI,MultipartFile mf) throws Exception{
		String uuid = UUID.randomUUID().toString().replace("-","");
		String ext = mf.getOriginalFilename();
		 	ext = ext.substring(ext.lastIndexOf(".")+1).toLowerCase();
		if(Constant.toEmpty(ext)){
			if(Constant.toEmpty(oldName))
				UploadFileUtil.deleteImg(CommonField.PHOTOGRAPHS4_URL[arrI], oldName);
			String newFileName = uuid + "." + ext;
			UploadFileUtil.saveImg(CommonField.PHOTOGRAPHS4_URL[arrI],newFileName, readInputStream(mf.getInputStream()));
			return newFileName;
		}else{
			return null;
		}
	}

	/**
	 *	获取保存在服务器的路径
	 * @param folderName	文件夹名
	 * @param fileName		图片名字
	 * @return
	 * @throws Exception
	 */
	public static String getImgURL(String folderName,String fileName) throws Exception {
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		String key = folderName+fileName;
		// 设置URL过期时间为1小时
		Date expiration = new Date(new Date().getTime() + 3600 * 1000);
		// 生成URL
		URL url = ossClient.generatePresignedUrl(bucketName, key, expiration);
		ossClient.shutdown();
		return url.toString();
	}

	/**
	 *	删除保存在服务器的图片
	 * @param folderName	文件夹名
	 * @param fileName		图片名字
	 * @return
	 * @throws Exception
	 */
	public static Boolean deleteImg(String folderName,String fileName) {
		try {
			OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			String key = folderName+fileName;
			// 设置URL过期时间为1小时
			Date expiration = new Date(new Date().getTime() + 3600 * 1000);
			// 删除Object
			ossClient.deleteObject(bucketName, key);
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) throws Exception{
		String assert1 = CommonField.getMaintenanceShopImg(1, "c857fe97ce124f20aebd95f391400fe6.jpg");
		String maintenance = CommonField.getMaintenanceShopImg(2, "f4ae907c2f024fd6a6088aea82869a91.jpg");
		System.out.println("assert = " + assert1);
		System.out.println("maintenance = " + maintenance);
	}

	private static String getStringName(StringBuffer buffer){
		if (buffer.length()>10) {
			String substring = buffer.substring(0, buffer.length() - 1);
			return substring;
		}
		return "";
	}
	/**
	 *
	 * @param Base64S  \(^o^)/~H5格式的64位
	 * @return {"drivingLicenseFront\\","idCardFront\\","idCardReverse\\","copyDrivingLicense\\"};
	 * @throws Exception
	 */
	public static Map<String,Object> Base64ToImg(String[] Base64S) throws Exception {
		Map<String,Object> hashMap = new HashMap<>();
		//图片新名字
		String newFileName = null;
		//添加文件类型
		List fileTypes = new ArrayList();
		fileTypes.add("jpg");
		fileTypes.add("jpeg");
		fileTypes.add("bmp");
		fileTypes.add("gif");
		fileTypes.add("png");
		final String str ="http://chevdianoss001.oss-cn-beijing.aliyuncs.com";

		for (int i = 0; i < Base64S.length; i++) {
			String string = Base64S[i];
			String uuid = UUID.randomUUID().toString().replace("-", "");
			//新的图片文件名 = 获取时间戳+"."+图片扩展名
			//String.valueOf(System.currentTimeMillis())
			newFileName = uuid + ".jpg";
			if (string.contains(str)) {
				newFileName=string.substring(string.lastIndexOf("/") + 1, string.indexOf("?"));
				hashMap.put(CommonField.PHOTOGRAPHS4[i], newFileName);
			} else {
				String substring = string.substring(string.indexOf(",") + 1);
				byte[] bytes = Base64.decodeBase64(substring);
				if (Base64S.length == 1) {
					saveImg(CommonField.ASSOCIATOR_IMG_URL, newFileName, bytes);
					hashMap.put(CommonField.ASSOCIATORIMG, newFileName);
				} else {
					saveImg(CommonField.PHOTOGRAPHS4_URL[i], newFileName, bytes);
					hashMap.put(CommonField.PHOTOGRAPHS4[i], newFileName);
				}
			}
		}
		return hashMap;
	}
	public static Map<String,Object> WeChatBase64ToImg(String[] Base64S,String url) throws Exception {
		Map<String,Object> hashMap = new HashMap<>();
		//图片新名字
		String newFileName = null;
		//添加文件类型
		List fileTypes = new ArrayList();
		fileTypes.add("jpg");
		fileTypes.add("jpeg");
		fileTypes.add("bmp");
		fileTypes.add("gif");
		fileTypes.add("png");
		final String str ="http://chevdianoss001.oss-cn-beijing.aliyuncs.com";

		for (int i = 0; i < Base64S.length; i++) {
			String string = Base64S[i];
			String uuid = UUID.randomUUID().toString().replace("-", "");
			//新的图片文件名 = 获取时间戳+"."+图片扩展名
			//String.valueOf(System.currentTimeMillis())
			newFileName = uuid + ".jpg";
			if (string.contains(str)) {
				newFileName=string.substring(string.lastIndexOf("/") + 1, string.indexOf("?"));
				hashMap.put(CommonField.PHOTOGRAPHS4[i], newFileName);
			} else {
				String substring = string.substring(string.indexOf(",") + 1);
				byte[] bytes = Base64.decodeBase64(substring);
				if (Base64S.length == 1) {
					saveImg(url, newFileName, bytes);
					hashMap.put(CommonField.ASSOCIATORIMG, newFileName);
				} else {
					saveImg(CommonField.PHOTOGRAPHS4_URL[i], newFileName, bytes);
					hashMap.put(CommonField.PHOTOGRAPHS4[i], newFileName);
				}
			}
		}
		return hashMap;
	}
	
	public static Boolean UploadImg(MultipartFile file, String fileName) throws Exception{
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		try {
			ossClient.putObject(bucketName, fileName, new ByteArrayInputStream(file.getBytes()));
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
