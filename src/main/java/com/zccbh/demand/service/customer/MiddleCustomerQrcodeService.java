package com.zccbh.demand.service.customer;

import com.zccbh.demand.controller.weChat.WeixinConstants;
import com.zccbh.demand.mapper.customer.MiddleCustomerQrcodeMapper;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.HttpUtils;
import com.zccbh.util.uploadImg.UploadFileUtil;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class MiddleCustomerQrcodeService {

    @Autowired
    private MiddleCustomerQrcodeMapper middleCustomerQrcodeMapper;

    @Autowired
    private WeiXinUtils weiXinUtils;

    private Logger logger = LoggerFactory.getLogger(MiddleCustomerQrcodeService.class);

    public String getQrcodeByCustomerId(Integer customerId) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("customerId", customerId);
        Map<String, Object> rs = middleCustomerQrcodeMapper.findByCustomerId(param);
        String ticket = null;
        if (!CollectionUtils.isEmpty(rs)) {
            ticket = rs.get("qrcode").toString();
        } else {
            String eventKey = String.format("%s_u_%s", customerId, 5); // 用户id_类型_活动id
            ticket = getQrcodeFromWeXin(eventKey);
            param.put("qrcode", ticket);
            param.put("customerId", customerId);
            saveSingle(param);
        }
        return ticket;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSingle(Map<String, Object> param) throws Exception {
        middleCustomerQrcodeMapper.saveSingle(param);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateModel(Map<String, Object> param) throws Exception {
        return middleCustomerQrcodeMapper.updateModel(param);
    }

    public Map<String, Object> findSingle(Map<String, Object> param) throws Exception {
        return middleCustomerQrcodeMapper.findSingle(param);
    }

    /**
     * 生成用户专属永久二维码
     *
     * @param eventKey
     * @return
     * @throws Exception
     */
    private String getQrcodeFromWeXin(String eventKey) throws Exception {
        String accessToken = weiXinUtils.getAccessToken();
        String url = WeixinConstants.QRCODE_URL.replace("ACCESS_TOKEN", accessToken);
        JSONObject json = new JSONObject();
        json.put("action_name", "QR_LIMIT_STR_SCENE");
        JSONObject str = new JSONObject();
        str.put("scene_str", eventKey);
        JSONObject info = new JSONObject();
        info.put("scene", str);
        json.put("action_info", info);
        HttpResponse response = HttpUtils.sendPost(url, json.toString());
        String content = EntityUtils.toString(response.getEntity(), "utf-8");
        String ticket = JSONObject.fromObject(content).getString("ticket");
        return downloadTicket(ticket);
    }

    private String downloadTicket(String ticket) throws Exception {
        HttpClient hClient = new DefaultHttpClient();
        String ticketUrl = WeixinConstants.QRCODE_URL_GET.replace("TICKET", URLEncoder.encode(ticket, "utf-8"));
        HttpGet httpGet = new HttpGet(ticketUrl);
        HttpResponse response = hClient.execute(httpGet);
        if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            String newFileName = uuid + ".jpg";
            // 上传至阿里云
            Boolean uploadResult = UploadFileUtil.saveImg(CommonField.POSTER_IMG_URL, newFileName, IOUtils.toByteArray(in));
            logger.info("上传到阿里云的图片({})结果为: {}", CommonField.POSTER_IMG_URL + newFileName, uploadResult);
            if (!uploadResult) {
                downloadTicket(ticket);
            }
            return newFileName;
        }
        return null;
    }
}
