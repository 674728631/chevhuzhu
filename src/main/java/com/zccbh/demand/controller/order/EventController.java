package com.zccbh.demand.controller.order;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.controller.quartz.CommentJob;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.controller.weChat.WeixinConstants;
import com.zccbh.demand.controller.weChat.util.CoreService;
import com.zccbh.demand.mapper.event.EventApplyMapper;
import com.zccbh.demand.mapper.event.EventReceivecarMapper;
import com.zccbh.demand.service.basic.DictionaryService;
import com.zccbh.demand.service.customer.MessageBackstageService;
import com.zccbh.demand.service.customer.MessageService;
import com.zccbh.demand.service.customer.UserCustomerService;
import com.zccbh.demand.service.event.*;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.*;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.qrcode.QRCodeUtil;
import com.zccbh.util.uploadImg.UploadFileUtil;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @Autowired
    private EventApplyService eventApplyService;

    @Autowired
    private EventReceiveService eventReceiveService;

    @Autowired
    private EventAssertService eventAssertService;

    @Autowired
    private EventRepairService eventRepairService;

    @Autowired
    private EventComplaintService eventComplaintService;

    @Autowired
    private EventCommentService eventCommentService;

    @Autowired
    EventReceivecarMapper eventReceivecarMapper;

    @Autowired
    private UserCustomerService customerService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private WeiXinUtils weiXinUtils;

    @Autowired
    private DistributionOrder distributionOrder;

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private MessageBackstageService messageBackstageService;

    @Autowired
    private EventApplyFailService eventApplyFailService;
    
    @Autowired
    private EventApplyMapper eventApplyMapper;
    
    private Logger logger = LoggerFactory.getLogger(EventController.class);

    /**
     * 加载理赔订单统计数据
     */
    @RequestMapping(value = "/count",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String countData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> count = eventService.findEventCount(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",count);
        }catch (Exception e){
            return Constant.toReModel("4000","加载订单统计出错",e);
        }
    }

    /**
     * 理赔列表页面
     */
	@RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(Integer flag,String isInvalid,String status,String searchInfo,ModelMap modelMap){
        modelMap.put("flag", flag);
        modelMap.put("isInvalid", isInvalid);
        modelMap.put("status", status);
        modelMap.put("searchInfo", searchInfo);
	    return "order/event-list";
    }


    /**
     * 加载理赔列表数据
     */
	@RequestMapping(value = "/list",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> eventList = eventService.findEventList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",eventList);
        }catch (Exception e){
            return Constant.toReModel("4000","/event/list",e);
        }
    }

    /**
     * 理赔订单详情页面
     */
    @RequestMapping(value = "/detail.html",method = RequestMethod.GET)
    public String detailPage(Integer statusEvent,String eventNo,ModelMap modelMap){
        modelMap.put("statusEvent", statusEvent);
        modelMap.put("eventNo", eventNo);
        return (statusEvent==1 || statusEvent==2 || statusEvent==3 || statusEvent==4)?"order/event-apply":
                (statusEvent==10 || statusEvent==11 || statusEvent==12 )?"order/event-distribution":
                (statusEvent==21 || statusEvent==22 || statusEvent==31 || statusEvent==51)?"order/event-assert":
                (statusEvent==52 || statusEvent==61 || statusEvent==71)?"order/event-repair":
                (statusEvent==81)?"order/event-complaint":
                (statusEvent==100)?"order/event-complete":"";
    }

    /**
     * 查询申请理赔详情数据
     */
    @RequestMapping(value = "/applydetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String eventApply(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> eventApply = eventApplyService.findEventApply(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",eventApply);
        }catch (Exception e){
            return Constant.toReModel("4000","加载数据失败",e);
        }
    }
    
    @RequestMapping(value = "/applyFailDetail",method = RequestMethod.POST,produces="application/json;charset=utf-8")
    @ResponseBody
    public String applyFailDetail(HttpServletRequest request, @RequestBody String strJson){
    	try {
			Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
			Map<String, Object> eventApplyFail = eventApplyService.getHistoryEventApply(paramModelMap);
			return Constant.toReModel("0", "SUCCESSFUL", eventApplyFail);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel("4000","加载数据失败",e);
		}
    	
    }

    /**
     * 申请理赔审核通过
     * 互助理赔
     */
    @RequestMapping(value = "/applysuccess",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("理赔订单>申请通过")
    public String applysuccess(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
          //获取当前订单状态
            int newStatus = eventService.getEventStatus(paramModelMap.get("eventNo").toString());
            if (newStatus != 1) {
            	return Constant.toReModel("4000","订单已被操作",null);
			}
            //修改理赔申请明细
            eventApplyService.updateEventApply(paramModelMap);
            //创建活动二维码
            String shareUrl = Constant.toReadPro("shareUrl") + "?id=" + paramModelMap.get("eventNo");
            String logoPath = request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeLogo.png";
            String qrcodeName = QRCodeUtil.saveQrcode(shareUrl,logoPath,"event/qrcode/");
            //修改互助单
            Map map = new HashMap();
            map.put("eventNo",paramModelMap.get("eventNo"));
            map.put("statusEvent",3);
            map.put("eventQrcode",qrcodeName);
            eventService.updateEvent(map);
            //推送消息给用户告知他审核通过
            map.clear();
            map.put("eventNo",paramModelMap.get("eventNo"));
            Map<String,Object> eventInfo = eventService.findEventByEventNo(map);
            map.clear();
            map.put("customerId",(Integer)eventInfo.get("customerId"));
            map.put("licensePlateNumber",eventInfo.get("licensePlateNumber"));
            map.put("eventNo",paramModelMap.get("eventNo"));
            map.put("type",41);
            map.put("title","救助消息");
            map.put("content",eventInfo.get("licensePlateNumber") + "的救助订单申请审核通过，请及时处理。");
            messageService.saveMessage(map);
            //微信推送
            if (Constant.toEmpty(eventInfo.get("openId"))) {
                map.clear();
                map.put("openid", eventInfo.get("openId"));
                map.put("eventNo", paramModelMap.get("eventNo"));
                map.put("licensePlateNumber",eventInfo.get("licensePlateNumber"));
                map.put("content", "爱车"+eventInfo.get("licensePlateNumber")+" "+eventInfo.get("model")+"申请救助审核通过");
                map.put("theme", "互助申请");
                map.put("keyword1", "审核通过");
                map.put("keyword2", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
                map.put("url", Constant.toReadPro("realURL")+"hfive/view/order_detail.html?id="+paramModelMap.get("eventNo"));
                weiXinUtils.sendTemplate(1, map);
            }
            //短信通知
            SmsDemo.sendSms(11,eventInfo.get("customerPN").toString(),eventInfo.get("licensePlateNumber").toString());
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("4000","审核出错",e);
        }
    }

    /**
     * 申请审核不通过
     */
    @RequestMapping(value = "/applyfail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("理赔订单>申请不通过")
    public String applyfail(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            
            //获取当前订单状态
          //获取当前订单状态
            int newStatus = eventService.getEventStatus(paramModelMap.get("eventNo").toString());
            if (newStatus != 1) {
            	return Constant.toReModel("4000","订单已被操作",null);
			}
            
            logger.info("eventController审核不通过", paramModelMap);
            //更新理赔申请的失败原因
            String result = eventApplyService.updateEventApply(paramModelMap);
            //更新互助事件的状态
            Map map = new HashMap();
            map.put("statusEvent",2);
            map.put("eventNo",paramModelMap.get("eventNo"));
            eventService.updateEvent(map);
            
            //删除App推送消息
            map.clear();
            map.put("orderNo", paramModelMap.get("eventNo"));
            map.put("orderStatus", 1);
            messageBackstageService.deleteBackMsg(map);
            
            //推送消息给用户告知他审核不通过
            map.clear();
            map.put("eventNo",paramModelMap.get("eventNo"));
            Map<String,Object> eventInfo = eventService.findEventByEventNo(map);
            map.clear();
            map.put("customerId",(Integer)eventInfo.get("customerId"));
            map.put("licensePlateNumber",eventInfo.get("licensePlateNumber"));
            map.put("eventNo",paramModelMap.get("eventNo"));
            map.put("type",41);
            map.put("title","救助消息");
            map.put("content",eventInfo.get("licensePlateNumber") + "的救助订单申请审核未通过，请及时处理。");
            messageService.saveMessage(map);
            
            //根据订单号获取申请理赔详情
            map.clear();
            map.put("eventNo", paramModelMap.get("eventNo"));
            Map<String, Object> parameMap = eventApplyMapper.getEventApply(map);
            eventApplyFailService.saveEventApplyFail(parameMap);
            
            //微信推送给用户
            if (Constant.toEmpty(eventInfo.get("openId"))) {
                map.clear();
                map.put("openid", eventInfo.get("openId"));
                map.put("licensePlateNumber",eventInfo.get("licensePlateNumber"));
                map.put("model",eventInfo.get("model"));
                map.put("theme", "互助申请订单");
                map.put("keyword1", "救助申请");
                map.put("remark", paramModelMap.get("reasonFailure"));
                map.put("url",Constant.toReadPro("realURL")+"hfive/view/order_detail.html?id=" + paramModelMap.get("eventNo").toString());
                weiXinUtils.sendTemplate(9, map);
            }
            //短信通知
            SmsDemo.sendSms(21,eventInfo.get("customerPN").toString(),eventInfo.get("licensePlateNumber").toString());
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
        	e.printStackTrace();
            return Constant.toReModel("4000","审核出错",e);
        }
    }

    /**
     * 查询定损详情数据
     */
    @RequestMapping(value = "/assertdetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String eventAssert(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> eventAssert = eventAssertService.findEventAssert(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",eventAssert);
        }catch (Exception e){
            return Constant.toReModel("4000","加载数据失败",e);
        }
    }

    /**
     * 定损审核通过
     */
    @RequestMapping(value = "/assertsuccess",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("理赔订单>定损通过")
    public String assertsuccess(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
          //获取当前订单状态
            int newStatus = eventService.getEventStatus(paramModelMap.get("eventNo").toString());
            if (newStatus != 22) {
            	return Constant.toReModel("4000","订单已被操作",null);
			}
            String result = eventAssertService.assertSuccess(paramModelMap);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","审核出错",e);
        }
    }

    /**
     * 查询分单详情数据
     */
    @RequestMapping(value = "/distributionDetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String findDistributionDetail(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> eventReceive = eventReceiveService.findDistributionDetail(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",eventReceive);
        }catch (Exception e){
            return Constant.toReModel("4000","加载数据失败",e);
        }
    }

    /**
     * 分单
     */
    @RequestMapping(value = "/distribution",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("互助理赔>手动分单")
    public String distribution(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            
            //获取当前订单状态
            int newStatus = eventService.getEventStatus(paramModelMap.get("eventNo").toString());
            if (newStatus != 10 && newStatus != 12) {
            	return Constant.toReModel("4000","订单已操作",null);
			}
            
            String result = distributionOrder.distributionOfEvent(paramModelMap);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","分单失败",e);
        }
    }

    /**
     * 查询维修详情数据
     */
    @RequestMapping(value = "/repairdetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String eventRepair(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> eventRepair = eventRepairService.findEventRepair(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",eventRepair);
        }catch (Exception e){
            return Constant.toReModel("4000","加载数据失败",e);
        }
    }

    /**
     * 查询投诉详情数据
     */
    @RequestMapping(value = "/complaintdetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String eventComplaint(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> eventComplaint = eventComplaintService.findEventComplaint(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",eventComplaint);
        }catch (Exception e){
            return Constant.toReModel("4000","加载投诉详情出错",e);
        }
    }

    /**
     * 处理投诉
     */
    @RequestMapping(value = "/complaintsuccess",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("理赔订单>处理投诉")
    public String complaintsuccess(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map map = new HashMap();
            //修改互助单
            map.put("eventNo",paramModelMap.get("eventNo"));
            map.put("statusEvent",71);
            eventService.updateEvent(map);
            //记录交车信息
            map.clear();
            map.put("eventNo", paramModelMap.get("eventNo")); //订单号
            map.put("status", 3);
            map.put("timeReceiveEnd", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            map.put("nameCarOwner", paramModelMap.get("nameCarOwner"));
            map.put("telCarOwner", paramModelMap.get("customerPN"));
            eventReceivecarMapper.saveSingle(map);
            //定时器默认好评
            map.clear();
            map.put("jobName","commentJob_" + paramModelMap.get("eventNo"));
            map.put("eventNo", paramModelMap.get("eventNo"));
            map.put("maintenanceshopId", paramModelMap.get("maintenanceshopId"));
            map.put("customerId", paramModelMap.get("customerId"));
            Scheduler sche = schedulerFactory.getScheduler();
            QuartzUtils.removeJob(sche,map.get("jobName").toString());
            Map dicMap = new HashMap();
            dicMap.put("type","commentTime");
            Map dictionary = dictionaryService.findSingle(dicMap);
            Map<String, String> dateMap = DateUtils.getDateMap(new Long(dictionary.get("value").toString()));
            String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
            QuartzUtils.addJob(sche,map.get("jobName").toString(), CommentJob.class, map, cron);
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","处理投诉出错",e);
        }
    }

    /**
     * 废弃订单
     */
    @RequestMapping(value = "/invalidOrder",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("互助理赔>废弃订单")
    public String invalidOrder(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            eventService.invalidOrder(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","废弃订单出错",e);
        }
    }

    /**
     * 完成详情数据
     */
    @RequestMapping(value = "/completedetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String eventComplete(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> eventComment = eventCommentService.findEventComment(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",eventComment);
        }catch (Exception e){
            return Constant.toReModel("4000","/comment/detail",e);
        }
    }

    /**
     * 查询商家报价时的互助订单详情数据
     */
    @RequestMapping(value = "/quotation",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String quotation(HttpServletRequest request){
        try{
            String type = request.getParameter("type");
            Map<String, Object> result = eventService.findEventQuotation(request.getParameter("eventNo"),type);
            if(Constant.toEmpty(result)){
                return Constant.toReModel(CommonField.SUCCESS,"SUCCESSFUL",result);
            }else {
                return Constant.toReModel(CommonField.FAIL,"您查询的订单号不存在",null);
            }
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel(CommonField.FAIL,CommonField.SERVER_FAILURE,null);
        }
    }

    /**
     * 发起理赔页面
     */
    @RequestMapping(value = "/create.html",method = RequestMethod.GET)
    public String createPage(ModelMap modelMap){
        return "business/create-event";
    }

    /**
     * 创建互助事件单
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("理赔订单>发起理赔")
    public String saveBusiness(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            //根据会员手机和车牌号查询对应会员及车辆
            Map map = new HashMap();
            map.put("customerPN",paramModelMap.get("customerPN"));
            map.put("licensePlateNumber",paramModelMap.get("licensePlateNumber"));
            Map customerAndCar = customerService.findCustomerAndCar(map);
            //如果信息真实，进行创建互助事件操作
            if(Constant.toEmpty(customerAndCar.get("customerId")) && Constant.toEmpty(customerAndCar.get("carId"))){
                //判断该车辆是否已经发起理赔，同一辆车不能重复发起理赔
                map.clear();
                map.put("carId", customerAndCar.get("carId"));
                int count = eventService.findApplyCount(map);
                if(count>0){
                    return Constant.toReModel("4003", "该车辆已在救助中，不要重复发起救助", null);
                }
                //保存互助事件基本信息
                map.clear();
                String eventNo = Constant.createEventNo();
                map.put("eventNo",eventNo);
                map.put("statusEvent",3);
                map.put("customerId",customerAndCar.get("customerId"));
                map.put("carId",customerAndCar.get("carId"));
                eventService.saveEvent(map);
                //保存互助事件申请信息
                map.clear();
                map.put("eventNo",eventNo);
                map.put("accidentDescription",paramModelMap.get("accidentDescription"));
                map.put("accidentImg",paramModelMap.get("accidentImg"));
                map.put("timeExamine", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                eventService.saveApply(map);
                map.clear();
                map.put("eventNo",eventNo);
                map.put("statusEvent",3);
                return Constant.toReModel("0","SUCCESSFUL",map);
            }
            return Constant.toReModel("4000","发起救助失败！原因：车辆信息错误或车辆不处于保障中",null);
        }catch (Exception e){
            return Constant.toReModel("4000","发起救助失败！",e);
        }
    }

    @RequestMapping(value = "/uploadImg",method = RequestMethod.POST,produces="text/html;charset=UTF-8")
    @ResponseBody
    public String createEvent( @RequestParam("file") MultipartFile[] files){
        StringBuffer sb = new StringBuffer();
        for (int i = 0;i<files.length;i++){
            //图片新名字
            String newFileName = null;
            String uuid = UUID.randomUUID().toString().replace("-", "");
            newFileName = uuid + ".jpg";
            try {
                UploadFileUtil.saveImg(CommonField.COMPENSATE_IMG_URL,newFileName,files[i].getBytes());
                if(i == (files.length-1)){
                    sb.append(newFileName);
                }else {
                    sb.append(newFileName);
                    sb.append("_");
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return Constant.toReModel("0","SUCCESSFUL",sb.toString());
    }
}
