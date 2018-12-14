package com.zccbh.demand.controller.business;

import com.zccbh.demand.controller.quartz.CheckCarJob;
import com.zccbh.demand.controller.quartz.PayOrAddJob;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.service.business.CbhUserBusinessService;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.uploadImg.UploadFileUtil;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 互助理赔
 * @ClassName: CbhUserBusinessController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年10月16日 下午3:16:47 
 *
 */

@RequestMapping("/merchants/business/")
@Controller
public class CbhUserBusinessController {

    @Autowired
    private CbhUserBusinessService businessService;
    @Autowired
    private SchedulerFactoryBean schedulerFactory;
   /**
     * @param request  专门查询token是否在有效期内
     * @return
     */
    @RequestMapping(value = "validationToken",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String validationToken(HttpServletRequest request){
        return getString("validationToken",request);
    }

    /**
     * @param request       请求
     * @return  method = RequestMethod.POST
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "login",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String appLogin(HttpServletRequest request) {
        return getString("login",request);
    }

    /**
     * @param request       验证验证码是否正确
     * @return  method = RequestMethod.POST
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "phoneVerification",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String phoneVerification(HttpServletRequest request) {
        return getString("phoneVerification",request);
    }

    /**
     * @param request       修改密码
     */
    @RequestMapping(value = "updadePassWord",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String updadePassWord(HttpServletRequest request) {
        return getString("updadePassWord",request);
    }
    /**
     * @param request       订单列表
     */
    @RequestMapping(value = "orderList",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String orderList(HttpServletRequest request) {
        return getString("orderList",request);
    }
    /**
     * @param request       点击订单明细
     */
    @RequestMapping(value = "orderDetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String orderDetail(HttpServletRequest request) {
        return getString("orderDetail",request);
    }
    /**
     * @param request       放弃接单
     */
    @RequestMapping(value = "abandonOrder",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String abandonOrder(HttpServletRequest request) {
        return getString("abandonOrder",request);
    }
    /**
     * @param request       定损人员
     */
    @RequestMapping(value = "assertEmployeeList",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String assertEmployeeList(HttpServletRequest request) {
        return getString("assertEmployeeList",request);
    }
    /**
     * @param request       立即接单
     */
    @RequestMapping(value = "takingOrder",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String takingOrder(HttpServletRequest request) {
        return getString("takingOrder",request);
    }

    /**
     * @param request 图片上传 1 代表上传车主照片 2代表上传被保人照片 3用户头像  安卓IOS使用的
     * @return
     */
    @RequestMapping(value = "/uploadImg",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String uploadImg(HttpServletRequest request){
        return getString("uploadImg",request);
    }
    /**
     * @param request       提交定损
     */
    @RequestMapping(value = "submitAssert",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String submitAssert(HttpServletRequest request) {
        return getString("submitAssert",request);
    }
    /**
     * @param request       首页
     */
    @RequestMapping(value = "homePage",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String homePage(HttpServletRequest request) {
        return getString("homePage",request);
    }
    /**
     * @param request       修改为已读
     */
    @RequestMapping(value = "messageRead",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String messageRead(HttpServletRequest request) {
        return getString("messageRead",request);
    }
    /**
     * @param request       消息列表
     */
    @RequestMapping(value = "messageList",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String messageList(HttpServletRequest request) {
        return getString("messageList",request);
    }
    /**
     * @param request       扣分记录
     */
    @RequestMapping(value = "servicePointsRecord",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String servicePointsRecord(HttpServletRequest request) {
        return getString("servicePointsRecord",request);
    }
    /**
     * @param request       我的总额接口
     */
    @RequestMapping(value = "totalAmount",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String totalAmount(HttpServletRequest request) {
        return getString("totalAmount",request);
    }
    /**
     * @param request       开始维修
     */
    @RequestMapping(value = "startMaintenance",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String startMaintenance(HttpServletRequest request) {
        return getString("startMaintenance",request);
    }
    /**
     * @param request       提交维修
     */
    @RequestMapping(value = "submitMaintenance",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String submitMaintenance(HttpServletRequest request) {
        return getString("submitMaintenance",request);
    }
    /**
     * @param request       我的
     */
    @RequestMapping(value = "myDetails",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String myDetails(HttpServletRequest request) {
        return getString("myDetails",request);
    }
    /**
     * @param request       设置提现密码
     */
    @RequestMapping(value = "setWithdrawCashPassword",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String setWithdrawCashPassword(HttpServletRequest request) {
        return getString("setWithdrawCashPassword",request);
    }
    /**
     * @param request       绑定支付宝账户
     */
    @RequestMapping(value = "bindingAlipay",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String bindingAlipay(HttpServletRequest request) {
        return getString("bindingAlipay",request);
    }
    /**
     * @param request       验证提现密码是否正确
     */
    @RequestMapping(value = "verifyPassword",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String verifyPassword(HttpServletRequest request) {
        return getString("verifyPassword",request);
    }
    /**
     * @param request       账户明细
     */
    @RequestMapping(value = "transactionDetails",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String transactionDetails(HttpServletRequest request) {
        return getString("transactionDetails",request);
    }
    /**
     * @param request       店铺详情
     */
    @RequestMapping(value = "shopDetails",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String shopDetails(HttpServletRequest request) {
        return getString("shopDetails",request);
    }
    /**
     * @param request       编辑店铺
     */
    @RequestMapping(value = "updateShops",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String updateShops(HttpServletRequest request) {
        return getString("updateShops",request);
    }
    /**
     * @param request       添加或编辑员工
     */
    @RequestMapping(value = "addOrUpdateEmployee",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String addOrUpdateEmployee(HttpServletRequest request) {
        return getString("addOrUpdateEmployee",request);
    }
    /**
     * @param request       获得员工列表
     */
    @RequestMapping(value = "getEmployeeList",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getEmployeeList(HttpServletRequest request) {
        return getString("getEmployeeList",request);
    }
    /**
     * @param request       删除员工
     */
    @RequestMapping(value = "deleteEmployee",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String deleteEmployee(HttpServletRequest request) {
        return getString("deleteEmployee",request);
    }
    /**
     * @param request       意见反馈
     */
    @RequestMapping(value = "feedback",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String feedback(HttpServletRequest request) {
        return getString("feedback",request);
    }
    /**
     * @param request      支付宝提现
     */
    @RequestMapping(value = "cashWithdrawal",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String cashWithdrawal(HttpServletRequest request) {
        return getString("cashWithdrawal",request);
    }
    /**
     * @param request      查看加入互助名单
     */
    @RequestMapping(value = "helpEachOtherList",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String helpEachOtherList(HttpServletRequest request) {
        return getString("helpEachOtherList",request);
    }
    /**
     *      开启定时
     */
    @RequestMapping(value = "openQuartz",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String openQuartz() {
        try {
            Scheduler sche = schedulerFactory.getScheduler();
            String job_name = "openQuartZDSJDVJASVDJ";
            String cron = "0 0 10 * * ? *";
            Map<String, Object> params = new HashMap<>();
            params.put("jobName", job_name);
            QuartzUtils.addJob(sche, job_name, PayOrAddJob.class, params, cron);
            System.in.read();
            return "200";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "500";
    }
    /**s
     *      开启定时检查车辆保障期
     */
    @RequestMapping(value = "openCheckCarQuartz",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String openCarQuartz() {
        try {
            Scheduler sche = schedulerFactory.getScheduler();
            String job_name = "openCheckCarQuartz";
            String cron = "0 0 0/6 * * ? *";
            Map<String, Object> params = new HashMap<>();
            params.put("jobName", job_name);
            QuartzUtils.addJob(sche, job_name, CheckCarJob.class, params, cron);
            System.in.read();
            return "200";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "500";
    }

    //抽取公用的
    private String getString( String parameter,HttpServletRequest request){
        Class osSystem = null;
        try {
            Map<String,Object> resultMap=null;
            switch (parameter){
                case "validationToken":
                resultMap = businessService.validationToken(request);
                break;
                case "login":
                resultMap = businessService.login(request);
                break;
                case "phoneVerification":
                resultMap = businessService.phoneVerification(request);
                break;
                case "updadePassWord":
                resultMap = businessService.updadePassWord(request);
                break;
                case "orderList":
                resultMap = businessService.orderList(request);
                break;
                case "orderDetail":
                resultMap = businessService.orderDetail(request);
                break;
                case "abandonOrder":
                resultMap = businessService.abandonOrder(request);
                break;
                case "assertEmployeeList":
                resultMap = businessService.assertEmployeeList(request);
                break;
                case "takingOrder":
                resultMap = businessService.takingOrder(request);
                break;
                case "submitAssert":
                resultMap = businessService.submitAssert(request);
                break;
                case "homePage":
                resultMap = businessService.homePage(request);
                break;
                case "messageRead":
                resultMap = businessService.messageRead(request);
                break;
                case "messageList":
                resultMap = businessService.messageList(request);
                break;
                case "servicePointsRecord":
                resultMap = businessService.servicePointsRecord(request);
                break;
                case "totalAmount":
                resultMap = businessService.totalAmount(request);
                break;
                case "startMaintenance":
                resultMap = businessService.startMaintenance(request);
                break;
                case "submitMaintenance":
                resultMap = businessService.submitMaintenance(request);
                break;
                case "myDetails":
                resultMap = businessService.myDetails(request);
                break;
                case "setWithdrawCashPassword":
                resultMap = businessService.setWithdrawCashPassword(request);
                break;
                case "bindingAlipay":
                resultMap = businessService.bindingAlipay(request);
                break;
                case "verifyPassword":
                resultMap = businessService.verifyPassword(request);
                break;
                case "transactionDetails":
                resultMap = businessService.transactionDetails(request);
                break;
                case "shopDetails":
                resultMap = businessService.shopDetails(request);
                break;
                case "updateShops":
                resultMap = businessService.updateShops(request);
                break;
                case "addOrUpdateEmployee":
                resultMap = businessService.addOrUpdateEmployee(request);
                break;
                case "getEmployeeList":
                resultMap = businessService.getEmployeeList(request);
                break;
                case "deleteEmployee":
                resultMap = businessService.deleteEmployee(request);
                break;
                case "uploadImg":
                resultMap = UploadFileUtil.uploadFile(request);
                break;
                case "feedback":
                    resultMap = businessService.feedback(request);
                break;
                case "cashWithdrawal":
                    resultMap = businessService.cashWithdrawal(request);
                break;
                case "helpEachOtherList":
                    resultMap = businessService.helpEachOtherList(request);
                break;
                default:
            }
            if (Constant.toEmpty(resultMap.get(CommonField.VALIDATION_CODE))) {
                return Constant.toReModel(CommonField.TOKEN_FAILURE, "您的登录已失效,请重新登录", CommonField.PARAMETER_ERROR_PROMPT);
            }
            Boolean status = (Boolean) resultMap.get("status");
            String resultMessage = (String) resultMap.get("resultMessage");
            return status? Constant.toReModel(CommonField.SUCCESS, "", resultMap)
                    : Constant.toReModel(CommonField.FAIL, resultMessage,resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            // 要求不要返回具体的错误信息
            return Constant.toReModel(CommonField.FAIL, "系统出现异常", CommonField.PARAMETER_ERROR_PROMPT);
        }
    }
}
