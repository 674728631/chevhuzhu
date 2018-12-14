package com.zccbh.demand.controller.system;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.pojo.system.Log;
import com.zccbh.demand.service.system.LogService;
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
@RequestMapping("/log")
public class LogController {
    @Autowired
    private LogService logService;

    /**
     * 日志列表页面
     */
    @RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(ModelMap modelMap){
        return "system/log-list";
    }

    /**
     * 查询日志数据
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Log> customerList = logService.findLogList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",customerList);
        }catch (Exception e){
            return Constant.toReModel("4000","/log/list",e);
        }
    }
}
