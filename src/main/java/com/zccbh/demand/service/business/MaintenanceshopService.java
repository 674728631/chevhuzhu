package com.zccbh.demand.service.business;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.controller.weChat.WeixinConstants;
import com.zccbh.demand.mapper.business.*;
import com.zccbh.demand.mapper.customer.MessageMapper;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import com.zccbh.demand.mapper.merchants.CbhMaintenanceshopMapper;
import com.zccbh.demand.service.customer.RecordRechargeService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.*;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.MD5Util;
import com.zccbh.util.qrcode.QRCodeUtil;
import com.zccbh.util.uploadImg.UploadFileUtil;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import javax.swing.SortingFocusTraversalPolicy;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MaintenanceshopService {
    @Autowired
    private MaintenanceshopMapper maintenanceshopMapper;

    @Autowired
    private UserBusinessMapper businessMapper;

    @Autowired
    private MiddleBusinessMaintenanceshopMapper middleBusinessMaintenanceshopMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MaintenanceshopEmployeeMapper employeeMapper;

    @Autowired
    private WeiXinUtils weiXinUtils;

    @Autowired
    private RedisUtil redisUtil;
    
    @Autowired
    private CbhMaintenanceshopMapper cbhMaintenanceshopMapper;
    
    @Autowired
    private RecordRechargeMapper recordRechargeMapper;

    /**
     * 修改商家
     */
    @Transactional
    public String updateModel(Map<String, Object> paramModelMap) throws Exception{
    	
    	 //创建二维码
        redisUtil.delect("accessToken2");
        String accessToken =  weiXinUtils.getAccessToken();
        String url = WeixinConstants.QRCODE_URL.replace("ACCESS_TOKEN", accessToken);
//        String bodys = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+ paramModelMap.get("id") +"}}}";
        String bodys = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": "+ String.valueOf(paramModelMap.get("id")) +"}}}";
        HttpResponse response = HttpUtils.sendPost(url,bodys);
        String content = EntityUtils.toString(response.getEntity(), "utf-8");
        String qrcode_url = JSONObject.fromObject(content).getString("url");
        String qrcodeName = QRCodeUtil.saveQrcode(qrcode_url,(String) paramModelMap.get("logoPath"),"maintenanceshop/qrcode/");
        //创建活动海报
        String posterName = QRCodeUtil.savePoster(qrcode_url,(String) paramModelMap.get("logoPath"),(String) paramModelMap.get("backgroundPath"),"maintenanceshop/poster/");
        //保存二维码和活动海报
        paramModelMap.put("id",paramModelMap.get("id"));
        paramModelMap.put("qrcode",qrcodeName);
        paramModelMap.put("poster",posterName);
    	
        maintenanceshopMapper.updateModel(paramModelMap);
        return "0";
    }
    

    /**
     * 添加其他渠道
     */
    @Transactional
    public String saveChannel(Map<String, Object> paramModelMap) throws Exception {
        //添加商家时同时为商家创建提款账户
        Map map = new HashMap();
        map.put("createAt",new Date());
        accountMapper.saveSingle(map);
        paramModelMap.put("accountId",map.get("id"));
        //保存其他渠道
        maintenanceshopMapper.saveSingle(paramModelMap);
        //创建商家用户
        String tel = "";
        if(Constant.toEmpty(paramModelMap.get("tel"))){
            tel = paramModelMap.get("tel").toString();
        }else {
            throw new RuntimeException("4001");
        }
        map.clear();
        map.put("businessUN",tel);
        map.put("businessPN",tel);
        List<Map<String, Object>> businessList = businessMapper.isExist(map);
        if(businessList.size()>0){
            throw new RuntimeException("4003");
        }else {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("businessUN",tel);
            map2.put("businessPN",tel);
            map2.put("typeUser",1);
            map2.put("businessPW",MD5Util.getMD5Code(tel.substring(tel.length()-6)));
            businessMapper.saveSingle(map2);
            map.clear();
            map.put("businessId",map2.get("id"));
            map.put("maintenanceshopId",paramModelMap.get("id"));
            middleBusinessMaintenanceshopMapper.saveSingle(map);
        }
        //创建二维码
        redisUtil.delect("accessToken2");
        String accessToken =  weiXinUtils.getAccessToken();
        String url = WeixinConstants.QRCODE_URL.replace("ACCESS_TOKEN", accessToken);
        String bodys = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+ paramModelMap.get("id") +"}}}";
        HttpResponse response = HttpUtils.sendPost(url,bodys);
        String content = EntityUtils.toString(response.getEntity(), "utf-8");
        String qrcode_url = JSONObject.fromObject(content).getString("url");
        String qrcodeName = QRCodeUtil.saveQrcode(qrcode_url,(String) paramModelMap.get("logoPath"),"maintenanceshop/qrcode/");
        //创建活动海报
        String posterName = QRCodeUtil.savePoster(qrcode_url,(String) paramModelMap.get("logoPath"),(String) paramModelMap.get("backgroundPath"),"maintenanceshop/poster/");
        //保存二维码和活动海报
        map.clear();
        map.put("id",paramModelMap.get("id"));
        map.put("qrcode",qrcodeName);
        map.put("poster",posterName);
        maintenanceshopMapper.updateModel(map);
        return "0";
    }
    
    @Transactional
    public String updateQrcode(Map<String, Object> map)throws Exception{    	
        //创建二维码
        redisUtil.delect("accessToken2");
        String accessToken =  weiXinUtils.getAccessToken();
        String url = WeixinConstants.QRCODE_URL.replace("ACCESS_TOKEN", accessToken);
        String bodys = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+ map.get("id") +"}}}";
        HttpResponse response = HttpUtils.sendPost(url,bodys);
        String content = EntityUtils.toString(response.getEntity(), "utf-8");
        String qrcode_url = JSONObject.fromObject(content).getString("url");
        String qrcodeName = QRCodeUtil.saveQrcode(qrcode_url,(String) map.get("logoPath"),"maintenanceshop/qrcode/");
        //创建活动海报
        String posterName = QRCodeUtil.savePoster(qrcode_url,(String) map.get("logoPath"),(String) map.get("backgroundPath"),"maintenanceshop/poster/");
        //保存二维码和活动海报
        map.clear();
        map.put("id",map.get("id"));
        map.put("qrcode",qrcodeName);
        map.put("poster",posterName);
        maintenanceshopMapper.updateModel(map);
        return "0";
    }

    /**
     * 添加商家
     * @param paramModelMap 商家信息
     * @return 添加结果信息
     * @throws Exception
     */
    @Transactional
    public String saveMaintenanceshop(Map<String, Object> paramModelMap) throws Exception {
        //判断输入的比率是否合法
        if(Constant.toEmpty(paramModelMap.get("ratio"))){
            try {
                paramModelMap.put("ratio",new BigDecimal((String) paramModelMap.get("ratio")));
            }catch (Exception e){
                return "4001";
            }
        }

        //添加商家时同时为商家创建提款账户
        Map map = new HashMap();
        map.put("createAt",new Date());
        accountMapper.saveSingle(map);
        paramModelMap.put("accountId",map.get("id"));

        //根据商家地址获取它的经纬度
        Map<String, Object> latAndLngMap = GetLatAndLngByGaoDe.getLatitudeAndLongitude((String) paramModelMap.get("address"));
        if(Constant.toEmpty(latAndLngMap)){
            paramModelMap.put("latitude",latAndLngMap.get("latitude"));
            paramModelMap.put("longitude",latAndLngMap.get("longitude"));
        }
        //保存商家
        maintenanceshopMapper.saveSingle(paramModelMap);
        //创建商家用户
        String tel = "";
        if(Constant.toEmpty(paramModelMap.get("tel"))){
            tel = paramModelMap.get("tel").toString();
        }else {
            throw new RuntimeException("4001");
        }
        map.clear();
        map.put("businessUN",tel);
        map.put("businessPN",tel);
        List<Map<String, Object>> businessList = businessMapper.isExist(map);
        if(businessList.size()>0){
            throw new RuntimeException("4003");
        }else {
            Map<String, Object> map2 = new HashMap<>();
            map2.put("businessUN",tel);
            map2.put("businessPN",tel);
            map2.put("typeUser",1);
            map2.put("businessPW",MD5Util.getMD5Code(tel.substring(tel.length()-6)));
            businessMapper.saveSingle(map2);
            map.clear();
            map.put("businessId",map2.get("id"));
            map.put("maintenanceshopId",paramModelMap.get("id"));
            middleBusinessMaintenanceshopMapper.saveSingle(map);
        }
        //创建商家二维码
        redisUtil.delect("accessToken2");
        String accessToken =  weiXinUtils.getAccessToken();
        String url = WeixinConstants.QRCODE_URL.replace("ACCESS_TOKEN", accessToken);
        String bodys = "{\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": "+ paramModelMap.get("id") +"}}}";
        HttpResponse response = HttpUtils.sendPost(url,bodys);
        String content = EntityUtils.toString(response.getEntity(), "utf-8");
        String qrcode_url = JSONObject.fromObject(content).getString("url");
        String qrcodeName = QRCodeUtil.saveQrcode(qrcode_url,(String) paramModelMap.get("logoPath"),"maintenanceshop/qrcode/");
        //创建活动海报
        String posterName = QRCodeUtil.savePoster(qrcode_url,(String) paramModelMap.get("logoPath"),(String) paramModelMap.get("backgroundPath"),"maintenanceshop/poster/");
        //保存商家二维码和活动海报
        map.clear();
        map.put("id",paramModelMap.get("id"));
        map.put("qrcode",qrcodeName);
        map.put("poster",posterName);
        maintenanceshopMapper.updateModel(map);
        return "0";
    }

    /**
     * 修改商家
     * @param paramModelMap 商家信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String updateMaintenanceshop(Map<String, Object> paramModelMap) throws Exception{
        //判断输入的比率是否合法
        if(Constant.toEmpty(paramModelMap.get("ratio"))){
            try {
                paramModelMap.put("ratio",new BigDecimal((String) paramModelMap.get("ratio")));
            }catch (Exception e){
                return "4001";
            }
        }

        //根据商家地址获取它的经纬度
        Map<String, Object> latAndLngMap = GetLatAndLngByGaoDe.getLatitudeAndLongitude((String) paramModelMap.get("address"));
        if(Constant.toEmpty(latAndLngMap)){
            paramModelMap.put("latitude",latAndLngMap.get("latitude"));
            paramModelMap.put("longitude",latAndLngMap.get("longitude"));
        }

        maintenanceshopMapper.updateModel(paramModelMap);
        return "0";
    }
    public Map<String,Object> findSingle(Map<String, Object> paramModelMap) throws Exception{
    	return maintenanceshopMapper.findSingle(paramModelMap);
    }

    /**
     * 删除商家
     * @param businessId 商家id
     * @return
     * @throws Exception
     */
    @Transactional
    public String deleteMaintenanceshop(Integer businessId) throws Exception{
        maintenanceshopMapper.deleteModel(businessId);
        return "0";
    }

    /**
     * 查询商家数据
     * @param paramModelMap 查询条件
     * @return 商家信息
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findMaintenanceshopList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> maintenanceshopList = maintenanceshopMapper.findMore(paramModelMap);
        for(Map<String, Object> maintenanceShop : maintenanceshopList){
        	//保障数
        	BigDecimal carNum = null;
        	if (maintenanceShop.get("carNum") != null) {
        		carNum = new BigDecimal(maintenanceShop.get("carNum").toString());
			}else{
				carNum = new BigDecimal(0);
			}
        	//申请理赔数
        	BigDecimal eventApplyNum = null;
        	if (maintenanceShop.get("eventApplyNum") != null) {
        		eventApplyNum = new BigDecimal(maintenanceShop.get("eventApplyNum").toString());
			}else{
				eventApplyNum = new BigDecimal(0);
			}
        	
        	//理赔数
        	BigDecimal eventNum = null;
        	if (maintenanceShop.get("eventNum") != null) {
        		eventNum = new BigDecimal(maintenanceShop.get("eventNum").toString());
			}else{
				eventNum = new BigDecimal(0);
			}
       
        	//理赔支出
        	BigDecimal expenses = null;
        	if (maintenanceShop.get("expenses") != null) {
        		expenses = new BigDecimal(maintenanceShop.get("expenses").toString());
			}else{
				expenses = new BigDecimal(0);
			}
        	
        	//充值金额
        	Map<String, Object> reqMap = new HashMap<>();
        	reqMap.put("id", maintenanceShop.get("id"));
        	BigDecimal chargeNum = null;
        	Map<String, Object> rechargeNum = recordRechargeMapper.getRechargeNum(reqMap);
        	if (rechargeNum.get("rechargeNum") != null ) {
				chargeNum = new BigDecimal(rechargeNum.get("rechargeNum").toString());
			}else{
				chargeNum = new BigDecimal(0);
			}
        	if (carNum.compareTo(new BigDecimal(0)) == 0) {
        		maintenanceShop.put("applyRate", 0);
			}else{
				if (eventApplyNum.compareTo(new BigDecimal(0)) == 0) {
	        		maintenanceShop.put("applyRate", 0);
				}else{
					maintenanceShop.put("applyRate", eventApplyNum.divide(carNum, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
				}
			}
        	
        	if (eventApplyNum.compareTo(new BigDecimal(0)) == 0) {
        		maintenanceShop.put("passRate", 0);
			}else{
				if (eventNum.compareTo(new BigDecimal(0)) == 0) {
	        		maintenanceShop.put("passRate", 0);
				}else{
					maintenanceShop.put("passRate", eventNum.divide(eventApplyNum, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
				}
			}
        	
        	if (chargeNum.compareTo(new BigDecimal(0)) == 0) {
        		maintenanceShop.put("eventRate", expenses.divide(new BigDecimal(1), 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			}else{
				if (expenses.compareTo(new BigDecimal(0)) == 0) {
	        		maintenanceShop.put("eventRate", 0);
				}else{
					maintenanceShop.put("eventRate", expenses.divide(chargeNum, 3, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
				}
			}
        }
        PageInfo<Map<String, Object>> maintenanceshopInfo = new PageInfo<>(maintenanceshopList);
        return maintenanceshopInfo;
    }

    /**
     * 查询可以分单的商家数据
     * @param paramModelMap 查询条件
     * @return 商家信息
     * @throws Exception
     */
    public List<Map<String, Object>> findCanDistribution(Map<String, Object> paramModelMap) throws Exception {
        List<Map<String, Object>> maintenanceshopList = maintenanceshopMapper.findCanDistribution(paramModelMap);
        if(Constant.toEmpty(paramModelMap.get("latitude")) && Constant.toEmpty(paramModelMap.get("longitude"))){
            Double laCarOwner = Double.valueOf(String.valueOf(paramModelMap.get("latitude")));
            Double loCarOwner = Double.valueOf(String.valueOf(paramModelMap.get("longitude")));
            for (Map<String, Object> maintenanceshop:maintenanceshopList) {
                if(String.valueOf(maintenanceshop.get("latitude")).equals("null")){
                    continue;
                }
                Double laShop = Double.valueOf(String.valueOf(maintenanceshop.get("latitude")));
                Double loShop = Double.valueOf(String.valueOf(maintenanceshop.get("longitude")));
                Double dis = LocationUtils.getDistance(laCarOwner, loCarOwner, laShop, loShop);
                maintenanceshop.put("distance", dis);
            }
            Collections.sort(maintenanceshopList, new Comparator<Map<String,Object>>() {
                public int compare(Map<String,Object> o1, Map<String,Object> o2) {
                    return new BigDecimal(String.valueOf(o1.get("distance")).equals("null")?"0":String.valueOf(o1.get("distance"))).compareTo(new BigDecimal(String.valueOf(o2.get("distance")).equals("null")?"0":String.valueOf(o2.get("distance"))));
                }
            });
        }
        return maintenanceshopList;
    }

    /**
     * 查询可以参加活动的商家数据
     * @param paramModelMap 查询条件
     * @return 商家信息
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findActivityShop(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> maintenanceshopList = maintenanceshopMapper.findActivityShop(paramModelMap);
        PageInfo<Map<String, Object>> maintenanceshopInfo = new PageInfo<>(maintenanceshopList);
        return maintenanceshopInfo;
    }

    /**
     * 查询商家数据
     */
    public Map<String, Object> findShopDetail(Map<String, Object> paramModelMap) throws Exception {
        Map<String, Object> maintenanceshopDetail = maintenanceshopMapper.findDetail(paramModelMap);
        //解析图片
        if(Constant.toEmpty(maintenanceshopDetail.get("img"))){
            maintenanceshopDetail.put("img", CommonField.getMaintenanceShopImgList(1,(String) maintenanceshopDetail.get("img")));
        }
        if(Constant.toEmpty(maintenanceshopDetail.get("businessLicenseImg"))){
            maintenanceshopDetail.put("businessLicenseImg", CommonField.getMaintenanceShopImg(4,(String) maintenanceshopDetail.get("businessLicenseImg")));
        }
        if(Constant.toEmpty(maintenanceshopDetail.get("qrcode"))){
            maintenanceshopDetail.put("qrcode", CommonField.getMaintenanceShopImg(5,(String) maintenanceshopDetail.get("qrcode")));
        }
        if(Constant.toEmpty(maintenanceshopDetail.get("logo"))){
            maintenanceshopDetail.put("logo", CommonField.getMaintenanceShopImg(0,(String) maintenanceshopDetail.get("logo")));
        }
        //获取商家总金额
        Map<String, Object> accountDetail = accountMapper.findDetail(paramModelMap);
        BigDecimal totalAmt = new BigDecimal(0);
        totalAmt = totalAmt.add(new BigDecimal(accountDetail.get("amtPaid").toString()))
            .add(new BigDecimal(Constant.toOr(accountDetail.get("amtUnfreeze").toString(), Constant.toReadPro("orKey"), "decrypt")))
            .add(new BigDecimal(Constant.toOr(accountDetail.get("amtFreeze").toString(), Constant.toReadPro("orKey"), "decrypt")))
            .setScale(0, RoundingMode.HALF_UP);
        maintenanceshopDetail.put("totalAmt",totalAmt);
        //获取商家总接单量
        Map<String, Object> eventCount = eventMapper.findShopEvent(paramModelMap);
        maintenanceshopDetail.put("orderNum",eventCount.get("orderNum"));
        //获取商家员工人数
        Map<String, Object> empCount = employeeMapper.findShopEmp(paramModelMap);
        maintenanceshopDetail.put("empNum",empCount.get("empNum"));
        return maintenanceshopDetail;
    }

    /**
     * 查询其他渠道数据
     */
    public Map<String, Object> findChannelDetail(Map<String, Object> paramModelMap) throws Exception {
        Map<String, Object> channelDetail = maintenanceshopMapper.findDetail(paramModelMap);
        if(Constant.toEmpty(channelDetail.get("qrcode"))){
            channelDetail.put("qrcode", CommonField.getMaintenanceShopImg(5,(String) channelDetail.get("qrcode")));
        }
        return channelDetail;
    }

    public PageInfo<Map<String, Object>> findShopSortList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, 1000000);
        List<Map<String, Object>> maintenanceshopList = maintenanceshopMapper.findShopList(paramModelMap);
        for(int i=0;i<maintenanceshopList.size();i++){
        	Map<String, Object> m1 = maintenanceshopList.get(i);
            String img = String.valueOf(m1.get("logo"));
    		if(!img.equals("null")){
    			m1.put("logo", UploadFileUtil.getImgURL("maintenanceshop/maintenanceshopImg/",img));
    		}
        }
        if(String.valueOf(paramModelMap.get("latitude")).equals("null")||String.valueOf(paramModelMap.get("longitude")).equals("null")){
            PageInfo<Map<String, Object>> maintenanceshopInfo = new PageInfo<>(maintenanceshopList);
            return maintenanceshopInfo;
        }
        Double la = Double.valueOf(String.valueOf(paramModelMap.get("latitude")));
        Double lo = Double.valueOf(String.valueOf(paramModelMap.get("longitude")));
        if(paramModelMap.get("distanceSort")!=null&&!"".equals(paramModelMap.get("distanceSort"))){
            /*for(int i=0;i<maintenanceshopList.size();i++){
                Map<String, Object> m1 = maintenanceshopList.get(i);
                if(String.valueOf(m1.get("latitude")).equals("null")){
                	continue;
                }
                Double la1 = Double.valueOf(String.valueOf(m1.get("latitude")));
                Double lo1 = Double.valueOf(String.valueOf(m1.get("longitude")));
                for(int j=i+1;j<maintenanceshopList.size();j++){
                    Map<String, Object> m2 = maintenanceshopList.get(j);
                    if(String.valueOf(m2.get("latitude")).equals("null")){
                    	continue;
                    }
                    Double la2 = Double.valueOf(String.valueOf(m2.get("latitude")));
                    Double lo2 = Double.valueOf(String.valueOf(m2.get("longitude")));
                    Double d1 = LocationUtils.getDistance(la, lo, la1, lo1);
                    Double d2 = LocationUtils.getDistance(la, lo, la2, lo2);
                    m1.put("distance", d1);
                    m2.put("distance", d2);
                    if(d1<d2){
                        maintenanceshopList.set(i, m2);
                        maintenanceshopList.set(j, m1);
                    }
                }
            }*/
        	for(int i=0;i<maintenanceshopList.size();i++){
                Map<String, Object> m1 = maintenanceshopList.get(i);
                if(String.valueOf(m1.get("latitude")).equals("null")){
                	continue;
                }
                Double la1 = Double.valueOf(String.valueOf(m1.get("latitude")));
                Double lo1 = Double.valueOf(String.valueOf(m1.get("longitude")));
                Double d1 = LocationUtils.getDistance(la, lo, la1, lo1);
                m1.put("distance", d1);
                
                String img = String.valueOf(m1.get("logo"));
        		if(!img.equals("null")){
        			m1.put("logo", UploadFileUtil.getImgURL("maintenanceshop/maintenanceshopImg/",img));
        		}
            }
        	Collections.sort(maintenanceshopList, new Comparator<Map<String,Object>>() {
                public int compare(Map<String,Object> o1, Map<String,Object> o2) {
                    return new BigDecimal(String.valueOf(o1.get("distance")).equals("null")?"0":String.valueOf(o1.get("distance"))).compareTo(new BigDecimal(String.valueOf(o2.get("distance")).equals("null")?"0":String.valueOf(o2.get("distance"))));
                }
            });
        }else{
            for(int i=0;i<maintenanceshopList.size();i++){
                Map<String, Object> m1 = maintenanceshopList.get(i);
                Double la1 = Double.valueOf(String.valueOf(m1.get("latitude")));
                Double lo1 = Double.valueOf(String.valueOf(m1.get("longitude")));
                Double d1 = LocationUtils.getDistance(la, lo, la1, lo1);
                maintenanceshopList.get(i).put("distance", d1);
            }
        }
        PageInfo<Map<String, Object>> maintenanceshopInfo = new PageInfo<>(maintenanceshopList);
        return maintenanceshopInfo;
    }

    /**
     * 查询商家数量
     * @throws Exception
     */
    public Map<String,Object> findShopCount(Map<String, Object> paramModelMap) throws Exception{
        return maintenanceshopMapper.findCount(paramModelMap);
    }

    /**
     *  商家服务分记录
     */
    public List<Map<String, Object>> servicePointsRecord(Map<String, Object> paramModelMap) throws Exception {
        return messageMapper.findServicePoints(paramModelMap);
    }
    
    public Map<String, Object> getMaintenanceShopDetail(Map<String, Object> requestMap) throws Exception{
    	Map<String, Object> shopMap = cbhMaintenanceshopMapper.getMaintenanceShopDetail(requestMap);
    	String qrcode = UploadFileUtil.getImgURL("maintenanceshop/qrcode/",shopMap.get("qrcode").toString());
    	shopMap.put("qrcode", qrcode);
    	return shopMap;
    }
}


