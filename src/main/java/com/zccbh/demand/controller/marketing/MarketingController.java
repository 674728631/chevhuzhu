package com.zccbh.demand.controller.marketing;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.service.customer.RecordRechargeService;
import com.zccbh.demand.service.marketing.MarketingService;
import com.zccbh.util.base.DateUtils;
import com.zccbh.util.collect.Constant;

import net.sf.json.JSONObject;

/**
 * @author xiaowuge
 * @ClassName: MarketingController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2018年10月24日 下午5:27:21
 */
@Controller
@RequestMapping("/marketing")
public class MarketingController {

    @Autowired
    private MarketingService marketingService;

    @Autowired
    private RecordRechargeService recordRechargeService;
    
    @RequestMapping(value = "/weChat.html", method = RequestMethod.GET)
    public String weChat(){
    	return "marketing/weChat";
    }

    /**
     * 营销统计页面
     */
    @RequestMapping(value = "/list.html", method = RequestMethod.GET)
    public String listPage(ModelMap model) {
        try {
            Map<String, Object> map = new HashMap<>();
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
            String beginTime = time.format(new Date());
            map.put("beginTime", beginTime);
            Date endDate = DateUtils.getDayDate(new Date(), "+", 1);
            String endTime = time.format(endDate);
            map.put("endTime", endTime);
            Map<String, Object> resultMap = marketingService.localShow(map);
            model.addAttribute("weChatConcernNum", resultMap.get("weChatConcernNum"));
            model.addAttribute("registerNum", resultMap.get("registerNum"));
            model.addAttribute("observationNum", resultMap.get("observationNum"));
            model.addAttribute("guaranteeNum", resultMap.get("guaranteeNum"));
            model.addAttribute("rechargeNum", resultMap.get("rechargeNum"));
            model.addAttribute("eventNum", resultMap.get("eventNum"));
            model.addAttribute("eventMoney", resultMap.get("eventMoney"));
            return "marketing/marketing-detail";
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel("4000", "加载数据出错", e);
        }
    }

    /**
     * 获取局部数据--概况
     *
     * @author xiaowuge
     * @date 2018年10月26日
     * @version 1.0
     */
    @RequestMapping(value = "/localShow", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String localShow(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> parameModelMap = JSONObject.fromObject(strJson);
            //获取微信关注数
            Map<String, Object> resultMap = marketingService.localShow(parameModelMap);
            return Constant.toReModel("0", "SUCCESSFUL", resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel("4000", "加载数据出错", e);
        }

    }

    @RequestMapping(value = "/getRechargeInfo", method = RequestMethod.POST)
    @ResponseBody
    public String getRechargeInfo(HttpServletRequest request, @RequestBody String strJson) {
        try {
            Map<String, Object> parameModelMap = JSONObject.fromObject(strJson);
            List<Map<String, Object>> list = recordRechargeService.getRechargeInfo(parameModelMap);
            return Constant.toReModel("0", "success", list);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel("4000", "加载数据失败", e);
        }

    }

    /**
     * 全局数据
     *
     * @return
     */
    @GetMapping(value = "/getGlobalDate", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String globalDate() {
        Map<String, Object> outDate = marketingService.globalDate();
        return Constant.toReModel("200", "OK", outDate);
    }

    /**
     * 全局理赔概况
     *
     * @return
     */
    @GetMapping(value = "/globalClaimsAnalysis", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String globalClaimsAnalysis() {
        Map<String, Object> outDate = marketingService.globalClaimsAnalysis();
        return Constant.toReModel("200", "OK", outDate);
    }

    /**
     * 理赔金额比例
     *
     * @return
     */
    @GetMapping(value = "/eventAmt", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String eventAmt() {
        List<Map<String, Object>> outDate = marketingService.eventAmt();
        return Constant.toReModel("200", "OK", outDate);
    }

    /**
     * 虚拟补贴
     *
     * @return
     */
    @GetMapping(value = "/rechargeNum", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String rechargeNum() {
        List<Map<String, Object>> outDate = marketingService.rechargeNum();
        return Constant.toReModel("200", "OK", outDate);
    }

    /**
     * 维修厂结算数据
     *
     * @return
     */
    @PostMapping(value = "/maintenanceshopAmt", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String maintenanceshopAmt(@RequestBody Map<String,Object> paramModelMap) {
        try {
            PageInfo<Map<String, Object>> outDate = marketingService.maintenanceshopBill(paramModelMap);
            return Constant.toReModel("200", "OK", outDate);
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.toReModel("4000","加载数据出错",e);
        }
    }
    
    /**
     * 受损部位统计
     *
     * @return
     */
    @GetMapping(value = "/getDamagePosition", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getDamagePosition() {
        List<Map<String, Object>> outDate = marketingService.getDamagePosition();
        return Constant.toReModel("200", "OK", outDate);
    }
    
    /**
     * 获取复购信息
     * @author xiaowuge  
     * @date 2018年11月16日  
     * @version 1.0
     */
    @RequestMapping(value = "/getReEnter", method = RequestMethod.POST)
    @ResponseBody
    public String getReEnter(){
    	try {
//			Map<String, Object> map = recordRechargeService.getReEnter();
            Map<String, Object> map = marketingService.getReEnter();
			return Constant.toReModel("0", "ok", map);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel("200", "fale", null);
		}
    }
    
    @RequestMapping(value = "/getShopList", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getShopList(){
    	try {
			List<Map<String, Object>> shopList = recordRechargeService.getShopList();
			return Constant.toReModel("0", "ok", shopList);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel("200", "fale", null);
		}
    }
}
