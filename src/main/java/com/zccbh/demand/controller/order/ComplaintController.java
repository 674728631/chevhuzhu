package com.zccbh.demand.controller.order;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.service.customer.ComplaintService;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/complaint")
public class ComplaintController {
    @Autowired
    private ComplaintService complaintService;

    /**
     * 加载投诉统计数据
     */
    @RequestMapping(value = "/count",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String countData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> count = complaintService.findComplaintCount(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",count);
        }catch (Exception e){
            return Constant.toReModel("4000","加载投诉统计出错",e);
        }
    }

    /**
     * 投诉列表页面
     */
    @RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(ModelMap modelMap){
        return "order/complaint-list";
    }

    /**
     * 查询投诉数据
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> maintenanceshopList = complaintService.findComplaint(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",maintenanceshopList);
        }catch (Exception e){
            return Constant.toReModel("4000","加载投诉数据失败",e);
        }
    }

    /**
     * 查询投诉详情数据
     */
    @RequestMapping(value = "/detail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String detailComplaint(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> detail = complaintService.findComplaintDetail(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",detail);
        }catch (Exception e){
            return Constant.toReModel("4000","查询详情失败",e);
        }
    }

    /**
     * 创建投诉订单
     */
    @RequestMapping(value = "/create",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("投诉订单>新建")
    public String createComplaint(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = complaintService.createComplaint(paramModelMap);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","新建失败！",e);
        }
    }

    /**
     * 处理投诉订单
     */
    @RequestMapping(value = "/success",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("投诉订单>处理")
    public String complaintSuccess(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("solveAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            paramModelMap.put("status",3);
            String result = complaintService.updateComplaint(paramModelMap);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","新建失败！",e);
        }
    }
}
