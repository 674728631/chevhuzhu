package com.zccbh.demand.controller.order;

import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.controller.quartz.CommentJob;
import com.zccbh.demand.controller.quartz.QuartzUtils;
import com.zccbh.demand.service.event.DistributionOrder;
import com.zccbh.demand.service.order.OrderService;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.collect.Constant;
import net.sf.json.JSONObject;
import org.quartz.Scheduler;
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
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private DistributionOrder distributionOrder;

    /**
     * 加载保险理赔统计数据
     */
    @RequestMapping(value = "/count",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String countData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> count = orderService.findOrderCount(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",count);
        }catch (Exception e){
            return Constant.toReModel("4000","加载订单统计出错",e);
        }
    }

    /**
     * 保险理赔列表页面
     */
	@RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(Integer flag,String isInvalid,String status,String searchInfo,ModelMap modelMap){
        modelMap.put("flag", flag);
        modelMap.put("isInvalid", isInvalid);
        modelMap.put("status", status);
        modelMap.put("searchInfo", searchInfo);
	    return "order/order-list";
    }


    /**
     * 加载保险理赔列表数据
     */
	@RequestMapping(value = "/list",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            return Constant.toReModel("0","SUCCESSFUL",orderService.findOrderList(paramModelMap));
        }catch (Exception e){
            return Constant.toReModel("4000","加载保险理赔数据出错",e);
        }
    }

    /**
     * 理赔订单详情页面
     */
    @RequestMapping(value = "/detail.html",method = RequestMethod.GET)
    public String detailPage(Integer status,String orderNo,ModelMap modelMap){
        modelMap.put("status", status);
        modelMap.put("orderNo", orderNo);
        return "order/order-detail";
    }

    /**
     * 查询申请理赔详情数据
     */
    @RequestMapping(value = "/detail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String eventApply(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            return Constant.toReModel("0","SUCCESSFUL",orderService.findDetail(paramModelMap));
        }catch (Exception e){
            return Constant.toReModel("4000","加载数据失败",e);
        }
    }

    /**
     * 申请理赔审核通过
     */
    @RequestMapping(value = "/applysuccess",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("保险理赔>申请通过")
    public String applysuccess(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
           orderService.applysuccess(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","审核出错",e);
        }
    }

    /**
     * 申请理赔审核不通过
     */
    @RequestMapping(value = "/applyfail",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("保险理赔>申请不通过")
    public String applyfail(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
           orderService.applyfail(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","审核出错",e);
        }
    }

    /**
     * 分单
     */
    @RequestMapping(value = "/distribution",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("保险理赔>手动分单")
    public String distribution(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            String result = distributionOrder.distributionOfOrder(paramModelMap);
            return Constant.toReModel(result,"SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","分单失败",e);
        }
    }

    /**
     * 定损审核通过
     */
    @RequestMapping(value = "/assertsuccess",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("保险理赔>定损确认")
    public String assertsuccess(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            orderService.assertSuccess(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","审核出错",e);
        }
    }

    /**
     * 处理投诉
     */
    @RequestMapping(value = "/complaintsuccess",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("保险理赔>处理投诉")
    public String complaintsuccess(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            orderService.complaintsuccess(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","处理投诉出错",e);
        }
    }

    /**
     * 废弃订单
     */
    @RequestMapping(value = "/invalidOrder",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("保险理赔>废弃订单")
    public String invalidOrder(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            orderService.invalidOrder(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","废弃订单出错",e);
        }
    }
}
