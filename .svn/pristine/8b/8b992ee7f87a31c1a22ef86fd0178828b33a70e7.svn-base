package com.zccbh.demand.controller.business;


import com.zccbh.demand.service.business.BusinessInsuranceClaimService;
import com.zccbh.test.exception.CustomException;
import com.zccbh.test.exception.Result;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.base.IpAdrressUtil;
import com.zccbh.util.base.IpUtils;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.ConstantNetwork;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * 保险理赔接口
 * @ClassName: BusinessInsuranceClaimController 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author xiaowuge
 * @date 2018年10月16日 下午3:15:53 
 *
 */
@RequestMapping("/merchants/business/insuranceClaim/")
@RestController
public class BusinessInsuranceClaimController {

    @Autowired
    private BusinessInsuranceClaimService insuranceClaimService;

    /**
     * @param request       点击订单明细
     */
    @RequestMapping(value = "orderDetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String orderDetail(HttpServletRequest request) {
        return getString("orderDetail",request);
    }
    /**
     * @param request       放弃接单
     */
    @RequestMapping(value = "abandonOrder",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String abandonOrder(HttpServletRequest request) {
        return getString("abandonOrder",request);
    }
    /**
     * @param request       立即接单
     */
    @RequestMapping(value = "takingOrder",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String takingOrder(HttpServletRequest request) {
        return getString("takingOrder",request);
    }
    /**
     * @param request       提交定损
     */
    @RequestMapping(value = "submitAssert",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String submitAssert(HttpServletRequest request) {
        return getString("submitAssert",request);
    }

    /**
     * @param request       开始维修
     */
    @RequestMapping(value = "startMaintenance",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String startMaintenance(HttpServletRequest request) {
        return getString("startMaintenance",request);
    }
    /**
     * @param request       提交维修
     */
    @RequestMapping(value = "submitMaintenance",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    public String submitMaintenance(HttpServletRequest request) {
        return getString("submitMaintenance",request);
    }

    //抽取公用的
    private String getString( String parameter,HttpServletRequest request){
        Class osSystem = null;
        try {
            Map<String,Object> resultMap=null;
            switch (parameter){
                case "orderDetail":
                resultMap = insuranceClaimService.orderDetail(request);
                break;
                case "abandonOrder":
                resultMap = insuranceClaimService.abandonOrder(request);
                break;
                case "takingOrder":
                resultMap = insuranceClaimService.takingOrder(request);
                break;
                case "submitAssert":
                resultMap = insuranceClaimService.submitAssert(request);
                break;
                case "startMaintenance":
                resultMap = insuranceClaimService.startMaintenance(request);
                break;
                case "submitMaintenance":
                resultMap = insuranceClaimService.submitMaintenance(request);
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
            return Constant.toReModel(CommonField.FAIL, e.getMessage(), CommonField.PARAMETER_ERROR_PROMPT);
        }
    }

    @GetMapping(value = "testexc",produces = "application/json;charset=utf-8")
    public String testException(int a,HttpServletRequest request) throws CustomException {
        String ipAdrress = IpAdrressUtil.getIpAdrress(request);
        System.out.println("ipAdrress = " + ipAdrress);
        String[] split = Constant.toReadPro("cheMaMaIP").split("_");
        System.out.println("split = " + Arrays.asList(split).contains("127.0.0.1"));
        System.out.println("Arrays.toString(split) = " + Arrays.toString(split));
        String s = ConstantNetwork.toClientIp(request);
        System.out.println("s = " + s);
        String realIp = IpUtils.getRealIp();
        System.err.println("realIp = " + realIp);
        /**
         * 获取访问者IP
         *
         * 在一般情况下使用Request.getRemoteAddr()即可，但是经过nginx等反向代理软件后，这个方法会失效。
         *
         * 本方法先从Header中获取X-Real-IP，如果不存在再从X-Forwarded-For获得第一个IP(用,分割)，
         * 如果还不存在则调用Request .getRemoteAddr()。
         *
         * @param request
         * @return
         */
            String ip = request.getHeader("X-Real-IP");
            if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
            ip = request.getHeader("X-Forwarded-For");
            if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                // 多次反向代理后会有多个IP值，第一个为真实IP。
                int index = ip.indexOf(',');
                if (index != -1) {
                    System.out.println("index = " + ip.substring(0, index));
                } else {
                    System.out.println("index = " + ip);
                }
            } else {
                System.out.println("index = " + request.getRemoteAddr());
            }
          return "123";
//        return Constant.toSuccessReModel(insuranceClaimService.testexc(a));
//        return new Result(insuranceClaimService.testexc(a));
    }
}
