package com.zccbh.demand.controller.member;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.service.customer.UserCustomerService;
import com.zccbh.util.collect.Constant;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private UserCustomerService customerService;

    /**
     * 加载用户统计数据
     */
    @RequestMapping(value = "/count",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String countData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> count = customerService.findCustomerCount(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",count);
        }catch (Exception e){
            return Constant.toReModel("4000","加载用户统计出错",e);
        }
    }

    /**
     * 用户名单页面
     */
    @RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(ModelMap modelMap){
        return "member/customer-list";
    }

    /**
     * 查询所有用户数据
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> customerList = customerService.findCustomerList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",customerList);
        }catch (Exception e){
            return Constant.toReModel("4000","/customer/list",e);
        }
    }

    /**
     * 查询用户详情数据
     */
    @RequestMapping(value = "/detail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String detailCustomer(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> detail = customerService.findCustomerDetail(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",detail);
        }catch (Exception e){
            return Constant.toReModel("4000","查询详情失败",e);
        }
    }

    /**
     * 冻结用户
     */
    @RequestMapping(value = "/freeze",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("用户名单>冻结用户")
    public String freeze(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("status",2);
            customerService.updateModel(paramModelMap);
            return Constant.toReModel("0","冻结成功",null);
        }catch (Exception e){
            return Constant.toReModel("4000","冻结失败",e);
        }
    }

    /**
     * 解冻用户
     */
    @RequestMapping(value = "/unfreeze",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("用户名单>解冻用户")
    public String unfreeze(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("status",1);
            customerService.updateModel(paramModelMap);
            return Constant.toReModel("0","解冻成功",null);
        }catch (Exception e){
            return Constant.toReModel("4000","解冻失败",e);
        }
    }

    /**
     * 添加会员
     */
    @RequestMapping(value = "/saveCustomer",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("会员管理>会员列表>添加会员")
    public String saveCustomer(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = customerService.saveCustomer(paramModelMap);
            switch (result){
                case "0":return Constant.toReModel(result,"SUCCESSFUL",null);
                case "4001":return Constant.toReModel(result,"输入的信息不合法",null);
                case "4003":return Constant.toReModel(result,"该手机号已被注册",null);
            }
            return Constant.toReModel("4000","添加失败",null);
        }catch (Exception e){
            return Constant.toReModel("4000","添加失败",e);
        }
    }

    /**
     * 关注了维修厂的会员页面
     */
    @RequestMapping(value = "/shopCustomer.html",method = RequestMethod.GET)
    public String shopCustomerPage(Integer maintenanceshopId,ModelMap modelMap){
        modelMap.put("maintenanceshopId", maintenanceshopId);
        return "business/shopCustomer-list";
    }

    /**
     * 查询关注了维修厂的会员数据
     */
    @RequestMapping(value = "/shopCustomer",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String shopCustomerData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> carList = customerService.findShopCustomer(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",carList);
        }catch (Exception e){
            return Constant.toReModel("4000","加载数据出错",e);
        }
    }
}
