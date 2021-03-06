package com.zccbh.demand.controller.order;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.controller.quartz.ObservationJob;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.controller.weChat.util.CoreService;
import com.zccbh.demand.service.basic.DictionaryService;
import com.zccbh.demand.service.customer.CarService;
import com.zccbh.demand.service.customer.MessageService;
import com.zccbh.demand.service.event.EventService;
import com.zccbh.demand.service.user.UsersService;
import com.zccbh.demand.service.weChat.WeiXinUtils;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.collect.Constant;
import net.sf.json.JSONObject;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/car")
public class CarController {
    @Autowired
    private CarService carService;
    @Autowired
    private EventService eventService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private WeiXinUtils weiXinUtils;

    @Autowired
    private SchedulerFactoryBean schedulerFactory;

    @Autowired
    private DictionaryService dictionaryService;
    
    @Autowired
    private UsersService usersService;
    
    private Logger logger = LoggerFactory.getLogger(CarController.class);

    /**
     * 订单/互助车辆 页面 按状态统计车辆数量
     */
    @RequestMapping(value = "/count",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String countData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> count = carService.findCarCount(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",count);
        }catch (Exception e){
            return Constant.toReModel("4000","加载车辆数量统计出错",e);
        }
    }

    /**
     * 车辆设置补贴页面
     */
    @RequestMapping(value = "/setting.html",method = RequestMethod.GET)
    public String settingPage(Integer flag,String status,String searchInfo,String beginTime,String endTime,ModelMap modelMap){
        modelMap.put("flag", flag);
        modelMap.put("status", status);
        modelMap.put("searchInfo", searchInfo);
        modelMap.put("beginTime", beginTime);
        modelMap.put("endTime", endTime);
        return "marketing/car-setting";
    }

    /**
     * 订单/互助车辆 页面
     */
    @RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(Integer flag,String status,String searchInfo,String beginTime,String endTime,ModelMap modelMap){
        modelMap.put("flag", flag); // 1.待支付 2.观察期 3.保障中 4.退出计划 5.全部 6.退出再加入
        modelMap.put("status", status); // 1-1 2-13 3-20 4-30 5- 6--1
        modelMap.put("searchInfo", searchInfo); // 搜索条件
        modelMap.put("beginTime", beginTime);
        modelMap.put("endTime", endTime);
        return "order/car-list";
    }

    /**
     * 订单/互助车辆 页面 查询车辆数据
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> carList = null;
            // 处理结束时间，多加一天，sql用的是between and
            if (Constant.toEmpty(paramModelMap.get("endTime"))){
                String endTime = LocalDate.parse((String)paramModelMap.get("endTime")).plusDays(1).toString();
                paramModelMap.put("endTime",endTime);
            }
            if("-1".equals(paramModelMap.get("status"))){//退出再加入
                carList = carService.findCarList3(paramModelMap);
            }else {
                // 将车牌号转化成大写
                String licensePlateNumber = Constant.toEmpty(paramModelMap.get("searchInfo"))? (String) paramModelMap.get("searchInfo"):"";
                paramModelMap.put("licensePlateNumber", licensePlateNumber.toUpperCase());
                carList = carService.findCarList2(paramModelMap);
            }
            return Constant.toReModel("0","SUCCESSFUL",carList);
        }catch (Exception e){
            return Constant.toReModel("4000","加载数据出错",e);
        }
    }

    /**
     * 车辆详情页面
     */
    @RequestMapping(value = "/carDetail.html",method = RequestMethod.GET)
    public String carDetailPage(Integer carId,Integer status,ModelMap modelMap){
        modelMap.put("carId", carId);
        modelMap.put("status", status);
        return "order/car-detail";
    }

