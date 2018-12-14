package com.zccbh.demand.controller.function;

import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.InvitationCustomerMapper;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.customer.RecordShareMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import com.zccbh.demand.mapper.foundation.OrderStatisticalMapper;
import com.zccbh.demand.mapper.order.OrderMapper;
import com.zccbh.demand.pojo.model.ExcelFieldEntity;
import com.zccbh.demand.service.customer.RecordRechargeService;
import com.zccbh.demand.service.customer.RecordShareService;
import com.zccbh.util.base.Base64;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.export.ExcelExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping("/excel")
public class ExcelExportController {

    @Autowired
    private RecordRechargeService recordRechargeService;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private InvitationCustomerMapper invitationCustomerMapper;

    @Autowired
    private OrderStatisticalMapper orderStatisticalMapper;
    
    @Autowired
    private RecordShareMapper recordShareMapper;
    
    @Autowired
    private RecordRechargeMapper recordRechargeMapper;

    /**
     *  导出充值记录
     */
    @RequestMapping(value = "/recharge",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String operatingExcel(HttpServletRequest request, HttpServletResponse response, Integer choice,String beginTime,String endTime){
        try{
            Map<String,Object> paramModelMap = new HashMap<>();
            if(choice == 1 || choice == 2){
            	 if(choice == 1){
                 	paramModelMap.put("type", "1,2,3,10");
                 }else if(choice == 2 ){
                 	paramModelMap.put("type", "4");
                 }            
                 paramModelMap.put("beginTime",beginTime);
                 paramModelMap.put("endTime",endTime);
                 List<Map<String,Object>> rechargeList = recordRechargeMapper.findRechargeList(paramModelMap);
                 List<ExcelFieldEntity> rowList = new ArrayList<ExcelFieldEntity>();
                 for (Map<String,Object> rowMap:rechargeList) {
                     String shopName = Constant.toEmpty(rowMap.get("shopName")) ? rowMap.get("shopName").toString() : "";
                     String customerPN = Constant.toEmpty(rowMap.get("customerPN")) ? rowMap.get("customerPN").toString() : "";
                     String licensePlateNumber = Constant.toEmpty(rowMap.get("licensePlateNumber")) ? rowMap.get("licensePlateNumber").toString() : "";
                     Integer type = Constant.toEmpty(rowMap.get("type"))?Integer.parseInt(rowMap.get("type").toString()): -1;
                     String typeString = type==1?"微信":type==2?"支付宝":type==3?"活动赠送":type==4?"后台充值":type==10?"车妈妈充值":"";
                     Integer eventType = Constant.toEmpty(rowMap.get("eventType"))?Integer.parseInt(rowMap.get("eventType").toString()): -1;
                     String eventTypeDesc = eventType==1?"缴纳互助金":eventType==2?"充值":eventType==3?"支付救助":"";
                     double amt = Constant.toEmpty(rowMap.get("amt")) ? Double.parseDouble(rowMap.get("amt").toString()) : 0;
                     ExcelFieldEntity rowData = new ExcelFieldEntity(shopName,customerPN,licensePlateNumber,typeString,String.valueOf(amt),eventTypeDesc,rowMap.get("createAt").toString());
                     rowList.add(rowData);
                 }
                 response.setContentType("octets/stream");
                 response.addHeader("Content-Disposition", "attachment;filename=" + new String("充值记录.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
                 OutputStream out = response.getOutputStream();
                 String [] headers = new String[]{"渠道", "会员手机","车牌","营销活动","金额","充值类型","充值时间"};
                 ExcelExport.exportExcel("充值记录",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
                 return Constant.toReModel("0","SUCCESSFUL",null);
            }else{
            	paramModelMap.put("beginTime",beginTime);
                paramModelMap.put("endTime",endTime);
                List<Map<String, Object>> expensesList = recordShareMapper.findMore(paramModelMap);
                List<ExcelFieldEntity> rowList = new ArrayList<>();
                for(Map<String, Object> rowMap:expensesList){
                	String description = Constant.toEmpty(rowMap.get("description"))?rowMap.get("description").toString():"";
                	double amtCooperation = Constant.toEmpty(rowMap.get("amtCooperation"))?Double.parseDouble(rowMap.get("amtCooperation").toString()):0;
                	ExcelFieldEntity rowData = new ExcelFieldEntity(description,String.valueOf(amtCooperation),rowMap.get("createAt").toString());
                	rowList.add(rowData);                	
                }
                response.setContentType("octets/stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + new String("支出记录.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
                OutputStream out = response.getOutputStream();
                String [] headers = new String[]{"支出描述","支出金额","支出时间"};
                ExcelExport.exportExcel("支出记录",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
                return Constant.toReModel("0","SUCCESSFUL",null);
            }
           
        }catch (Exception e){
            return Constant.toReModel("4000","导出数据失败",e);
        }
    }

    /**
     *  导出互助车辆
     */
    @RequestMapping(value = "/car",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String carExcel(HttpServletRequest request, HttpServletResponse response,Integer status,String searchInfo,String beginTime,String endTime){
        try{
            Map<String,Object> paramModelMap = new HashMap<>();
            paramModelMap.put("status",status);
            paramModelMap.put("searchInfo",searchInfo);
            paramModelMap.put("beginTime",beginTime);
            paramModelMap.put("endTime",endTime);
            List<Map<String,Object>> carList = null;
            if(status != null && "-1".equals(status.toString())){
                carList = carMapper.findCarList3(paramModelMap);
            }else {
                carList = carMapper.findCarList2(paramModelMap);
            }
            List<ExcelFieldEntity> rowList = new ArrayList<ExcelFieldEntity>();
            for (Map<String,Object> rowMap:carList) {
                String nameCarOwner = Constant.toEmpty(rowMap.get("nameCarOwner")) ? rowMap.get("nameCarOwner").toString() : "";
                String customerPN = Constant.toEmpty(rowMap.get("customerPN")) ? rowMap.get("customerPN").toString() : "";
                String licensePlateNumber = Constant.toEmpty(rowMap.get("licensePlateNumber")) ? rowMap.get("licensePlateNumber").toString() : "";
                String shopName = Constant.toEmpty(rowMap.get("shopName"))?rowMap.get("shopName").toString(): "自然用户";
                double amtCooperation = Constant.toEmpty(rowMap.get("amtCooperation")) ? Double.parseDouble(rowMap.get("amtCooperation").toString()) : 0;
                Integer carStatus = Constant.toEmpty(rowMap.get("status"))?Integer.parseInt(rowMap.get("status").toString()): -1;
                String carStatusDesc = carStatus==1?"待支付":carStatus==2?"待添加照片":carStatus==10?"待审核":carStatus==12?"未通过":carStatus==13?"观察期":carStatus==20?"保障中":carStatus==30?"退出计划":"";
                String joinDay = "0天";
                if(rowMap.get("timeBegin") !=null){
                    Date timeBegin = (Date)rowMap.get("timeBegin");
                    if(carStatus==20){
                        joinDay = (new Date().getTime()-timeBegin.getTime())/(1000*60*60*24) + "天";
                    }
                    if(carStatus==30 && rowMap.get("timeSignout")!=null){
                        Date timeSignout = (Date)rowMap.get("timeSignout");
                        joinDay = (timeSignout.getTime()-timeBegin.getTime())/(1000*60*60*24) + "天";
                    }
                }
                ExcelFieldEntity rowData = new ExcelFieldEntity(nameCarOwner,customerPN,licensePlateNumber,shopName,String.valueOf(amtCooperation),joinDay,carStatusDesc);
                rowList.add(rowData);
            }
            response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("互助车辆.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
            OutputStream out = response.getOutputStream();
            String [] headers = new String[]{"车主姓名", "手机号","车牌号","渠道来源","互助金余额","加入时长","状态"};
            ExcelExport.exportExcel("互助车辆",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","导出数据失败",e);
        }
    }

    /**
     *  导出互助订单
     */
    @RequestMapping(value = "/event",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String eventExcel(HttpServletRequest request, HttpServletResponse response,Integer statusEvent,Integer isInvalid,String searchInfo){
        try{
            Map<String,Object> paramModelMap = new HashMap<>();
            paramModelMap.put("statusEvent",statusEvent);
            paramModelMap.put("isInvalid",isInvalid);
            paramModelMap.put("searchInfo",searchInfo);
            List<Map<String,Object>> eventList = eventMapper.findMore(paramModelMap);
            List<ExcelFieldEntity> rowList = new ArrayList<ExcelFieldEntity>();
            for (Map<String,Object> rowMap:eventList) {
                //车主姓名
                String nameCarOwner = Constant.toEmpty(rowMap.get("nameCarOwner")) ? rowMap.get("nameCarOwner").toString() : "";
                //手机号
                String customerPN = Constant.toEmpty(rowMap.get("customerPN")) ? rowMap.get("customerPN").toString() : "";
                //车牌号
                String licensePlateNumber = Constant.toEmpty(rowMap.get("licensePlateNumber")) ? rowMap.get("licensePlateNumber").toString() : "";
                //车型品牌
                String model = Constant.toEmpty(rowMap.get("model")) ? rowMap.get("model").toString() : "";
                //加入时间
                Integer carStatus = Constant.toEmpty(rowMap.get("carStatus"))?Integer.parseInt(rowMap.get("carStatus").toString()): -1;
                String joinDay = "0天";
                if(rowMap.get("timeBegin") !=null){
                    Date timeBegin = (Date)rowMap.get("timeBegin");
                    if(carStatus==20){
                        joinDay = (new Date().getTime()-timeBegin.getTime())/(1000*60*60*24) + "天";
                    }
                    if(carStatus==30 && rowMap.get("timeSignout")!=null){
                        Date timeSignout = (Date)rowMap.get("timeSignout");
                        joinDay = (timeSignout.getTime()-timeBegin.getTime())/(1000*60*60*24) + "天";
                    }
                }
                //互助金余额
                double carAmtCooperation = Constant.toEmpty(rowMap.get("carAmtCooperation")) ? Double.parseDouble(rowMap.get("carAmtCooperation").toString()) : 0;
                //理赔余额
                double amtCompensation = Constant.toEmpty(rowMap.get("amtCompensation")) ? Double.parseDouble(rowMap.get("amtCompensation").toString()) : 0;
                //渠道来源
                String sourceName = Constant.toEmpty(rowMap.get("sourceName"))?rowMap.get("sourceName").toString(): "自然用户";
                //维修厂
                String shopName = Constant.toEmpty(rowMap.get("shopName"))?rowMap.get("shopName").toString(): "";
                //状态
                Integer status = Constant.toEmpty(rowMap.get("statusEvent"))?Integer.parseInt(rowMap.get("statusEvent").toString()): -1;
                String statusEventDesc = status==1?"待审核":status==2?"未通过":status==3?"已通过":status==10?"待分单":status==11?"待接单"
                        :status==12?"放弃接单":status==21?"待定损":status==22?"定损待确认":status==31?"待接车":status==51?"待维修":status==52?"维修中"
                        :status==61?"待交车":status==71?"待评价":status==81?"投诉中":status==100?"已完成":"";
                //理赔时间
                Date createAt = (Date)rowMap.get("createAt");
                String createDay = (new Date().getTime()-createAt.getTime())/(1000*60*60*24) + "天";

                ExcelFieldEntity rowData = new ExcelFieldEntity(nameCarOwner,customerPN,licensePlateNumber,model,joinDay,String.valueOf(carAmtCooperation),String.valueOf(amtCompensation),sourceName,shopName,statusEventDesc,createDay);
                rowList.add(rowData);
            }
            response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("互助订单.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
            OutputStream out = response.getOutputStream();
            String [] headers = new String[]{"车主姓名", "手机号","车牌号","车型品牌","加入时间","互助金余额","救助余额","渠道来源","维修厂","状态","救助时间"};
            ExcelExport.exportExcel("互助订单",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","导出数据失败",e);
        }
    }

    /**
     *  导出保险理赔订单
     */
    @RequestMapping(value = "/order",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String orderExcel(HttpServletRequest request, HttpServletResponse response,Integer status,Integer isInvalid,String searchInfo){
        try{
            Map<String,Object> paramModelMap = new HashMap<>();
            paramModelMap.put("status",status);
            paramModelMap.put("isInvalid",isInvalid);
            paramModelMap.put("searchInfo",searchInfo);
            List<Map<String,Object>> orderList = orderMapper.findOrderList(paramModelMap);
            List<ExcelFieldEntity> rowList = new ArrayList<ExcelFieldEntity>();
            for (Map<String,Object> rowMap:orderList) {
                //车主姓名
                String nameCarOwner = Constant.toEmpty(rowMap.get("nameCarOwner")) ? rowMap.get("nameCarOwner").toString() : "";
                //手机号
                String customerPN = Constant.toEmpty(rowMap.get("customerPN")) ? rowMap.get("customerPN").toString() : "";
                //车牌号
                String licensePlateNumber = Constant.toEmpty(rowMap.get("licensePlateNumber")) ? rowMap.get("licensePlateNumber").toString() : "";
                //车型品牌
                String model = Constant.toEmpty(rowMap.get("model")) ? rowMap.get("model").toString() : "";
                //渠道来源
                String sourceName = Constant.toEmpty(rowMap.get("sourceName"))?rowMap.get("sourceName").toString(): "自然用户";
                //维修厂
                String shopName = Constant.toEmpty(rowMap.get("shopName"))?rowMap.get("shopName").toString(): "";
                //状态
                Integer orderStatus = Constant.toEmpty(rowMap.get("status"))?Integer.parseInt(rowMap.get("status").toString()): -1;
                String orderStatusDesc = orderStatus==1?"申请代办":orderStatus==2?"未通过":orderStatus==3?"已通过":orderStatus==10?"待分单":orderStatus==11?"待接单" :orderStatus==12?"放弃接单":
                        orderStatus==21?"待接车":orderStatus==31?"待定损":orderStatus==32?"待确认":orderStatus==41?"待维修":orderStatus==42?"维修中"
                        :orderStatus==51?"待交车":orderStatus==61?"待评价":orderStatus==71?"投诉中":orderStatus==100?"已完成":"";
                //救助时间
                Date applyTime = (Date)rowMap.get("applyTime");
                String applyDay = (new Date().getTime()-applyTime.getTime())/(1000*60*60*24) + "天";

                ExcelFieldEntity rowData = new ExcelFieldEntity(nameCarOwner,customerPN,licensePlateNumber,model,sourceName,shopName,orderStatusDesc,applyDay);
                rowList.add(rowData);
            }
            response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("保险代办.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
            OutputStream out = response.getOutputStream();
            String [] headers = new String[]{"车主姓名", "手机号","车牌号","车型品牌","渠道来源","维修厂","状态","救助时间"};
            ExcelExport.exportExcel("保险代办",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","导出数据失败",e);
        }
    }

    /**
     *  导出保险理赔订单收支数据
     */
    @RequestMapping(value = "/orderStatistical",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String orderStatisticalExcel(HttpServletRequest request, HttpServletResponse response,Integer status,Integer isInvalid,String searchInfo){
        try{
            Map<String,Object> paramModelMap = new HashMap<>();
            List<Map<String,Object>> dataList = orderStatisticalMapper.findOrderStatisticalList(paramModelMap);
            List<ExcelFieldEntity> rowList = new ArrayList<ExcelFieldEntity>();
            for (Map<String,Object> rowMap:dataList) {
                String customerPN = Constant.toEmpty(rowMap.get("customerPN")) ? rowMap.get("customerPN").toString() : "";
                String licensePlateNumber = Constant.toEmpty(rowMap.get("licensePlateNumber")) ? rowMap.get("licensePlateNumber").toString() : "";
                String channelName = Constant.toEmpty(rowMap.get("channelName"))?rowMap.get("channelName").toString(): "";
                String amtOrder = Constant.toEmpty(rowMap.get("amtOrder")) ? rowMap.get("amtOrder").toString() : "";
                String amtBusiness = Constant.toEmpty(rowMap.get("amtBusiness"))?rowMap.get("amtBusiness").toString(): "";
                String amtChannel = Constant.toEmpty(rowMap.get("amtChannel"))?rowMap.get("amtChannel").toString(): "";
                ExcelFieldEntity rowData = new ExcelFieldEntity(customerPN,licensePlateNumber,channelName,amtOrder,amtBusiness,amtChannel,rowMap.get("tradeTime").toString());
                rowList.add(rowData);
            }
            response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("理赔代办.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
            OutputStream out = response.getOutputStream();
            String [] headers = new String[]{"账户", "车牌","渠道","交易金额（元）","维修厂结算（元）","渠道结算（元）","车主交易时间"};
            ExcelExport.exportExcel("理赔代办",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","导出数据失败",e);
        }
    }

    @RequestMapping(value = "/doExcelInvitation1",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String doExcelInvitation1(HttpServletRequest request, HttpServletResponse response){
        try{
        	String customerPN = request.getParameter("customerPN");
            Map<String,Object> paramModelMap = new HashMap<>();
            paramModelMap.put("customerPN", customerPN);
            List<Map<String,Object>> list = invitationCustomerMapper.selectInvitationList1(paramModelMap);
            List<ExcelFieldEntity> rowList = new ArrayList<ExcelFieldEntity>();
            for (Map<String,Object> map:list) {
            	String portrait = String.valueOf(map.get("portrait"));
            	String nickname = Base64.getFromBase64((String) map.get("nickname"));
                ExcelFieldEntity rowData = new ExcelFieldEntity();
                rowData.setFieldOne(portrait);
                rowData.setFieldTwo(nickname);
                rowData.setFieldThree(String.valueOf(map.get("customerPN")));
                rowData.setFieldFour("0");
                rowData.setFieldfive(String.valueOf(map.get("count")));
                rowData.setFieldSix(String.valueOf(map.get("guancha")));
                rowData.setFieldSeven(String.valueOf(map.get("baozhang")));
                rowData.setFieldEight(String.valueOf(map.get("createAt")));
                rowList.add(rowData);
            }
            response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("拉新统计.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
            OutputStream out = response.getOutputStream();
            String [] headers = new String[]{"头像", "昵称","手机号","关注数","注册数","观察期车辆","保障中车辆","注册时间"};
            ExcelExport.exportExcel("拉新统计",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","导出数据失败",e);
        }
    }

    @RequestMapping(value = "/doExcelInvitation2",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String doExcelInvitation2(HttpServletRequest request, HttpServletResponse response){
        try{
        	String customerPN = request.getParameter("customerPN");
            Map<String,Object> paramModelMap = new HashMap<>();
            paramModelMap.put("customerPN", customerPN);
            paramModelMap.put("customerId", request.getParameter("customerId"));
            List<Map<String,Object>> list = invitationCustomerMapper.selectInvitationList2(paramModelMap);
            List<ExcelFieldEntity> rowList = new ArrayList<ExcelFieldEntity>();
            for (Map<String,Object> map:list) {
            	String portrait = String.valueOf(map.get("portrait"));
            	String nickname = Base64.getFromBase64((String) map.get("nickname"));
                ExcelFieldEntity rowData = new ExcelFieldEntity();
                rowData.setFieldOne(portrait);
                rowData.setFieldTwo(nickname);
                rowData.setFieldThree(String.valueOf(map.get("customerPN")));
                rowData.setFieldFour(String.valueOf(map.get("guancha")));
                rowData.setFieldfive(String.valueOf(map.get("baozhang")));
                rowData.setFieldSix(String.valueOf(map.get("amt")));
                rowData.setFieldSeven(String.valueOf(map.get("yue")));
                rowData.setFieldEight(String.valueOf(map.get("createAt")));
                rowList.add(rowData);
            }
            response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("拉新统计.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
            OutputStream out = response.getOutputStream();
            String [] headers = new String[]{"头像", "昵称","手机号","观察期车辆","保障中车辆","充值金额","余额","注册时间"};
            ExcelExport.exportExcel("拉新统计",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","导出数据失败",e);
        }
    }
}
