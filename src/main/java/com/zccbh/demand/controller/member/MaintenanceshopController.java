package com.zccbh.demand.controller.member;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.service.business.MaintenanceshopEmployeeService;
import com.zccbh.demand.service.business.MaintenanceshopService;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/maintenanceshop")
public class MaintenanceshopController {
    @Autowired
    private MaintenanceshopService maintenanceshopService;

    @Autowired
    private MaintenanceshopEmployeeService employeeService;

    /**
     * 加载商家统计数据
     */
    @RequestMapping(value = "/count",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String countData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> count = maintenanceshopService.findShopCount(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",count);
        }catch (Exception e){
            return Constant.toReModel("4000","加载商家统计出错",e);
        }
    }

    /**
     * 商家列表页面
     */
    @RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(ModelMap modelMap){
        return "member/maintenanceshop-list";
    }

    /**
     * 查询商家数据
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> maintenanceshopList = maintenanceshopService.findMaintenanceshopList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",maintenanceshopList);
        }catch (Exception e){
            return Constant.toReModel("4000","加载商家数据失败",e);
        }
    }

    /**
     * 查询可接单的商家
     */
    @RequestMapping(value = "/canDistribution",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String shopData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            List<Map<String, Object>> maintenanceshopList = maintenanceshopService.findCanDistribution(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",maintenanceshopList);
        }catch (Exception e){
            return Constant.toReModel("4000","加载商家数据失败",e);
        }
    }

    /**
     * 查询可参见活动的商家
     */
    @RequestMapping(value = "/activityShop",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String activityData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> maintenanceshopList = maintenanceshopService.findActivityShop(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",maintenanceshopList);
        }catch (Exception e){
            return Constant.toReModel("4000","加载商家数据失败",e);
        }
    }

    /**
     * 编辑商家页面
     */
    @RequestMapping(value = "/editShop.html",method = RequestMethod.GET)
    public String editShopPage(Integer maintenanceshopId,ModelMap modelMap){
        modelMap.put("maintenanceshopId", maintenanceshopId);
        return "member/maintenanceshop-edit";
    }

    /**
     * 商家详情页面
     */
    @RequestMapping(value = "/detail.html",method = RequestMethod.GET)
    public String maintenanceshopDetailPage(Integer maintenanceshopId,Integer status,ModelMap modelMap){
        modelMap.put("maintenanceshopId", maintenanceshopId);
        modelMap.put("status", status);
        return "member/maintenanceshop-detail";
    }

