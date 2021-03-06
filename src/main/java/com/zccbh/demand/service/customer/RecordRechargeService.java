package com.zccbh.demand.service.customer;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RecordRechargeService {
    @Autowired
    private RecordRechargeMapper recordRechargeMapper;

    @Autowired
    private FoundationMapper foundationMapper;

    /**
     * 查询充值数据
     * @param paramModelMap 查询条件
     * @return 充值信息
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> customerRecord(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> rechargeList = recordRechargeMapper.customerRecord(paramModelMap);
        PageInfo<Map<String, Object>> rechargeInfo = new PageInfo<>(rechargeList);
        return rechargeInfo;
    }

    /**
     * 查询充值数据
     * @param paramModelMap 查询条件
     * @return 充值信息
     * @throws Exception
     */
    public List<Map<String, Object>> findAllRecharge(Map<String, Object> paramModelMap) throws Exception {
        return recordRechargeMapper.findRechargeList(paramModelMap);
    }
    
    public Map<String, Object> findRechargeDate(Map<String, Object> paramModelMap) throws Exception{
    	Map<String,Object> resultMap=recordRechargeMapper.findRechargeDate(paramModelMap); 
    	Map result = new HashMap<>();
    	result.put("rechargeAmt", resultMap.get("rechargeAmt"));
    	result.put("rechargeNum", resultMap.get("rechargeNum"));
    	result.put("amt1Total", resultMap.get("amt1Total"));
    	result.put("amt3Total", resultMap.get("amt3Total"));
    	result.put("amt9Total", resultMap.get("amt9Total"));
    	result.put("amt99Total", resultMap.get("amt99Total"));
    	result.put("rescueTotal", resultMap.get("rescueTotal"));
    	return result;
    }
    
    @Transactional
    public List<Map<String, Object>> getRechargeInfo(Map<String, Object> map)throws Exception{
//    	List<Map<String, Object>> list = new ArrayList<>();
//    	//获取9元充值数据
//    	Map<String, Object> nineInfo = recordRechargeMapper.selectNine(map);
//    	list.add(nineInfo);
//    	//获取99元数据
//    	Map<String, Object> twoNineInfo = recordRechargeMapper.selectTwoNine(map);
//    	list.add(twoNineInfo);
		return recordRechargeMapper.getRechargeInfo();
    }
    
    @Transactional
    public Map<String, Object> getReEnter()throws Exception{
    	Map<String, Object> reEnterDetail = new HashMap<>();
    	Map<String, Object> total = recordRechargeMapper.selectTotalReEnter();
    	BigDecimal totalNum = null;
		if (total == null){
			totalNum = new BigDecimal(0);
		} else {
			totalNum = new BigDecimal(total.get("total").toString());
		}
    	reEnterDetail.put("total", totalNum);
    	Map<String, Object> naturalTotal = recordRechargeMapper.selectNaturalReEnter();
    	BigDecimal naturalNum = (naturalTotal == null)?(new BigDecimal(0)):((new BigDecimal(naturalTotal.get("total").toString()))); //自然用户复购数
    	BigDecimal giveNum = totalNum.subtract(naturalNum); //赠送用户复购数
    	reEnterDetail.put("naturalNum", naturalNum.divide(totalNum,3,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
    	reEnterDetail.put("giveNum", giveNum.divide(totalNum,3,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
    	return reEnterDetail;
    }
    
    public List<Map<String, Object>> getShopList() throws Exception{
    	//获取总补贴
//    	Map<String, Object> totalSubsidy = recordRechargeMapper.selectSubsidy();
		Foundation totalSubsidy = foundationMapper.findEntitySingle(new HashMap<>());

    	BigDecimal subsidy = null;
		if (totalSubsidy == null){
			subsidy = new BigDecimal(0);
		} else {
//			subsidy = (BigDecimal)totalSubsidy.get("total");
			subsidy = totalSubsidy.getAllowance();
		}
    	//获取总理赔金额
//    	Map<String, Object> totalEventMoney = recordRechargeMapper.selectEventMoney();
    	//总理赔次数
    	/*Map<String, Object> totalEventNum = recordRechargeMapper.selectEventTotal();
    	BigDecimal eventNum = null;
		if (totalEventNum == null){
			eventNum = new BigDecimal(0);
		} else {
			eventNum = new BigDecimal(totalEventNum.get("total").toString());
		}*/

    	//渠道修理厂排行
    	List<Map<String, Object>> listShop = recordRechargeMapper.listShop();
    	
    	for(Map<String, Object> shop : listShop){
    		//计算补贴比例
    		BigDecimal shopSubsidy = new BigDecimal(shop.get("shopTotal") == null ? "0":shop.get("shopTotal").toString());
    		if (Constant.toEmpty(shop.get("shopTotal"))) {
    			shop.put("shopSubsidy",shopSubsidy.divide(subsidy,3,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			}else{
				shop.put("shopSubsidy", 0);
			}
    		
    		//计算理赔率
//    		BigDecimal shopEventNum = new BigDecimal(shop.get("countEvent") == null ? "0":shop.get("countEvent").toString());
    		BigDecimal sumRechargeAmt = new BigDecimal(shop.get("sumRechargeAmt") == null ? "0":shop.get("sumRechargeAmt").toString());
    		BigDecimal shopEventTotal = new BigDecimal(shop.get("shopEventTotal") == null ? "0":shop.get("shopEventTotal").toString());

    		if (shopEventTotal.doubleValue() != 0) {
    			shop.put("shopEventRatio",shopEventTotal.divide(sumRechargeAmt.add(shopSubsidy),3,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
			}else{
				shop.put("shopEventRatio", 0);
			}
		}
    	return listShop;
    }
}


