package com.zccbh.demand.controller.system;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.service.user.UserAdminService;
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
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserAdminService adminService;

    /**
     * 用户管理页面
     */
    @RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(ModelMap modelMap){
        return "system/admin-list";
    }

    /**
     * 查询管理员用户数据
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> adminList = adminService.findAdminList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",adminList);
        }catch (Exception e){
            return Constant.toReModel("4000","/admin/list",e);
        }
    }

    /**
     * 添加管理员
     */
    @RequestMapping(value = "/saveAdmin",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("系统管理>用户管理>添加用户")
    public String saveAdmin(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = adminService.saveAdmin(paramModelMap);
            switch (result){
                case "0":return Constant.toReModel(result,"SUCCESSFUL",null);
                case "4001":return Constant.toReModel(result,"输入的信息不合法",null);
                case "4003":return Constant.toReModel(result,"该手机号或账号已被注册",null);
            }
            return Constant.toReModel("4000","/admin/saveAdmin",null);
        }catch (Exception e){
            return Constant.toReModel("4000","/admin/saveAdmin",e);
        }
    }

    /**
     * 修改管理员信息
     */
    @RequestMapping(value = "/updateAdmin",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("系统管理>用户管理>修改用户")
    public String updateAdmin(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = adminService.updateAdmin(paramModelMap);
            switch (result){
                case "0":return Constant.toReModel(result,"SUCCESSFUL",null);
                case "4001":return Constant.toReModel(result,"输入的信息不合法",null);
                case "4003":return Constant.toReModel(result,"该手机号或账号已被注册",null);
            }
            return Constant.toReModel("4000","/admin/updateAdmin",null);
        }catch (Exception e){
            return Constant.toReModel("4000","/admin/updateAdmin",e);
        }
    }

    /**
     * 删除管理员
     */
    @RequestMapping(value = "/deleteAdmin",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("系统管理>用户管理>删除用户")
    public String deleteAdmin(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = adminService.deleteAdmin((Integer) paramModelMap.get("id"));
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","/admin/deleteAdmin",e);
        }
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/updatePassword",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("系统管理>用户管理>修改密码")
    public String updatePassword(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> result = adminService.resetPassword(paramModelMap);
            return Constant.toReModel((String)result.get("code"),(String) result.get("message"),result);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("4000","/admin/updatePassword",e);
        }
    }
}
