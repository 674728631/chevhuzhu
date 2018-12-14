package com.zccbh.demand.controller.business;

import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.service.business.MaintenanceshopEmployeeService;
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
@RequestMapping("/employee")
public class MaintenanceshopEmployeeController {
    @Autowired
    private MaintenanceshopEmployeeService employeeService;

    /**
     * 维修人员详情页面
     */
    @RequestMapping(value = "/detail.html",method = RequestMethod.GET)
    public String employeeDetailPage(Integer employeeId,ModelMap modelMap){
        modelMap.put("employeeId", employeeId);
        return "business/employee-detail";
    }

    /**
     * 查询维修人员详情数据
     */
    @RequestMapping(value = "/detail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String employeeDetail(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> employeeDetail = employeeService.findEmployeeDetail(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",employeeDetail);
        }catch (Exception e){
            return Constant.toReModel("4000","/employee/detail",e);
        }
    }

    /**
     * 某一商家维修人员列表页面
     */
    @RequestMapping(value = "/info.html",method = RequestMethod.GET)
    public String infoPage(Integer maintenanceshopId,ModelMap modelMap){
        modelMap.put("maintenanceshopId", maintenanceshopId);
        return "business/employee-maintenanceshop";
    }

    /**
     * 添加维修人员
     */
    @RequestMapping(value = "/saveEmployee",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("商家管理>维修团队>添加员工")
    public String saveEmployee(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = employeeService.saveEmployee(paramModelMap);
            switch (result){
                case "0":return Constant.toReModel(result,"SUCCESSFUL",null);
            }
            return Constant.toReModel("4000","/employee/saveEmployee",null);
        }catch (Exception e){
            return Constant.toReModel("4000","添加员工失败",e);
        }
    }

    /**
     * 修改维修人员信息
     */
    @RequestMapping(value = "/updateEmployee",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("商家管理>维修团队>修改员工")
    public String updateEmployee(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = employeeService.updateEmployee(paramModelMap);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","修改员工失败",e);
        }
    }

    /**
     * 删除维修人员
     */
    @RequestMapping(value = "/deleteEmployee",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("商家管理>维修团队>删除员工")
    public String deleteEmployee(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = employeeService.deleteEmployee((Integer) paramModelMap.get("id"));
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","删除员工失败",e);
        }
    }
}
