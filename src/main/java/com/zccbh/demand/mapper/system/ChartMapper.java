package com.zccbh.demand.mapper.system;

import java.util.List;
import java.util.Map;

public interface ChartMapper {

    Map<String, Object> userCost();

    Map<String, Object> avgRecharge();

    Map<String, Object> avgGuaranteeNum();

    Map<String, Object> avgEventAmt();

    Map<String, Object> toPay();

    Map<String, Object> noBanlance();

    Map<String, Object> eventNum();

    String eventNumByMonth();

    Map<String, Object> getGuaranteeNumByMonth();

    List<Map<String, Object>> eventAmt();

    List<Map<String, Object>> rechargeNum();

    String getDamagePosition();

    Map<String, Object> getCarCountByStatus();

    Map<String, Object> getUserCountByAmt();

    Map<String, Object> getRechargeCountByAmt();

    Map<String, Object> getRechargeCountByAmt12();

    Map<String, Object> getRechargeCountByAmt3();

    Map<String, Object> getRechargeCountByAmt9x();

    Map<String, Object> getRechargeCountByAmt9z();

    Map<String, Object> getTotalUserCount();
}