    /**
     * 查询商家详情数据
     */
    @RequestMapping(value = "/shopDetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String shopDetail(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> maintenanceshopDetail = maintenanceshopService.findShopDetail(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",maintenanceshopDetail);
        }catch (Exception e){
            return Constant.toReModel("4000","查询详情失败",e);
        }
    }

    /**
     * 查询其他渠道详情数据
     */
    @RequestMapping(value = "/channelDetail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String channelDetail(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> maintenanceshopDetail = maintenanceshopService.findChannelDetail(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",maintenanceshopDetail);
        }catch (Exception e){
            return Constant.toReModel("4000","查询详情失败",e);
        }
    }

    /**
     * 添加其他渠道
     */
    @RequestMapping(value = "/saveChannel",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("渠道名单>添加其他渠道")
    public String saveChannel(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            System.out.println(request.getSession().getServletContext().getRealPath("/")+"+++++++++++++");
            paramModelMap.put("logoPath",request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeLogo.png");
            paramModelMap.put("backgroundPath",request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeBackground.png");
            String result = maintenanceshopService.saveChannel(paramModelMap);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            e.printStackTrace();
            switch (e.getMessage()){
                case "4001": return Constant.toReModel(e.getMessage(),"必填项不能为空",null);
                case "4003": return Constant.toReModel(e.getMessage(),"该手机号已被其他用户使用",null);
            }
            return Constant.toReModel("4000","添加失败",null);
        }
    }

    /**
     * 编辑其他渠道
     */
    @RequestMapping(value = "/updateChannel",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("渠道名单>编辑其他渠道")
    public String updateChannel(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("logoPath",request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeLogo.png");
			paramModelMap.put("backgroundPath",request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeBackground.png");
            String result = maintenanceshopService.updateModel(paramModelMap);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","编辑失败",null);
        }
    }

    /**
     * 冻结渠道
     */
    @RequestMapping(value = "/freeze",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("渠道名单>冻结渠道")
    public String freeze(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("logoPath",request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeLogo.png");
			paramModelMap.put("backgroundPath",request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeBackground.png");
            paramModelMap.put("status",2);
            maintenanceshopService.updateModel(paramModelMap);
            return Constant.toReModel("0","冻结成功",null);
        }catch (Exception e){
            return Constant.toReModel("4000","冻结失败",e);
        }
    }

    /**
     * 解冻渠道
     */
    @RequestMapping(value = "/unfreeze",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("渠道名单>解冻渠道")
    public String unfreeze(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("status",1);
            maintenanceshopService.updateModel(paramModelMap);
            return Constant.toReModel("0","解冻成功",null);
        }catch (Exception e){
            return Constant.toReModel("4000","解冻失败",e);
        }
    }

    /**
     * 查询维修人员数据
     */
    @RequestMapping(value = "/employee",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String employee(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            List<Map<String, Object>> employeeList = employeeService.findEmployeeList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",employeeList);
        }catch (Exception e){
            return Constant.toReModel("4000","加载员工数据出错",e);
        }
    }

    /**
     * 查询商家服务分记录
     */
    @RequestMapping(value = "/servicePoints",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String servicePoints(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            List<Map<String, Object>> servicePoints = maintenanceshopService.servicePointsRecord(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",servicePoints);
        }catch (Exception e){
            return Constant.toReModel("4000","加载服务分记录出错",e);
        }
    }

    /**
     * 添加商家
     */
    @RequestMapping(value = "/saveMaintenanceshop",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("渠道名单>添加商家")
    public String saveMaintenanceshop(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("logoPath",request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeLogo.png");
            paramModelMap.put("backgroundPath",request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeBackground.png");
            String result = maintenanceshopService.saveMaintenanceshop(paramModelMap);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            switch (e.getMessage()){
                case "4001":return Constant.toReModel(e.getMessage(),"请输入合法的比率",null);
                case "4003": return Constant.toReModel(e.getMessage(),"该手机号已被其他用户使用",null);
            }
            return Constant.toReModel("4000","添加失败",null);
        }
    }

    /**
     * 修改商家
     */
    @RequestMapping(value = "/updateMaintenanceshop",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("渠道名单>修改商家")
    public String updateMaintenanceshop(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = maintenanceshopService.updateMaintenanceshop(paramModelMap);
            switch (result){
                case "0":return Constant.toReModel(result,"SUCCESSFUL",null);
                case "4001":return Constant.toReModel(result,"请输入合法的比率",null);
            }
            return Constant.toReModel("4000","修改失败",null);
        }catch (Exception e){
            return Constant.toReModel("4000","修改失败",e);
        }
    }

    /**
     * 删除商家
     */
    @RequestMapping(value = "/deleteMaintenanceshop",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("渠道名单>删除商家")
    public String deleteMaintenanceshop(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = maintenanceshopService.deleteMaintenanceshop((Integer) paramModelMap.get("id"));
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","删除商家失败",e);
        }
    }

    /**
     * 商家订单页面
     */
    @RequestMapping(value = "/event.html",method = RequestMethod.GET)
    public String maintenanceshopEventPage(Integer maintenanceshopId,ModelMap modelMap){
        modelMap.put("maintenanceshopId", maintenanceshopId);
        return "business/event-list";
    }
    
    /**
     * 更新二维码
     */
    @RequestMapping(value = "/updateQrcode",method = RequestMethod.POST)
    @ResponseBody
    public String updateQrcode(HttpServletRequest request, @RequestBody String strJson){
    	try {
			Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
			paramModelMap.put("id", 100001);
			paramModelMap.put("logoPath",request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeLogo.png");
			paramModelMap.put("backgroundPath",request.getSession().getServletContext().getRealPath("/")+"/cite/images/qrcodeBackground.png");
			String result = maintenanceshopService.updateQrcode(paramModelMap);
			return Constant.toReModel(result, "更新成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel("4000","更新失败",e);
		}
        
    	
    }
}
