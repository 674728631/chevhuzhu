package com.zccbh.demand.controller.appBackstage;

import com.zccbh.demand.service.appBackstage.AppBackstageService;
import com.zccbh.demand.service.system.MenuService;
import com.zccbh.demand.service.weChat.BaseInterceptor;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: luoyuangang
 * @JDK version used:       	1.8
 * @Modified By:            	<修改人中文名或拼音缩写>
 * @Modified Date:          	<修改日期，格式:YYYY年MM月DD日>
 * @Why & What is modified: 	<修改原因描述>
 * @create 2018-08-06 10:31
 **/
@RestController
@RequestMapping("/appBackstage/")
public class AppBackstageController {
    private static final Logger LOGGE = LoggerFactory.getLogger(BaseInterceptor.class);

    @Autowired
    private AppBackstageService backstageService;

    /**
     *      登录
     */
    @PostMapping(value = "appLogin", produces = "application/json;charset=utf-8")
    public String appLogin()throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.appLogin());
    }
    /**
     * @param request       专门查询token是否在有效期内
     */
    @PostMapping(value = "validationToken",produces = "application/json;charset=utf-8")
    public String validationToken(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.validationToken());
    }
    /**
     * @param request       验证验证码是否正确
     */
    @PostMapping(value = "phoneVerification",produces = "application/json;charset=utf-8")
    public String phoneVerification(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.phoneVerification());
    }
    /**
     * @param request       修改密码
     */
    @PostMapping(value = "updadePassWord",produces = "application/json;charset=utf-8")
    public String updadePassWord(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.updadePassWord());

    }
    /**
     * @param request       修改头像
     */
    @PostMapping(value = "updatePortrait",produces = "application/json;charset=utf-8")
    public String updatePortrait(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.updatePortrait());
    }
    /**
     * @param request       首页
     */
    @PostMapping(value = "homePage",produces = "application/json;charset=utf-8")
    public String homePage(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.homePage());
    }
    /**
     * 首页菜单
     */
    @PostMapping(value = "homeMenu", produces = "application/json;charset=utf-8")
    public String homeMenu(HttpServletRequest request)throws Exception{
    	return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.homeMenu());
    }
    /**
     * @param request       消息列表
     */
    @PostMapping(value = "messageList",produces = "application/json;charset=utf-8")
    public String messageList(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.messageList());
    }
    /**
     * @param request       消息已读接口
     */
    @PostMapping(value = "messageRead",produces = "application/json;charset=utf-8")
    public String messageRead(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.messageRead());
    }
    /**
     * @param request       统计各状态车辆数量
     */
    @PostMapping(value = "countCar",produces = "application/json;charset=utf-8")
    public String countCar(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.countCar());
    }
    /**
     * @param request       车辆列表
     */
    @PostMapping(value = "listCar",produces = "application/json;charset=utf-8")
    public String listCar(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.listCar());
    }
    /**
     * @param request       车辆详情
     */
    @PostMapping(value = "detailCar",produces = "application/json;charset=utf-8")
    public String detailCar(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.detailCar());
    }
    /**
     * @param request       统计各状态保险理赔数量
     */
    @PostMapping(value = "countOrder",produces = "application/json;charset=utf-8")
    public String countOrder(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.countOrder());
    }
    /**
     * @param request       保险理赔列表
     */
    @PostMapping(value = "listOrder",produces = "application/json;charset=utf-8")
    public String listOrder(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.listOrder());
    }
    /**
     * @param request       保险理赔详情
     */
    @PostMapping(value = "detailOrder",produces = "application/json;charset=utf-8")
    public String detailOrder(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.detailOrder());
    }
    /**
     * @param request       作废保险理赔订单
     */
    @PostMapping(value = "invalidOrder",produces = "application/json;charset=utf-8")
    public String invalidOrder(HttpServletRequest request)throws Exception {
        backstageService.invalidOrder();
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, null);
    }
    /**
     * @param request       保险理赔申请通过
     */
    @PostMapping(value = "successOrder",produces = "application/json;charset=utf-8")
    public String successOrder(HttpServletRequest request)throws Exception {
        backstageService.successOrder();
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, null);
    }
    /**
     * @param request       保险理赔申请不通过
     */
    @PostMapping(value = "failOrder",produces = "application/json;charset=utf-8")
    public String failOrder(HttpServletRequest request)throws Exception {
        backstageService.failOrder();
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, null);
    }
    /**
     * @param request       保险理赔分单
     */
    @PostMapping(value = "distributionOrder",produces = "application/json;charset=utf-8")
    public String distributionOrder(HttpServletRequest request)throws Exception {
        backstageService.distributionOrder();
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, null);
    }
    /**
     * @param request       保险理赔确认定损
     */
    @PostMapping(value = "assertOrder",produces = "application/json;charset=utf-8")
    public String assertOrder(HttpServletRequest request)throws Exception {
        backstageService.assertOrder();
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, null);
    }
    /**
     * @param request       保险理赔处理投诉
     */
    @PostMapping(value = "complaintOrder",produces = "application/json;charset=utf-8")
    public String complaintOrder(HttpServletRequest request)throws Exception {
        backstageService.complaintOrder();
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, null);
    }
    /**
     * 互助理赔 统计各种状态订单的数据
     * @param
     */
    @PostMapping(value = "hzCount",produces = "application/json;charset=utf-8")
    public String hzCount(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.hzCount());
    }
    /**
     * 互助理赔 订单的列表
     * @param
     */
    @PostMapping(value = "hzOrderList",produces = "application/json;charset=utf-8")
    public String hzOrderList(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.hzOrderList());
    }
    /**
     * 互助理赔 订单详情
     * @param
     */
    @PostMapping(value = "hzOrderDetail",produces = "application/json;charset=utf-8")
    public String hzOrderDetail(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.hzOrderDetail());
    }
    /**
     * 互助理赔 作废订单
     * @param
     */
    @PostMapping(value = "hzInvalidOrder",produces = "application/json;charset=utf-8")
    public String hzInvalidOrder(HttpServletRequest request)throws Exception {
        backstageService.hzInvalidOrder();
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS,null );
    }
    /**
     * 互助理赔 审核通过
     * @param
     */
    @PostMapping(value = "hzApplyPass",produces = "application/json;charset=utf-8")
    public String hzApplyPass(HttpServletRequest request)throws Exception {
        backstageService.hzApplyPass(request);
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS,null );
    }
    /**
     * 互助理赔 审核不通过
     * @param
     */
    @PostMapping(value = "hzApplyFail",produces = "application/json;charset=utf-8")
    public String hzApplyFail(HttpServletRequest request)throws Exception {
        backstageService.hzApplyFail(request);
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS,null );
    }
    /**
     * 互助理赔 分单
     * @param
     */
    @PostMapping(value = "hzDistribution",produces = "application/json;charset=utf-8")
    public String hzDistribution(HttpServletRequest request)throws Exception {
        backstageService.hzDistribution(request);
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS,null );
    }
    /**
     * 互助理赔 定损确认
     * @param
     */
    @PostMapping(value = "hzAssertOrder",produces = "application/json;charset=utf-8")
    public String hzAssertOrder(HttpServletRequest request)throws Exception {
        backstageService.hzAssertOrder(request);
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS,null );
    }
    /**
     * 互助理赔 投诉解决
     * @param
     */
    @PostMapping(value = "hzSolveComplaint",produces = "application/json;charset=utf-8")
    public String hzSolveComplaint(HttpServletRequest request)throws Exception {
        backstageService.hzSolveComplaint(request);
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS,null );
    }
    /**
     * @param request       添加模板
     */
    @PostMapping(value = "addModel",produces = "application/json;charset=utf-8")
    public String addModel(HttpServletRequest request)throws Exception {
        backstageService.addModel();
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, null);
    }
    /**
     * @param request       删除模板
     */
    @PostMapping(value = "deleteModel",produces = "application/json;charset=utf-8")
    public String deleteModel(HttpServletRequest request)throws Exception {
        backstageService.deleteModel();
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, null);
    }
    /**
     * @param request       模板列表
     */
    @PostMapping(value = "listModel",produces = "application/json;charset=utf-8")
    public String listModel(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.listModel());
    }
    /**
     * @param request       查询可接单的商家
     */
    @PostMapping(value = "canDistributionShop",produces = "application/json;charset=utf-8")
    public String canDistributionShop(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.canDistributionShop());
    }
    /**
     * @param request       统计各状态投诉订单数量
     */
    @PostMapping(value = "countComplaint",produces = "application/json;charset=utf-8")
    public String countComplaint(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.countComplaint());
    }
    /**
     * @param request       投诉订单列表
     */
    @PostMapping(value = "listComplaint",produces = "application/json;charset=utf-8")
    public String listComplaint(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.listComplaint());
    }
    /**
     * @param request       投诉订单详情
     */
    @PostMapping(value = "detailComplaint",produces = "application/json;charset=utf-8")
    public String detailComplaint(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.detailComplaint());
    }
    /**
     * @param request       处理投诉订单
     */
    @PostMapping(value = "solveComplaint",produces = "application/json;charset=utf-8")
    public String solveComplaint(HttpServletRequest request)throws Exception {
        backstageService.solveComplaint();
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, null);
    }

    /**
     * @param request       创建投诉订单
     */
    @PostMapping(value = "createComplaint",produces = "application/json;charset=utf-8")
    public String createComplaint(HttpServletRequest request)throws Exception {
        backstageService.createComplaint();
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, null);
    }

    /**
     * @param request       财务数据
     */
    @PostMapping(value = "financialDataStatistics",produces = "application/json;charset=utf-8")
    public String financialDataStatistics(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.financialDataStatistics());
    }

    /**
     * @param request       运营数据
     */
    @PostMapping(value = "operateDate",produces = "application/json;charset=utf-8")
    public String operateDate(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.operateDate());
    }

    /**
     * @param request       渠道拉新数据
     */
    @PostMapping(value = "channelData",produces = "application/json;charset=utf-8")
    public String channelData(HttpServletRequest request)throws Exception {
        return Constant.toReModel(CommonField.SUCCESS, CommonField.STRING_SUCCESS, backstageService.channelData());
    }
}
