package com.zccbh.demand.service.foundation;

import com.zccbh.demand.mapper.business.AccountMapper;
import com.zccbh.demand.mapper.customer.CarMapper;
import com.zccbh.demand.mapper.customer.RecordRechargeMapper;
import com.zccbh.demand.mapper.event.EventMapper;
import com.zccbh.demand.mapper.foundation.FoundationMapper;
import com.zccbh.demand.pojo.common.Foundation;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class FoundationService {
    @Autowired
    private FoundationMapper foundationMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private RecordRechargeMapper rechargeMapper;
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 修改互助金金额
     */
    public void update(BigDecimal amt) throws Exception {
        Foundation foundation =  foundationMapper.findEntitySingle(new HashMap<>());
        Map<String,Object> upfMap = new HashMap<>();
        upfMap.put("amtTotal", foundation.getAmtTotal().add(amt));
        upfMap.put("amtBalance", foundation.getAmtBalance().add(amt));
        upfMap.put("versions", foundation.getVersions());
        Integer integer = foundationMapper.updateData(upfMap);
        if (integer==0)
            update(amt);
        else
            System.out.println("数据修改成功");
        return;
    }

    /**
     * 查询互助总额数据
     * @param paramModelMap 查询条件
     * @return 互助总额信息
     * @throws Exception
     */
    public Map<String, Object> findFoundationData(Map<String, Object> paramModelMap) throws Exception {
        Map result = new HashMap();
        Foundation foundation = foundationMapper.findEntitySingle(paramModelMap);
        result.put("amtTotal",foundation.getAmtTotal());
        result.put("realPaid",foundation.getRealPaid());
        result.put("amtBalance",foundation.getAmtTotal().subtract(foundation.getRealPaid()));
        result.put("amtPaid",foundation.getAmtPaid());
        result.put("allowance",foundation.getAllowance());
        result.put("payNum",foundation.getPayNum());

        Map<String, Object> carCount = carMapper.findCount(new HashMap<>());
        Integer eventNum = Integer.parseInt(eventMapper.findCompleteEvent().get("eventNum").toString());
//        Integer carNum = Integer.parseInt(carCount.get("observationNum").toString())+Integer.parseInt(carCount.get("guaranteeNum").toString());
        Integer carNum = Integer.parseInt(carCount.get("guaranteeNum").toString());
        result.put("eventRadio",new BigDecimal(eventNum).multiply(new BigDecimal(100)).divide(new BigDecimal(carNum), 1, RoundingMode.HALF_UP));
        result.put("carNum",Integer.parseInt(carCount.get("totalNum").toString())-Integer.parseInt(carCount.get("initNum").toString()));
        // 查询本月实际支出
        Map<String, Object> currentMonthRealPaidMap = foundationMapper.findCurrentMonthRealPaid();
        if (currentMonthRealPaidMap != null && Constant.toEmpty(currentMonthRealPaidMap.get("currentMonthRealPaid"))){
            result.put("currentMonthRealPaid",currentMonthRealPaidMap.get("currentMonthRealPaid"));
        }
        return result;
    }

    /**
     * 查询理赔率
     */
    public BigDecimal findEventRadio() throws Exception {
        Map param = new HashMap();
        Map<String, Object> carCount = carMapper.findCount(param);
        Map<String, Object> eventCount = eventMapper.findCount(param);
        Integer eventNum = Integer.parseInt(eventCount.get("waitRepairNum").toString())+Integer.parseInt(eventCount.get("repairNum").toString())+Integer.parseInt(eventCount.get("receiveCarNum").toString())+Integer.parseInt(eventCount.get("commentNum").toString())+Integer.parseInt(eventCount.get("complaintNum").toString())+Integer.parseInt(eventCount.get("completeNum").toString());
        Integer carNum = Integer.parseInt(carCount.get("observationNum").toString())+Integer.parseInt(carCount.get("guaranteeNum").toString());
        return new BigDecimal(eventNum).multiply(new BigDecimal(100)).divide(new BigDecimal(carNum), 1, RoundingMode.HALF_UP);
    }

    /**
     * 查询虚拟补贴
     */
    public Double findAllowance() throws Exception {
        List<Map<String, Object>> recharges = rechargeMapper.findRecharge();
        return recharges.stream().filter(a -> a.get("type").toString().equals("3") | a.get("type").toString().equals("4")).map(c -> new BigDecimal(c.get("amt").toString())).reduce(new BigDecimal(0), (b, c) -> b.add(c)).doubleValue();
    }

    /**
     * 查询充值用户
     */
    public int findCarNum() throws Exception {
        Map param = new HashMap();
        Map<String, Object> carCount = carMapper.findCount(param);
        return Integer.parseInt(carCount.get("totalNum").toString())-Integer.parseInt(carCount.get("initNum").toString());
    }

    /**
     * 查询充值笔数
     */
    public int findPayNum() throws Exception {
        List<Map<String, Object>> recharges = rechargeMapper.findRechargeNum();
        return recharges.size();
    }

    /**
     * 查询实际支出金额
     */
    public String findRealPaid() throws Exception {
        return accountMapper.findRealPaid().get("realPaid").toString();
    }
}


