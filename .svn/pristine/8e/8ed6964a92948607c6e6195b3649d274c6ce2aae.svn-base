package com.zccbh.demand.controller.finance;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.annotation.LogAnnotation;
import com.zccbh.demand.service.foundation.OrderStatisticalService;
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
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/orderStatistical")
public class OrderStatisticalController {
    @Autowired
    private OrderStatisticalService orderStatisticalService;

    /**
     * 保险代办管理页面
     */
    @RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(ModelMap modelMap){
        return "finance/orderStatistical";
    }

    /**
     * 查询统计数据
     */
    @RequestMapping(value = "/sumData",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request){
        try{
            return Constant.toReModel("0","SUCCESSFUL",orderStatisticalService.findSumData());
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("4000","加载数据出错",e);
        }
    }

    /**
     * 查询保险理赔收支明细数据
     */
    @RequestMapping(value = "/orderList",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String recordList(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> rechargeList = orderStatisticalService.findOrderList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",rechargeList);
        }catch (Exception e){
            return Constant.toReModel("4000","加载数据出错",e);
        }
    }

    /**
     * 后台为车辆充值
     * @param request
     * @return
     */
    @RequestMapping(value = "/settle",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    @LogAnnotation("理赔代办>渠道结算")
    public String updateAmt(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            if(Constant.toEmpty(paramModelMap.get("amtChannel")) && Constant.toEmpty(paramModelMap.get("id"))){
                //判断金额是否为数字
                try {
                    new BigDecimal((String) paramModelMap.get("amtChannel"));
                }catch (Exception e){
                    return Constant.toReModel("4000","请输入合法的金额",null);
                }
                orderStatisticalService.updateOrderStatistical(paramModelMap);
                return Constant.toReModel("0","SUCCESSFUL",null);
            }
            return Constant.toReModel("4000","请填写结算金额",null);
        }catch (Exception e){
            return Constant.toReModel("4000","操作出错",e);
        }
    }
}
