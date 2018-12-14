/**   
 * @author xiaowuge  
 * @date 2018年9月10日  
 * @version 1.0  
 */ 
package com.zccbh.demand.controller.business;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zccbh.util.uploadImg.UploadFileUtil;

/** 
 * @ClassName: ImgUpload 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年9月10日 下午3:24:20 
 *  
 */
@Controller
@RequestMapping("/img")
public class ImgUpload {
	
	@PostMapping("/upload")
	@ResponseBody
	public Boolean upload(MultipartFile file, String fileName){
		try {
			UploadFileUtil.UploadImg(file, fileName);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	@PostMapping("delete")
	@ResponseBody
	public Boolean delete(String name){
		String folderName = "";
		try {
			UploadFileUtil.deleteImg(folderName, name);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	@PostMapping("getUrl")
	@ResponseBody
	public String getUrl(String name){
		System.out.println("name : "+ name);
		String folderName = "icon/";
		try {
			String url = UploadFileUtil.getImgURL(folderName, name);
			return url;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
