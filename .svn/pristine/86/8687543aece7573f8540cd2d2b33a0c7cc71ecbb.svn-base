package com.zccbh.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zccbh.demand.mapper.merchants.CbhMaintenanceshopMapper;
import com.zccbh.util.uploadImg.UploadFileUtil;

/** 
 * @ClassName: ImgTest 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年11月27日 下午5:03:03 
 *  
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/zccbh/config/spring.xml")
public class ImgTest {
	
	@Autowired
	private CbhMaintenanceshopMapper cbhMaintenanceshopMapper;
	
	@Test
	public void getImgUrl(){
		String folderName = "maintenanceshop/qrcode";
		List<String> qrcodes = cbhMaintenanceshopMapper.getShopQrcode();
		for(String name : qrcodes){
			try {
				String url = UploadFileUtil.getImgURL(folderName, name);
				System.out.println(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
