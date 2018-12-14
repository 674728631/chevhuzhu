package com.zccbh.demand.controller.system;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.service.system.MenuService;
import com.zccbh.demand.service.system.RightsService;
import com.zccbh.demand.service.system.RoleService;
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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RightsService rightsService;

    /**
     * 权限管理页面
     */
    @RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(ModelMap modelMap){
        return "system/role-list";
    }

    /**
     * 查询角色数据
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> roleList = roleService.findRoleList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",roleList);
        }catch (Exception e){
            return Constant.toReModel("4000","/role/list",e);
        }
    }

    /**
     * 添加角色
     */
    @RequestMapping(value = "/saveRole",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("系统管理>权限管理>添加角色")
    public String saveRole(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = roleService.saveRole(paramModelMap);
            switch (result){
                case "0":return Constant.toReModel(result,"SUCCESSFUL",null);
            }
            return Constant.toReModel("4000","/role/saveRole",null);
        }catch (Exception e){
            return Constant.toReModel("4000","/role/saveRole",e);
        }
    }

    /**
     * 修改角色
     */
    @RequestMapping(value = "/updateRole",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("系统管理>权限管理>修改角色")
    public String updateRole(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = roleService.updateRole(paramModelMap);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","/role/updateRole",e);
        }
    }

    /**
     * 删除角色
     */
    @RequestMapping(value = "/deleteRole",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("系统管理>权限管理>删除角色")
    public String deleteRole(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = roleService.deleteRole((Integer) paramModelMap.get("id"));
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","/role/deleteRole",e);
        }
    }

    /**
     * 载入所有菜单权限
     */
    @RequestMapping(value = "/loadMenuData",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String loadMenuData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Integer roleId = Constant.toEmpty(paramModelMap.get("roleId"))?Integer.parseInt(paramModelMap.get("roleId").toString()):null;
            String userMenus = null;
            if (roleId != null){
                Map<String,Object> rights = rightsService.findRights(paramModelMap);
                if(rights != null){
                    userMenus = (String) rights.get("rightsMenu");
                }
            }
            Map<String,Object> reModelMap = new HashMap<String,Object>();
            reModelMap.put("allMenus",menuService.findMenuList(null));
            reModelMap.put("userMenus",userMenus);
            return Constant.toReModel("0","SUCCESSFUL",reModelMap);
        }catch (Exception e){
            return Constant.toReModel("4000","/role/loadMenuData",e);
        }
    }
}
