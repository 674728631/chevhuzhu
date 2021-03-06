package com.zccbh.demand.controller.member;

import java.io.OutputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zccbh.demand.pojo.model.ExcelFieldEntity;
import com.zccbh.util.base.Base64;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.export.ExcelExport;
import com.zccbh.util.uploadImg.UploadFileUtil;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.zccbh.demand.service.customer.InvitationService;
import com.zccbh.util.collect.Constant;

@Controller
@RequestMapping("/invitation")
public class InvitationController {
    @Autowired
    private InvitationService invitationService;

    @RequestMapping(value = "/list.html",method = RequestMethod.GET)
    public String listPage(ModelMap modelMap) throws Exception{    	
        return "member/invitation-list";
    }
    
    @RequestMapping(value = "/getListDate",method = {RequestMethod.POST,RequestMethod.GET}, produces="application/json;charset=utf-8")
    @ResponseBody
    public String getListDate(HttpServletRequest request, @RequestBody String strJson){
    	try {
			Map<String, Object> paramModelMap = new HashMap<>();
			PageInfo<Map<String, Object>> business = invitationService.findInvitationListFromBusiness(paramModelMap);
			PageInfo<Map<String, Object>> channel = invitationService.findInvitationListFromChannel(paramModelMap);
			Map<String, Object> inviationInfo = invitationService.getInvitationInfo();
			paramModelMap.clear();
			paramModelMap.put("businessCount", business.getTotal());
			paramModelMap.put("channelCount", channel.getTotal());
			paramModelMap.put("inviationInfo", inviationInfo.get("customerNum")+"个用户邀请了"+inviationInfo.get("invitedCustomerNum")+"个车加入");
			return Constant.toReModel("0","SUCCESSFUL",paramModelMap); 
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel("4000","member/invitation-list",e);
		}
    }
    
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/list",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json;charset=utf-8")
    @ResponseBody
    public String listData(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> invitationList = invitationService.findInvitationList1(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",invitationList);
        }catch (Exception e){
        	e.printStackTrace();
            return Constant.toReModel("4000","/invitation/list",e);
        }
    }
    @RequestMapping(value = "/detailList.html",method = {RequestMethod.POST,RequestMethod.GET})
    public String detailListPage(HttpServletRequest request,ModelMap modelMap) throws Exception{
        modelMap.put("customerId", request.getParameter("customerId"));
        modelMap.put("tabType", request.getParameter("tabType"));
        modelMap.put("beginTime", request.getParameter("beginTime"));
        modelMap.put("endTime", request.getParameter("endTime"));

        Map<String, Object> paramModelMap = new HashMap<>();
        paramModelMap.put("customerId", request.getParameter("customerId"));
        paramModelMap.put("tabType", request.getParameter("tabType"));
        paramModelMap.put("beginTime", request.getParameter("beginTime"));
        paramModelMap.put("endTime", request.getParameter("endTime"));
        paramModelMap.put("endTimeTmp", request.getParameter("endTime"));
        PageInfo<Map<String, Object>> guanchaList = null;
        PageInfo<Map<String, Object>> baozhangList = null;
        if ("1".equals(paramModelMap.get("tabType"))){
            paramModelMap.put("status",13);
            guanchaList = invitationService.listUserDetail(paramModelMap);
            paramModelMap.put("status",20);
            paramModelMap.put("endTime", paramModelMap.get("endTimeTmp"));
            baozhangList = invitationService.listUserDetail(paramModelMap);
        }else if ("2".equals(paramModelMap.get("tabType"))) {
            paramModelMap.put("status",13);
            guanchaList = invitationService.listBusinessDetail(paramModelMap);
            paramModelMap.put("status",20);
            paramModelMap.put("endTime", paramModelMap.get("endTimeTmp"));
            baozhangList = invitationService.listBusinessDetail(paramModelMap);
        }else if ("3".equals(paramModelMap.get("tabType"))) {
            paramModelMap.put("status",13);
            guanchaList = invitationService.listChannelDetail(paramModelMap);
            paramModelMap.put("status",20);
            paramModelMap.put("endTime", paramModelMap.get("endTimeTmp"));
            baozhangList = invitationService.listChannelDetail(paramModelMap);
        }

        if (guanchaList == null){
            modelMap.put("guanchaCount",0);
        } else {
            modelMap.put("guanchaCount",guanchaList.getTotal());
        }
        if (baozhangList == null){
            modelMap.put("baozhangCount",0);
        }else {
            modelMap.put("baozhangCount", baozhangList.getTotal());
        }

        return "member/invitation-detailList";
    }
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/detailList/{customerId}",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json;charset=utf-8")
    @ResponseBody
    public String detailListData(HttpServletRequest request,@PathVariable("customerId") String customerId, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            paramModelMap.put("customerId", customerId);

            PageInfo<Map<String, Object>> invitationList = null;
            if ("1".equals(paramModelMap.get("tabType"))){
                invitationList = invitationService.listUserDetail(paramModelMap);
            }else if ("2".equals(paramModelMap.get("tabType"))) {
                invitationList = invitationService.listBusinessDetail(paramModelMap);
            }else if ("3".equals(paramModelMap.get("tabType"))) {
                invitationList = invitationService.listChannelDetail(paramModelMap);
            } else {
                // 其他情况，默认查询用户的详情
                invitationList = invitationService.listUserDetail(paramModelMap);
            }
            return Constant.toReModel("0","SUCCESSFUL",invitationList);
        }catch (Exception e){
            return Constant.toReModel("4000","/invitation/detailList",e);
        }
    }

    /**
     *  商家的列表查询
     * @param request
     * @param strJson
     * @return
     */
    @RequestMapping(value = "/businessList",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json;charset=utf-8")
    @ResponseBody
    public String businessList(HttpServletRequest request, @RequestBody String strJson){
        try{
        	System.out.println(strJson);
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> invitationList = invitationService.findInvitationListFromBusiness(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",invitationList);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("4000","/invitation/businessList",e);
        }
    }

    /**
     *  其他渠道的列表查询
     * @param request
     * @param strJson
     * @return
     */
    @RequestMapping(value = "/channelList",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json;charset=utf-8")
    @ResponseBody
    public String channelList(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> invitationList = invitationService.findInvitationListFromChannel(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",invitationList);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("4000","/invitation/businessList",e);
        }
    }

    /**
     * 用户的邀请记录导出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcelForUser")
    public String importExcelForUser(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String,Object> paramModelMap = new HashMap<>();
            paramModelMap.put("pageNo", request.getParameter("pageNo"));
            paramModelMap.put("pageSize", request.getParameter("pageSize"));
            paramModelMap.put("customerPN", request.getParameter("customerPN"));
            paramModelMap.put("beginTime", request.getParameter("beginTime"));
            paramModelMap.put("endTime", request.getParameter("endTime"));
            PageInfo<Map<String, Object>> invitationList = invitationService.findInvitationList1(paramModelMap);
            List<Map<String,Object>> list = invitationList.getList();
            List<ExcelFieldEntity> rowList = new ArrayList<ExcelFieldEntity>();
            for (Map<String,Object> map:list) {
                ExcelFieldEntity rowData = new ExcelFieldEntity();
                rowData.setFieldOne(String.valueOf(map.get("nickname")));
                rowData.setFieldTwo(String.valueOf(map.get("customerPN")));
                rowData.setFieldThree("0");
                rowData.setFieldFour(String.valueOf(map.get("count")));
                rowData.setFieldfive(String.valueOf(map.get("guancha")));
                rowData.setFieldSix(String.valueOf(map.get("baozhang")));
                rowData.setFieldSeven(String.valueOf(map.get("ratio")) + "%");
                rowData.setFieldEight(String.valueOf(map.get("createAt")));
                rowList.add(rowData);
            }
            response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("用户拉新统计.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
            OutputStream out = response.getOutputStream();
            String [] headers = new String[]{"昵称/名称", "手机号","关注数","注册数","观察期车辆","保障中车辆","占比（观察期+保障中）","注册时间"};
            ExcelExport.exportExcel("拉新统计",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","导出数据失败",e);
        }
    }

    /**
     * 商家的邀请记录导出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcelForBusiness")
    public String importExcelForBusiness(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String,Object> paramModelMap = new HashMap<>();
            paramModelMap.put("pageNo", request.getParameter("pageNo"));
            paramModelMap.put("pageSize", request.getParameter("pageSize"));
            paramModelMap.put("customerPN", request.getParameter("customerPN"));
            paramModelMap.put("beginTime", request.getParameter("beginTime"));
            paramModelMap.put("endTime", request.getParameter("endTime"));
            PageInfo<Map<String, Object>> invitationList = invitationService.findInvitationListFromBusiness(paramModelMap);
            List<Map<String,Object>> list = invitationList.getList();
            List<ExcelFieldEntity> rowList = new ArrayList<ExcelFieldEntity>();
            for (Map<String,Object> map:list) {
                ExcelFieldEntity rowData = new ExcelFieldEntity();
                rowData.setFieldOne(String.valueOf(map.get("name")));
                rowData.setFieldTwo(String.valueOf(map.get("tel")));
                rowData.setFieldThree(String.valueOf(map.get("followNum")));
                rowData.setFieldFour(String.valueOf(map.get("registerNum")));
                rowData.setFieldfive(String.valueOf(map.get("guancha")));
                rowData.setFieldSix(String.valueOf(map.get("baozhang")));
                rowData.setFieldSeven(String.valueOf(map.get("ratio")) + "%");
                rowData.setFieldEight(String.valueOf(map.get("createAt")));
                rowList.add(rowData);
            }
            response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("商家拉新统计.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
            OutputStream out = response.getOutputStream();
            String [] headers = new String[]{"昵称/名称", "手机号","关注数","注册数","观察期车辆","保障中车辆","占比（观察期+保障中）","注册时间"};
            ExcelExport.exportExcel("拉新统计",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","导出数据失败",e);
        }
    }

    /**
     * 其他渠道的邀请记录导出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcelForChannel")
    public String importExcelForChannel(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String,Object> paramModelMap = new HashMap<>();
            paramModelMap.put("pageNo", request.getParameter("pageNo"));
            paramModelMap.put("pageSize", request.getParameter("pageSize"));
            paramModelMap.put("customerPN", request.getParameter("customerPN"));
            paramModelMap.put("beginTime", request.getParameter("beginTime"));
            paramModelMap.put("endTime", request.getParameter("endTime"));
            PageInfo<Map<String, Object>> invitationList = invitationService.findInvitationListFromChannel(paramModelMap);
            List<Map<String,Object>> list = invitationList.getList();
            List<ExcelFieldEntity> rowList = new ArrayList<ExcelFieldEntity>();
            for (Map<String,Object> map:list) {
                ExcelFieldEntity rowData = new ExcelFieldEntity();
                rowData.setFieldOne(String.valueOf(map.get("name")));
                rowData.setFieldTwo(String.valueOf(map.get("tel")));
                rowData.setFieldThree(String.valueOf(map.get("followNum")));
                rowData.setFieldFour(String.valueOf(map.get("registerNum")));
                rowData.setFieldfive(String.valueOf(map.get("guancha")));
                rowData.setFieldSix(String.valueOf(map.get("baozhang")));
                rowData.setFieldSeven(String.valueOf(map.get("ratio")) + "%");
                rowData.setFieldEight(String.valueOf(map.get("createAt")));
                rowList.add(rowData);
            }
            response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("其他渠道拉新统计.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
            OutputStream out = response.getOutputStream();
            String [] headers = new String[]{"昵称/名称", "手机号","关注数","注册数","观察期车辆","保障中车辆","占比（观察期+保障中）","注册时间"};
            ExcelExport.exportExcel("拉新统计",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","导出数据失败",e);
        }
    }

    /**
     * 拉新详情的导出
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcelForPullNewDetail")
    public String importExcelForPullNewDetail(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String,Object> paramModelMap = new HashMap<>();
            paramModelMap.put("pageNo", request.getParameter("pageNo"));
            paramModelMap.put("pageSize", request.getParameter("pageSize"));
            paramModelMap.put("customerPN", request.getParameter("customerPN"));
            paramModelMap.put("status", request.getParameter("status"));
            paramModelMap.put("tabType", request.getParameter("tabType"));
            paramModelMap.put("customerId", request.getParameter("customerId"));
            paramModelMap.put("beginTime", request.getParameter("beginTime"));
            paramModelMap.put("endTime", request.getParameter("endTime"));

            PageInfo<Map<String, Object>> invitationList = null;
            if ("1".equals(paramModelMap.get("tabType"))){
                invitationList = invitationService.listUserDetail(paramModelMap);
            }else if ("2".equals(paramModelMap.get("tabType"))) {
                invitationList = invitationService.listBusinessDetail(paramModelMap);
            }else if ("3".equals(paramModelMap.get("tabType"))) {
                invitationList = invitationService.listChannelDetail(paramModelMap);
            } else {
                // 其他情况，默认查询用户的详情
                invitationList = invitationService.listUserDetail(paramModelMap);
            }

            List<Map<String,Object>> list = invitationList.getList();
            List<ExcelFieldEntity> rowList = new ArrayList<ExcelFieldEntity>();
            for (Map<String,Object> map:list) {
                ExcelFieldEntity rowData = new ExcelFieldEntity();
                String portrait = String.valueOf(map.get("portrait"));
                rowData.setFieldOne(portrait.indexOf("thirdwx.qlogo.cn")==-1? UploadFileUtil.getImgURL(CommonField.POSTER_IMG_URL,portrait):portrait);
                rowData.setFieldTwo(String.valueOf(map.get("nickname")));
                rowData.setFieldThree(String.valueOf(map.get("customerPN")));
                rowData.setFieldFour(String.valueOf(map.get("licensePlateNumber")));
                int carStatus = Integer.parseInt(map.get("status").toString());
                String status = carStatus==1?"待支付":carStatus==2?"待添加照片":carStatus==10?"待审核":carStatus==12?"未通过":carStatus==13?"观察期":
                        carStatus==20?"保障中":carStatus==30?"退出计划":carStatus==31?"不可用":"未知";
                rowData.setFieldfive(status);
                rowData.setFieldSix(String.valueOf(map.get("rechargeAmount")));
                rowData.setFieldSeven(String.valueOf(map.get("amtCooperation")));
                rowList.add(rowData);
            }
            response.setContentType("octets/stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("拉新详情统计.xls".getBytes("UTF-8"),"iso-8859-1")); //excel Name
            OutputStream out = response.getOutputStream();
            String [] headers = new String[]{"头像","昵称", "手机号","车牌号","车辆状态","充值金额","余额"};
            ExcelExport.exportExcel("拉新统计",headers,rowList,out,"yyyy-MM-dd hh:mm:ss");
            return Constant.toReModel("0","SUCCESSFUL",null);
        }catch (Exception e){
            return Constant.toReModel("4000","导出数据失败",e);
        }
    }
    
    @RequestMapping(value = "/userDate",method = RequestMethod.POST,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String userDate(HttpServletRequest request, @RequestBody String strJson){
    	try {
			Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
			PageInfo<Map<String, Object>> invitationList = invitationService.findUserInvitationList(paramModelMap);
			return Constant.toReModel("0","SUCCESSFUL",invitationList);
		} catch (Exception e) {
			e.printStackTrace();
			return Constant.toReModel("4000","加载数据失败",e);
		}
    }
    
    /**
     *  商家的列表查询
     * @param request
     * @param strJson
     * @return
     */
    @RequestMapping(value = "/businessDate",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json;charset=utf-8")
    @ResponseBody
    public String businessDate(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> invitationList = invitationService.findBusinessInvitationList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",invitationList);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("4000","/invitation/businessList",e);
        }
    }
    
    /**
     * 其他渠道拉新数据
     * @author xiaowuge  
     * @date 2018年11月9日  
     * @version 1.0
     */
    @RequestMapping(value = "/channelDate",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json;charset=utf-8")
    @ResponseBody
    public String channelDate(HttpServletRequest request, @RequestBody String strJson){
        try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> invitationList = invitationService.findChannelInvitationList(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",invitationList);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("4000","/invitation/businessList",e);
        }
    }
    
    /**
     * 拉新数据
     */
    @RequestMapping(value = "/inviationDate",method = {RequestMethod.POST,RequestMethod.GET},produces = "application/json;charset=utf-8")
    @ResponseBody
    public String inviationDate(HttpServletRequest request, @RequestBody String strJson){
    	try{
            Map<String,Object> paramModelMap = JSONObject.fromObject(strJson);
            PageInfo<Map<String, Object>> invitationList = invitationService.findInviationDate(paramModelMap);
            return Constant.toReModel("0","SUCCESSFUL",invitationList);
        }catch (Exception e){
            e.printStackTrace();
            return Constant.toReModel("4000","/invitation/businessList",e);
        }
    }
}
