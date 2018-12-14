package com.zccbh.demand.controller.business;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.service.business.UserBusinessService;
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
@RequestMapping("/business")
public class BusinessController {
    @Autowired
    private UserBusinessService businessService;

    /**
     * 商家用户管理页面
     */
    @RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(ModelMap modelMap){
        return "business/business-list";
    }

    /**
     * 查询商家用户数据
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> businessList = businessService.findBusinessList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",businessList);
        }catch (Exception e){
            return Constant.toReModel("4000","/business/list",e);
        }
    }

    /**
     * 添加商家用户
     */
    @RequestMapping(value = "/saveBusiness",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("商家管理>商家用户>添加用户")
    public String saveBusiness(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = businessService.saveBusiness(paramModelMap);
            switch (result){
                case "0":return Constant.toReModel(result,"SUCCESSFUL",null);
                case "4001":return Constant.toReModel(result,"输入的信息不合法",null);
                case "4003":return Constant.toReModel(result,"该手机号已被注册",null);
                case "4004":return Constant.toReModel(result,"该商家已有管理员",null);
            }
            return Constant.toReModel("4000","/business/saveBusiness",null);
        }catch (Exception e){
            return Constant.toReModel("4000","/business/saveBusiness",e);
        }
    }

    /**
     * 修改商家用户信息
     */
    @RequestMapping(value = "/updateBusiness",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("商家管理>商家用户>修改用户")
    public String updateBusiness(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = businessService.updateBusiness(paramModelMap);
            switch (result){
                case "0":return Constant.toReModel(result,"SUCCESSFUL",null);
                case "4001":return Constant.toReModel(result,"输入的信息不合法",null);
                case "4003":return Constant.toReModel(result,"该手机号已被注册",null);
                case "4004":return Constant.toReModel(result,"该商家已有管理员",null);
            }
            return Constant.toReModel("4000","/business/updateBusiness",null);
        }catch (Exception e){
            return Constant.toReModel("4000","/business/updateBusiness",e);
        }
    }

    /**
     * 删除商家用户
     */
    @RequestMapping(value = "/deleteBusiness",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("商家管理>商家用户>删除用户")
    public String deleteBusiness(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = businessService.deleteBusiness((Integer) paramModelMap.get("id"));
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","/business/deleteBusiness",e);
        }
    }
}
