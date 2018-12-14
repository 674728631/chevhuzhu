package com.zccbh.demand.service.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.mapper.business.AccountMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.mapper.foundation.OrderStatisticalMapper;
import com.zccbh.demand.mapper.merchants.CbhAccountDetailMapper;
import com.zccbh.demand.mapper.merchants.CbhMessageMapper;
import com.zccbh.demand.mapper.order.OrderMapper;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.customer.MessageBackstageService;
import com.zccbh.demand.service.customer.MessageService;
import com.zccbh.demand.service.event.EventService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.test.exception.CustomException;
import com.zccbh.util.base.Base64;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.base.SmsDemo;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.uploadImg.UploadFileUtil;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private CarService carService;
	@Autowired
	private CarMapper carMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WeiXinUtils weiXinUtils;
	@Autowired
	private RecordPaymentService recordPaymentService;
	@Autowired
	private AccountMapper accountMapper;
	@Autowired
	private FoundationMapper foundationMapper;
    @Autowired
    private EventService eventService;
    @Autowired
    private CbhAccountDetailMapper cbhAccountDetailMapper;
    @Autowired
    private OrderStatisticalMapper orderStatisticalMapper;
    @Autowired
    private SchedulerFactoryBean schedulerFactory;
    @Autowired
    private CbhMessageMapper cbhMessageMapper;
    @Autowired
    private MessageBackstageService messageBackstageService;


	/**
	 * 创建保险理赔订单
	 */
	@Transactional
	public void saveOrder(Map<String, Object> paramModelMap) throws Exception{
		orderMapper.saveSingle(paramModelMap);
	}

	/**
	 * 修改保险理赔订单
	 */
	@Transactional
	public void updateOrder(Map<String, Object> paramModelMap) throws Exception {
		orderMapper.updateModel(paramModelMap);
	}

	/**
	 * 按状态统计订单数量
	 * @throws Exception
	 */
	public Map<String,Object> findOrderCount(Map<String, Object> paramModelMap) throws Exception{
		return orderMapper.findCount(paramModelMap);
	}

	/**
	 * 查询保险理赔列表信息
	 */
	public PageInfo<Map<String, Object>> findOrderList(Map<String, Object> paramModelMap) throws Exception {
		int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
		int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
		PageHelper.startPage(pageNo, pageSize);
		List<Map<String, Object>> orderList = orderMapper.findOrderList(paramModelMap);
		return new PageInfo<>(orderList);
	}

	/**
	 * 查询保险理赔详情
	 */
	public Map<String, Object> findDetail(Map<String, Object> paramModelMap) throws Exception {
		Map<String, Object> order = orderMapper.findDetail(paramModelMap);
		if(Constant.toEmpty(order.get("drivingLicense"))){
			order.put("drivingLicense",CommonField.getCarDrivingUrl((String) order.get("drivingLicense")));
		}
        if(Constant.toEmpty(order.get("carPhotos"))){
            order.put("carPhotos",CommonField.getCarUrl((String) order.get("carPhotos")));
        }
		if(Constant.toEmpty(order.get("accidentImg"))){
			List<String> accidentImg = CommonField.getImgLists(1,0,(String) order.get("accidentImg"));
			order.put("accidentImg",accidentImg);
		}
		if(Constant.toEmpty(order.get("assertImg"))){
			List<String> assertImg = CommonField.getImgLists(1,1,(String) order.get("assertImg"));
			order.put("assertImg",assertImg);
		}
		if(Constant.toEmpty(order.get("repairImg"))){
			List<String> repairImg = CommonField.getImgLists(1,2,(String) order.get("repairImg"));
			order.put("repairImg",repairImg);
		}
		if(Constant.toEmpty(order.get("complaintImg"))){
			List<String> complaintImg = CommonField.getImgLists(1,3,(String) order.get("complaintImg"));
			order.put("complaintImg",complaintImg);
		}
		return order;
	}

	/**
	 * 申请理赔审核通过
	 * 
	 * 保险理赔
	 */
	@Transactional
	public void applysuccess(Map<String, Object> paramModelMap) throws Exception {
		//修改保险理赔订单状态
		Map map = new HashMap();
		map.put("orderNo",paramModelMap.get("orderNo"));
		if(Constant.toEmpty(paramModelMap.get("examineExplanation"))){
            map.put("examineExplanation",paramModelMap.get("examineExplanation"));
        }else {
            map.put("examineExplanation2","将examineExplanation设为空");
        }
		map.put("examineTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		map.put("status",3);
		updateOrder(map);
		//查询车辆详情信息
		map.clear();
		map.put("carId",paramModelMap.get("carId"));
		Map<String,Object> carInfo = carMapper.findCarInfo(map);
        //推送消息给用户告知他审核通过
        map.clear();
        map.put("customerId",paramModelMap.get("customerId"));
        map.put("licensePlateNumber",carInfo.get("licensePlateNumber"));
        map.put("eventNo",paramModelMap.get("orderNo"));
        map.put("type",41);
        map.put("title","理赔消息");
        map.put("content",carInfo.get("licensePlateNumber") + "的理赔订单申请审核通过，请及时处理。");
        messageService.saveMessage(map);
        //微信推送
        if (Constant.toEmpty(carInfo.get("openId"))) {
            map.clear();
            map.put("openid", carInfo.get("openId"));
            map.put("eventNo", paramModelMap.get("orderNo"));
            map.put("licensePlateNumber", carInfo.get("licensePlateNumber"));
            map.put("content", "爱车"+carInfo.get("licensePlateNumber")+"申请理赔审核通过");
            map.put("theme", "保险代理订单");
            map.put("keyword1", "审核通过");
            map.put("keyword2", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
            map.put("url", Constant.toReadPro("realURL")+"hfive/view/baoxian_order_detail.html?id="+paramModelMap.get("orderNo"));
            weiXinUtils.sendTemplate(1, map);
        }
        //短信通知
        SmsDemo.sendSms(11,carInfo.get("customerPN").toString(),carInfo.get("licensePlateNumber").toString());
	}

	/**
	 * 申请理赔审核不通过
	 */
	@Transactional
	public void applyfail(Map<String, Object> paramModelMap) throws Exception {
		//修改保险理赔订单状态
		Map map = new HashMap();
		map.put("orderNo",paramModelMap.get("orderNo"));
		map.put("examineExplanation",paramModelMap.get("examineExplanation"));
		map.put("examineTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		map.put("status",2);
		updateOrder(map);
		//查询车辆详情信息
		map.clear();
		map.put("carId",paramModelMap.get("carId"));
		Map<String,Object> carInfo = carMapper.findCarInfo(map);
        //推送消息给用户告知他审核不通过
        map.clear();
        map.put("customerId",paramModelMap.get("customerId"));
        map.put("licensePlateNumber",carInfo.get("licensePlateNumber"));
        map.put("eventNo",paramModelMap.get("orderNo"));
        map.put("type",41);
        map.put("title","理赔消息");
        map.put("content",carInfo.get("licensePlateNumber") + "的理赔订单申请审核未通过，请及时处理。");
        messageService.saveMessage(map);
        //微信推送给用户
        if (Constant.toEmpty(carInfo.get("openId"))) {
            map.clear();
            map.put("openid", carInfo.get("openId"));
            map.put("licensePlateNumber",carInfo.get("licensePlateNumber"));
            map.put("theme", "理赔代办订单");
            map.put("keyword1", "理赔申请");
            map.put("remark", paramModelMap.get("examineExplanation"));
			map.put("url", Constant.toReadPro("realURL")+"hfive/view/baoxian_order_detail.html?id="+paramModelMap.get("orderNo"));
            weiXinUtils.sendTemplate(9, map);
        }
        //短信通知
        SmsDemo.sendSms(21,carInfo.get("customerPN").toString(),carInfo.get("licensePlateNumber").toString());
	}

	/**
	 * 定损审核通过
	 */
	@Transactional
	public void assertSuccess(Map<String, Object> paramModelMap) throws Exception {
		Map map = new HashMap();
		//修改保险理赔订单状态
		map.put("orderNo",paramModelMap.get("orderNo"));
		map.put("status",41);
		map.put("amtAssert",new BigDecimal((String) paramModelMap.get("amtAssert")));
		map.put("amtBusiness",new BigDecimal((String) paramModelMap.get("amtBusiness")));
		map.put("explanationAssert",paramModelMap.get("explanationAssert"));
		map.put("comfirmAssertTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		updateOrder(map);
	}

	/**
	 * 处理投诉
	 */
	@Transactional
	public void complaintsuccess(Map<String, Object> paramModelMap) throws Exception {
		Map map = new HashMap();
		//修改保险理赔订单状态
		map.put("orderNo",paramModelMap.get("orderNo"));
		map.put("status",51);
		map.put("solveComplaintTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		updateOrder(map);
	}

	/**
	 * 废弃订单
	 */
	@Transactional
	public void invalidOrder(Map<String, Object> paramModelMap) throws Exception {
        //查询保险理赔详情
        Map detail = orderMapper.findDetail(paramModelMap);
        //判断订单是否作废，未作废才能进行作废操作
        Integer isInvalid = (Integer) detail.get("isInvalid");
        if(isInvalid == 1){
            Map map = new HashMap();
            //修改保险理赔订单状态
            map.put("orderNo",paramModelMap.get("orderNo"));
            map.put("isInvalid",10);
            map.put("invalidTime", DateUtils.formatDate(new Date()));
            updateOrder(map);

            //接单状态的时候删除 接单定时器和message里面的接单消息
            Integer status = (Integer) detail.get("status");
            if(status==11){
                Scheduler sche = schedulerFactory.getScheduler();
                QuartzUtils.removeJob(sche,paramModelMap.get("orderNo").toString());
                cbhMessageMapper.deleteByOrderNo(paramModelMap.get("orderNo").toString());
            }
        }else{
            throw new CustomException("该订单已被作废，请勿重复操作");
        }
	}

    /**
     *  订单列表
     */
    public PageInfo<Map<String, Object>> weChatFindOrderList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> orderList = orderMapper.findMore(paramModelMap);
        for(Map<String,Object> m:orderList){
            m.put("accidentImg",m.get("accidentImg")==null?null:CommonField.getImgLists(1,0,String.valueOf(m.get("accidentImg"))));
            m.put("assertImg",m.get("assertImg")==null?null:CommonField.getImgLists(1,1,String.valueOf(m.get("assertImg"))));
            m.put("repairImg",m.get("repairImg")==null?null:CommonField.getImgLists(1,2,String.valueOf(m.get("repairImg"))));
            m.put("complaintImg",m.get("complaintImg")==null?null:CommonField.getImgLists(1,3,String.valueOf(m.get("complaintImg"))));
            m.put("nickname", m.get("nickname")==null?null:Base64.getFromBase64(String.valueOf(m.get("nickname"))));
        }
        PageInfo<Map<String, Object>> orderInfo = new PageInfo<>(orderList);
        return orderInfo;
    }

    /**
     * 公示列表
     * @param paramModelMap
     * @return
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findPublicityList(Map<String, Object> paramModelMap) throws Exception {
        paramModelMap.put("isInvalid", 1);
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> orderList = orderMapper.findPublicityList(paramModelMap);
        for(Map<String,Object> m:orderList){
            m.put("accidentImg",m.get("accidentImg")==null?null:CommonField.getImgLists(1,0,String.valueOf(m.get("accidentImg"))));
            m.put("assertImg",m.get("assertImg")==null?null:CommonField.getImgLists(1,1,String.valueOf(m.get("assertImg"))));
            m.put("repairImg",m.get("repairImg")==null?null:CommonField.getImgLists(1,2,String.valueOf(m.get("repairImg"))));
            m.put("complaintImg",m.get("complaintImg")==null?null:CommonField.getImgLists(1,3,String.valueOf(m.get("complaintImg"))));
            m.put("nickname", m.get("nickname")==null?null:Base64.getFromBase64(String.valueOf(m.get("nickname"))));

            String model = String.valueOf(m.get("model"));
            try{
                m.put("model", model.substring(0,model.length()-3)+"***");
            }catch (Exception e){
                e.printStackTrace();
            }
            m.put("portrait", String.valueOf(m.get("portrait")).indexOf("thirdwx.qlogo.cn")==-1?UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL,String.valueOf(m.get("portrait"))):String.valueOf(m.get("portrait")));
//            String licensePlateNumber = String.valueOf(m.get("licensePlateNumber"));
//            m.put("licensePlateNumber", carService.hideStr(licensePlateNumber));
            m.put("nameCarOwner", eventService.hideName(String.valueOf(m.get("nameCarOwner"))));
        }
        PageInfo<Map<String, Object>> orderInfo = new PageInfo<>(orderList);
        return orderInfo;
    }

    /**
     *  未处理订单
     */
    public List<Map<String, Object>> weChatFindUnfinishedOrder(Map<String, Object> paramModelMap) throws Exception {
        List<Map<String, Object>> unfinishedOrder = orderMapper.findUnfinishedOrder(paramModelMap);
        return unfinishedOrder;
    }

    /**
     *  订单详情
     */
    public Map<String,Object> weChatGetOrderDetail(Map<String,Object> paramModelMap) throws Exception {
        Map<String,Object> orderMap = orderMapper.findSingle(paramModelMap);
        orderMap.put("accidentImg",orderMap.get("accidentImg")==null?null:CommonField.getImgLists(1,0,String.valueOf(orderMap.get("accidentImg"))));
        orderMap.put("assertImg",orderMap.get("assertImg")==null?null:CommonField.getImgLists(1,1,String.valueOf(orderMap.get("assertImg"))));
        orderMap.put("repairImg",orderMap.get("repairImg")==null?null:CommonField.getImgLists(1,2,String.valueOf(orderMap.get("repairImg"))));
        orderMap.put("complaintImg",orderMap.get("complaintImg")==null?null:CommonField.getImgLists(1,3,String.valueOf(orderMap.get("complaintImg"))));
        orderMap.put("nickname", orderMap.get("nickname")==null?null:Base64.getFromBase64(String.valueOf(orderMap.get("nickname"))));
        orderMap.put("portrait", String.valueOf(orderMap.get("portrait")).indexOf("thirdwx.qlogo.cn")==-1?UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL,String.valueOf(orderMap.get("portrait"))):String.valueOf(orderMap.get("portrait")));
        orderMap.put("logo", orderMap.get("logo")==null?null:UploadFileUtil.getImgURL(CommonField.MAINTENANCESHOP_URL[0],String.valueOf(orderMap.get("logo"))));

        String carPhotos = String.valueOf(orderMap.get("carPhotos"));
        if(!carPhotos.equals("null")&&!carPhotos.equals("")){
            carPhotos = CommonField.getCarUrl(carPhotos);
            JSONObject cp= JSON.parseObject(carPhotos);
            if(String.valueOf(cp.get("qd")).equals("null")||String.valueOf(cp.get("qd")).equals("")){
                orderMap.put("carPhotos", cp.get("zh")+"_"+cp.get("yh")+"_"+cp.get("zq")+"_"+cp.get("yq"));
            }else{
                orderMap.put("carPhotos", cp.get("zh")+"_"+cp.get("yh")+"_"+cp.get("zq")+"_"+cp.get("yq")+"_"+cp.get("qd")+"_"+cp.get("zc")+"_"+cp.get("yc"));
            }
        }
        if(Constant.toEmpty(orderMap.get("drivingLicense"))){
            orderMap.put("drivingLicense",CommonField.getCarDrivingUrl((String) orderMap.get("drivingLicense")));
        }
        return orderMap;
    }

    @Transactional
    public String weChatCompensation(HttpServletRequest request,Map<String,String> tokenMap)  throws Exception {
        String result = "";
        String carId = request.getParameter("carId"); //车辆ID
        String oldOrderNo = request.getParameter("orderNo");
        System.out.println(oldOrderNo);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("carId", carId);
        List<Map<String,Object>> count = this.weChatFindUnfinishedOrder(map);
        if(oldOrderNo==null||"".equals(oldOrderNo)){
            if(count!=null&&count.size()>0){
                result = "该车辆在救助或保险理赔中"+"_"+count.get(0).get("orderNo");
                return result;
            }
        }

        Map<String,Object> card = carService.findCarById(Integer.valueOf(carId));
        if(!String.valueOf(card.get("timeSignout")).equals("1111-11-11 11:11:11.0")&&card.get("timeSignout")!=null){
            result = "该车已退出保障，无法申请理赔";
            return  result;
        }
        String[] base = request.getParameterValues("base");  //车损照片base64编码
        String description = request.getParameter("description"); //描述

        String orderNo = Constant.createEventNo()+new Random().nextInt(10);
        String cs = "";
/*				Map<String,Object> rmap1 = UploadFileUtil.WeChatBase64ToImg(org.apache.commons.lang3.ArrayUtils.toArray(base1),CommonField.COMPENSATE_IMG_URL);
				if(rmap1!=null&&rmap1.get("associatorimg")!=null){
					json.put("ct",rmap1.get("associatorimg"));
				}
				Map<String,Object> rmap2 = UploadFileUtil.WeChatBase64ToImg(org.apache.commons.lang3.ArrayUtils.toArray(base2),CommonField.COMPENSATE_IMG_URL);
				if(rmap2!=null&&rmap2.get("associatorimg")!=null){
					json.put("cw",rmap2.get("associatorimg"));
				}*/
        Map<String,Object> detail = new HashMap();
        System.out.println("---------------------------------orderNo"+oldOrderNo);
        if(oldOrderNo!=null&&!"".equals(oldOrderNo)){
            Map<String,Object> pMap = new HashMap<String, Object>();
            pMap.put("orderNo", oldOrderNo);
            detail = this.weChatGetOrderDetail(pMap);
        }
        String accidentImg = String.valueOf(detail.get("accidentImg"));
        for(String b:base){
            if(b.contains("jpg")&&accidentImg.contains(b)){
                cs = cs==""?b:cs+"_"+b;
            }else {
                Map<String, Object> rmap3 = carService.saveImg(b, CommonField.ORDER_URL[0]);
                if (rmap3 != null && rmap3.get("flag") != null) {
                    cs = cs == "" ? String.valueOf(rmap3.get("associatorimg")) : cs + "_" + rmap3.get("associatorimg");
                }
            }
        }
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = time.format(new Date());
        if(oldOrderNo!=null&&!"".equals(oldOrderNo)){
            map.put("customerId", tokenMap.get("id"));
            map.put("status", 4);
            map.put("uploadImgTime", date);
            map.put("orderNo", oldOrderNo);
            map.put("createAt", date);
            map.put("carId", carId);
            map.put("accidentDescription", description);
            map.put("accidentImg", cs);
           // map.put("applyTime", date);
            this.updateOrder(map);
            result = oldOrderNo;
        }else {
            map.put("customerId", tokenMap.get("id"));
            map.put("status", 4);
            map.put("uploadImgTime", date);
            map.put("orderNo", orderNo);
            map.put("createAt", date);
            map.put("carId", carId);
            map.put("accidentDescription", description);
            map.put("accidentImg", cs);
            //map.put("applyTime", date);
            map.put("createAt", date);
            this.saveOrder(map);
            result = orderNo;
        }
        return result;
    }
    @Transactional
    public String weChatCompensation1(HttpServletRequest request,Map<String,String> tokenMap)  throws Exception {
        String result = "";
        String carId = request.getParameter("carId"); //车辆ID
        String oldOrderNo = request.getParameter("orderNo");
        System.out.println(oldOrderNo);
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("carId", carId);
        List<Map<String,Object>> count = this.weChatFindUnfinishedOrder(map);
        if(oldOrderNo==null||"".equals(oldOrderNo)){
            if(count!=null&&count.size()>0){
                result = "该车辆在理赔或保险理赔中"+"_"+count.get(0).get("orderNo");
                return result;
            }
        }

        Map<String,Object> card = carService.findCarById(Integer.valueOf(carId));
        if(!String.valueOf(card.get("timeSignout")).equals("1111-11-11 11:11:11.0")&&card.get("timeSignout")!=null){
            result = "该车已退出保障，无法申请理赔";
            return  result;
        }
        String[] base = request.getParameterValues("base");  //车损照片base64编码
        String description = request.getParameter("description"); //描述

        String orderNo = Constant.createEventNo()+new Random().nextInt(10);
        String cs = "";
/*				Map<String,Object> rmap1 = UploadFileUtil.WeChatBase64ToImg(org.apache.commons.lang3.ArrayUtils.toArray(base1),CommonField.COMPENSATE_IMG_URL);
				if(rmap1!=null&&rmap1.get("associatorimg")!=null){
					json.put("ct",rmap1.get("associatorimg"));
				}
				Map<String,Object> rmap2 = UploadFileUtil.WeChatBase64ToImg(org.apache.commons.lang3.ArrayUtils.toArray(base2),CommonField.COMPENSATE_IMG_URL);
				if(rmap2!=null&&rmap2.get("associatorimg")!=null){
					json.put("cw",rmap2.get("associatorimg"));
				}*/
        Map<String,Object> detail = new HashMap();
        if(oldOrderNo!=null&&!"".equals(oldOrderNo)){
            Map<String,Object> pMap = new HashMap<String, Object>();
            pMap.put("orderNo", oldOrderNo);
            detail = this.weChatGetOrderDetail(pMap);
        }
        String accidentImg = String.valueOf(detail.get("accidentImg"));
        for(String b:base){
            if(b.contains("jpg")&&accidentImg.contains(b)){
                cs = cs==""?b:cs+"_"+b;
            }else {
                Map<String, Object> rmap3 = carService.saveImg(b, CommonField.ORDER_URL[0]);
                if (rmap3 != null && rmap3.get("flag") != null) {
                    cs = cs == "" ? String.valueOf(rmap3.get("associatorimg")) : cs + "_" + rmap3.get("associatorimg");
                }
            }
        }
        SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = time.format(new Date());
        if(oldOrderNo!=null&&!"".equals(oldOrderNo)){
            map.put("customerId", tokenMap.get("id"));
            map.put("status", 1);
            map.put("orderNo", oldOrderNo);
            map.put("createAt", date);
            map.put("carId", carId);
            map.put("accidentDescription", description);
            map.put("accidentImg", cs);
            map.put("applyTime", date);
            this.updateOrder(map);
        }else {
            map.put("customerId", tokenMap.get("id"));
            map.put("status", 1);
            map.put("orderNo", orderNo);
            map.put("createAt", date);
            map.put("carId", carId);
            map.put("accidentDescription", description);
            map.put("accidentImg", cs);
            map.put("applyTime", date);
            map.put("createAt", date);
            this.saveOrder(map);
        }
        return result;
    }
    @Transactional
    public int orderPayResult(String amountId) throws Exception{
        try {
            Map<String, Object> orderMap =  orderMapper.findOrderByRecordRechargeId(amountId);
            System.out.println("保险理赔支付回调进来了-----------------------------"+amountId);
            if(orderMap!=null&&"2".equals(String.valueOf(orderMap.get("rStatus")))){
                System.out.println("保险理赔支付回调进来了，状态为2-----------------------------"+amountId);
                String orderNo = String.valueOf(orderMap.get("orderNo"));

                SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = time.format(new Date());

                Map<String,Object> map = new HashMap<String, Object>();
                map.put("payTime", date);
                map.put("id", amountId);
                map.put("status", 1);
                map.put("description", "手机号为"+orderMap.get("customerPN")+"的会员理赔"+orderMap.get("model")+"支付了"+orderMap.get("amt")+"元");
                recordPaymentService.updateRecordPayment(map);
                System.out.println("理赔支付更改支付数据成功-----------------------------"+amountId);


                map.clear();
                map.put("orderNo", orderNo); //订单号
                map.put("payTime", date);
                map.put("takeCarTime", date);
                map.put("status", 61);
                orderMapper.updateModel(map);


                map.clear();
                map.put("id", orderMap.get("accountId"));
                Map<String,Object> acountMap = accountMapper.findSingle(map);

                String amtUnfreezeStr = Constant.toOr(String.valueOf(acountMap.get("amtUnfreeze")), Constant.toReadPro("orKey"), "decrypt");
                BigDecimal amtUnfreeze = new BigDecimal(amtUnfreezeStr);
                BigDecimal newAf = amtUnfreeze.add(new BigDecimal(String.valueOf(orderMap.get("amtBusiness"))));
                map.put("amtUnfreeze", Constant.toOr(String.valueOf(newAf), Constant.toReadPro("orKey"), "encrypt"));
                accountMapper.updateModel(map);

                Foundation foundation =  foundationMapper.findEntitySingle(new HashMap<String, Object>());
                Map<String,Object> upfMap = new HashMap<String, Object>();
                BigDecimal amtPay = new BigDecimal(String.valueOf(orderMap.get("amt")).equals("null")?"0":String.valueOf(orderMap.get("amt")));
                upfMap.put("amtTotal", foundation.getAmtTotal().add(amtPay));
                upfMap.put("amtBalance", foundation.getAmtBalance().add(amtPay));
                upfMap.put("showTotal", foundation.getShowTotal().add(amtPay));
                foundationMapper.updateModel(upfMap);

                map.clear();
                map.put("accountId", orderMap.get("accountId"));
                map.put("eventNo", orderNo);
                map.put("type", 131);
                map.put("amt", orderMap.get("amtBusiness"));
                map.put("img", "b_shouru.png");
                map.put("content", orderMap.get("licensePlateNumber")+"车辆修理完成,"+orderMap.get("shopName")+"到账:"+orderMap.get("amtBusiness")+"元");
                map.put("isRead", 1);
                cbhAccountDetailMapper.saveSingle(map);

                map.clear();
                map.put("customerPN",orderMap.get("customerPN"));
                map.put("licensePlateNumber",orderMap.get("licensePlateNumber"));
                map.put("amtOrder",orderMap.get("amt"));
                map.put("amtBusiness",orderMap.get("amtBusiness"));
                map.put("shopId",orderMap.get("shopId"));
                map.put("shopName",orderMap.get("shopName")==null?"车保汇":orderMap.get("shopName"));
                map.put("channelId",orderMap.get("shopId1"));
                map.put("channelName",orderMap.get("shopName1"));
                map.put("orderNo",orderMap.get("orderNo"));
                orderStatisticalMapper.saveSingle(map);
            }
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}