    /**
     * 查询车辆详情数据
     */
    @RequestMapping(value = "/carDetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String carDetail(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> carDetail = carService.findCarDetail(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",carDetail);
        }catch (Exception e){
            return Constant.toReModel("4000","加载车辆详情出错",e);
        }
    }

    /**
     * 审核通过
     */
    @RequestMapping(value = "/success",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("互助车辆>审核通过")
    public String success(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> carInfo = carService.findCarById((Integer) paramModelMap.get("id"));
            //推送消息到C 端
            Map map = new HashMap();
            map.put("customerId",(Integer)carInfo.get("customerId"));
            map.put("licensePlateNumber",carInfo.get("licensePlateNumber"));
            map.put("type",31);
            map.put("title","车辆申请");
            map.put("content",carInfo.get("licensePlateNumber") + "的互助申请审核通过，请及时处理。");
            messageService.saveMessage(map);
            //修改车辆为进入观察期
            paramModelMap.put("status",13);
            String result = carService.updateCar(paramModelMap);
            //创建定时任务，指定时间后进入保障期
            map.clear();
            map.put("jobName","observationJob_" + paramModelMap.get("id"));
            map.put("carId",paramModelMap.get("id"));
            map.put("typeGuarantee", carInfo.get("typeGuarantee"));
            map.put("openid", carInfo.get("openId"));
            map.put("content", "爱车" + carInfo.get("licensePlateNumber") + "加入车V互助成功，您爱车正享受互助保障中。");
            map.put("keyword1", "审核通过");
            map.put("keyword2", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
            map.put("url", Constant.toReadPro("realURL")+"hfive/view/car.html");
            Scheduler sche = schedulerFactory.getScheduler();
            QuartzUtils.removeJob(sche,map.get("jobName").toString());
            //从数据库查询定时时间
            Map dicMap = new HashMap();
            dicMap.put("type","observationTime");
            Map dictionary = dictionaryService.findSingle(dicMap);
            //开启定时器
            Map<String, String> dateMap = DateUtils.getDateMap(new Long(dictionary.get("value").toString()));
            String cron = dateMap.get("second")+" "+dateMap.get("minute")+" "+dateMap.get("hour")+" "+ dateMap.get("day") +" "+ dateMap.get("month") +" ? *";
            QuartzUtils.addJob(sche,map.get("jobName").toString(), ObservationJob.class, map, cron);
            
            usersService.addAmtCompensationByInvitation(String.valueOf(carInfo.get("customerId")));
            eventService.updateDayNumber("observationNum",1);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","审核出错",e);
        }
    }

    /**
     * 审核不通过
     */
    @RequestMapping(value = "/fail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("互助车辆>审核不通过")
    public String fail(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            logger.info("carController审核不通过", paramModelMap);
            Map<String, Object> carInfo = carService.findCarById((Integer) paramModelMap.get("id"));
            //推送消息到C 端
            Map map = new HashMap();
            map.put("customerId",(Integer)carInfo.get("customerId"));
            map.put("licensePlateNumber",carInfo.get("licensePlateNumber"));
            map.put("type",31);
            map.put("title","车辆申请");
            map.put("content",carInfo.get("licensePlateNumber") + "的互助申请审核未通过，请及时处理。");
            messageService.saveMessage(map);
            //微信推送
            if (Constant.toEmpty(carInfo.get("openId"))) {
                map.clear();
                map.put("openid", carInfo.get("openId"));
                map.put("licensePlateNumber", carInfo.get("licensePlateNumber"));
                map.put("model", carInfo.get("model"));
                map.put("theme", "互助申请订单");
                map.put("keyword1", "加入互助社群");
                map.put("remark", paramModelMap.get("reasonFailure"));
                map.put("url", Constant.toReadPro("realURL")+"hfive/view/car_add.html?id=" + paramModelMap.get("id").toString());
                weiXinUtils.sendTemplate(9, map);
            }
            paramModelMap.put("status",12);
            String result = carService.updateCar(paramModelMap);
            eventService.updateDayNumber("notPassNum",1);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","审核出错",e);
        }
    }

    /**
     * 后台为车辆充值
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateAmt",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("互助车辆>修改互助余额")
    public String updateAmt(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            if(Constant.toEmpty(paramModelMap.get("addAmt")) && Constant.toEmpty(paramModelMap.get("type")) && Constant.toEmpty(paramModelMap.get("licensePlateNumber"))){
                //判断金额是否为数字
                try {
                    new BigDecimal((String) paramModelMap.get("addAmt"));
                }catch (Exception e){
                    return Constant.toReModel("4000","请输入合法的金额",null);
                }
                if(Constant.toEmpty(request.getSession().getAttribute("userInfo"))){
                    //获取当前登录用户信息
                    Map<String,Object> userInfo = (Map)request.getSession().getAttribute("userInfo");
                    //修改车辆余额
                    paramModelMap.put("practitioner",userInfo.get("adminUN"));
                    carService.updateAmt(paramModelMap);
                    return Constant.toReModel("0","SUCCESSFUL",null);
                }else {
                    return Constant.toReModel("4000","请登录后再操作",null);
                }
            }
            return Constant.toReModel("4000","请填写所有选项",null);
        }catch (Exception e){
            return Constant.toReModel("4000","修改金额出错",e);
        }
    }

    /**
     * 将车辆修改为不可用状态
     * @param request
     * @return
     */
    @RequestMapping(value = "/modifyCarUnavailable",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("营销>车辆补贴>将车辆修改为不可用")
    public String modifyCarUnavailable(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            if(!Constant.toEmpty(paramModelMap.get("licensePlateNumber"))){
                return Constant.toReModel("4000","车牌号不能为空",null);
            }
            carService.modifyCarUnavailable(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("4000","将车辆修改为不可用出现异常",e);
        }
    }
}
