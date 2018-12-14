package com.zccbh.demand.controller.finance;

import com.github.pagehelper.PageInfo;
import com.google.gson.JsonObject;
import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.demand.service.customer.RecordRechargeService;
import com.zccbh.demand.service.customer.RecordShareService;
import com.zccbh.demand.service.foundation.FoundationDetailService;
import com.zccbh.demand.service.foundation.FoundationService;
import com.zccbh.demand.service.marketing.MarketingService;
import com.zccbh.util.collect.Constant;
import net.sf.json.JSONObject;

import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/foundation")
public class FoundationController {
    @Autowired
    private FoundationService foundationService;

    @Autowired
    private FoundationDetailService foundationDetailService;

    @Autowired
    private RecordRechargeService recordRechargeService;

    @Autowired
    private RecordShareService recordShareService;
    
    @Autowired
    private MarketingService marketingService;

    /**
     * 互助金管理页面
     */
    @RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(ModelMap modelMap){
        return "finance/foundation";
    }

    /**
     * 商家费用页面
     */
    @RequestMapping(value = "/charge.html",method = RequestMethod.GET)
    public String eventPage(ModelMap modelMap){
        return "finance/event-complete";
    }

    /**
     * 查询互助总额数据
     */
    @RequestMapping(value = "/foundationData",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            return Constant.toReModel("0","SUCCESSFUL",foundationService.findFoundationData(paramModelMap));
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("4000","加载数据出错",e);
        }
    }
    /**
     * 查询充值统计数
     */
    @RequestMapping(value = "/rechargeDate", method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String rechargeDate(HttpServletRequest request, @RequestBody String strJson){
    	try {
			Map<String, Object> paramModelMap = JSONObject.fromObject(strJson);
			return Constant.toReModel("0", "SUCCESSFUL", recordRechargeService.findRechargeDate(paramModelMap));
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel("4000", "加载数据出错", e);
		}
    }

    /**
     * 查询用户充值数据
     */
    @RequestMapping(value = "/customerRecord",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String customerRecord(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> rechargeList = recordRechargeService.customerRecord(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",rechargeList);
        }catch (Exception e){
            return Constant.toReModel("4000","加载数据出错",e);
        }
    }

    /**
     * 查询支出数据
     */
    @RequestMapping(value = "/expensesRecord",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String expensesRecord(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> expensesList = recordShareService.findExpensesList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",expensesList);
        }catch (Exception e){
            return Constant.toReModel("4000","加载数据出错",e);
        }
    }

    /**
     * 查询互助金修改记录
     */
    @RequestMapping(value = "/detailList",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String detailList(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            return Constant.toReModel("0","SUCCESSFUL",foundationDetailService.findList(paramModelMap));
        }catch (Exception e){
            return Constant.toReModel("4000","加载数据出错",e);
        }
    }

    /**
     * 修改充值金额
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateAmt",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("互助金管理>修改充值总额")
    public String updateAmt(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            if(Constant.toEmpty(paramModelMap.get("category")) && Constant.toEmpty(paramModelMap.get("amt"))){
                //判断金额是否为数字
                try {
                    new BigDecimal((String) paramModelMap.get("amt"));
                }catch (Exception e){
                    return Constant.toReModel("4000","请输入正确格式的金额",null);
                }
                if(Constant.toEmpty(request.getSession().getAttribute("userInfo"))){
                    //获取当前登录用户信息
                    Map<String,Object> userInfo = (Map)request.getSession().getAttribute("userInfo");
                    //保存互助金修改操作
                    paramModelMap.put("practitioner",userInfo.get("adminUN"));
                    foundationDetailService.save(paramModelMap);
                    //修改互助金金额
                    foundationService.update(new BigDecimal((String) paramModelMap.get("amt")));
                    return Constant.toReModel("0","SUCCESSFUL",null);
                }else {
                    return Constant.toReModel("4000","请登录后再操作",null);
                }
            }
            return Constant.toReModel("4000","请填写所有选项",null);
        }catch (Exception e){
            return Constant.toReModel("4000","修改充值金额出错",e);
        }
    }
    
    /**
     * 修理厂结算
     */
    @RequestMapping(value = "/shopBill.html", method = RequestMethod.GET)
    public String shopBill() {
        return "finance/shop-bill";
    }
    
    /**
     * 维修厂财务数据
     *
     * @return
     */
    @RequestMapping(value = "/maintenanceshopBill", method=RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String maintenanceshopBill(HttpServletRequest request,@RequestBody String strJson) {
    	
		try {
			Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
	        PageInfo<Map<String, Object>> outDate = marketingService.maintenanceshopBill(paramModelMap);
			return Constant.toReModel("200", "OK", outDate);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel("4000","加载数据出错",e);
		}
        
    }
}
