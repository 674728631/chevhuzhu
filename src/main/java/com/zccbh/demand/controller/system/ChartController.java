package com.zccbh.demand.controller.system;

import com.zccbh.demand.service.system.ChartService;
import com.zccbh.util.collect.Constant;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/chart")
public class ChartController {
	@Autowired
    private ChartService chartService;

    /**
     * 注册用户数据
     */
    @RequestMapping(value = "/count",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String count(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> parameModelMap = JSONObject.fromObject(strJson);
            Map<String, Object> count = null;
            if("register".equals(parameModelMap.get("chartType").toString())){
                count = chartService.register(parameModelMap);
            }else if ("view".equals(parameModelMap.get("chartType").toString())){
                count = chartService.view(parameModelMap);
            }else if("guarantee".equals(parameModelMap.get("chartType").toString())){
                count = chartService.guarantee(parameModelMap);
            }else if("shop".equals(parameModelMap.get("chartType").toString())){
                count = chartService.shop(parameModelMap);
            }else if("channel".equals(parameModelMap.get("chartType").toString())){
                count = chartService.channel(parameModelMap);
            }else if("event".equals(parameModelMap.get("chartType").toString())){
                count = chartService.event(parameModelMap);
            }else if("foundation".equals(parameModelMap.get("chartType").toString())){
                count = chartService.foundation(parameModelMap);
            }else if("initNum".equals(parameModelMap.get("chartType").toString())){
                count = chartService.initNum(parameModelMap);
            }else if("observationNum".equals(parameModelMap.get("chartType").toString())){
                count = chartService.observationNum(parameModelMap);
            }else if("guaranteeNum".equals(parameModelMap.get("chartType").toString())){
                count = chartService.guaranteeNum(parameModelMap);
            }else if("outNum".equals(parameModelMap.get("chartType").toString())){
                count = chartService.outNum(parameModelMap);
            }else if("twiceRecharge".equals(parameModelMap.get("chartType").toString())){
                count = chartService.twiceRecharge(parameModelMap);
            }
            return Constant.toReModel("0","SUCCESSFUL",count);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("4000","加载统计图出错",e);
        }
    }
}
