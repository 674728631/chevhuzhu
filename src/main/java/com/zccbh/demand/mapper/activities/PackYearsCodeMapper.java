package com.zccbh.demand.mapper.activities;

import com.zccbh.demand.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PackYearsCodeMapper extends BaseMapper {

    /**
     * 保存ebo包年用户及车辆
     *
     * @param user
     * @return
     */
    int saveUserAndCar(Map<String, Object> user);

    /**
     * 根据code查询
     *
     * @param code
     * @return
     * @throws Exception
     */
    Map<String, Object> queryByCode(String code) throws Exception;

    /**
     * code对比验证更新
     *
     * @param customerId
     * @param carId
     * @param useStatus
     * @param uuid
     * @return
     */
    int updateCode4Use(@Param("customerId") String customerId, @Param("carId") String carId, @Param("use_status") int useStatus, @Param("uuid") String uuid);

    /**
     * @param carId
     * @param userId
     * @return
     */
    List<Map<String, Object>> getCodesByCarIdAndUserId(@Param("carId") String carId, @Param("customerId") String userId);

    /**
     * 根据用户获取所用包年活动车辆
     *
     * @param userId
     * @return
     */
    List<Map<String, Object>> getCarsByUserId(@Param("customerId") String userId);

    /**
     * 保存用户填写code
     *
     * @param codeInfo
     * @return
     */
    int saveCode(Map<String, Object> codeInfo);
}
