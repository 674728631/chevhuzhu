package com.zccbh.demand.mapper.customer;

import com.zccbh.demand.mapper.BaseMapper;
import com.zccbh.demand.pojo.user.Car;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CarMapper extends BaseMapper {
    int updateCarStatus() throws Exception;

    int updateCarAmt(Map<String, Object> map) throws Exception;

    int updateAmt1(Map<String, Object> map) throws Exception;

    int updateAmt2(Map<String, Object> map) throws Exception;

    int findCarCountByStatus(Map<String, Object> map);

    /**
     * @author xiaowuge
     * @date 2018年9月5日
     * @version 1.0
     * 根据车牌号查询车辆
     */
    Car getCarByLicensePlateNumber(String licensePlateNumber);

    /**
     * @author xiaowuge
     * @date 2018年9月5日
     * @version 1.0
     * <p>
     * 根据用户电话和车牌号查询车辆信息
     */
    Map<String, Object> getCarDetail(Map<String, Object> pMap);

    Map<String, Object> getChemamaCarByMobile(@Param("customerPN") String customerPN);

    Map<String, Object> findUpdateCount();

    Map<String, Object> findCarByRecordRechargeId(@Param("id") String id);

    Map<String, Object> findEventByRecordRechargeId(@Param("id") String id);

    Map<String, Object> findCarNumByUserId(@Param("customerId") int customerId);

    Map<String, Object> findHelpCount(Map<String, Object> map);

    Map<String, Object> findCount(Map<String, Object> paramModelMap);

    Map<String, Object> findCarInfo(Map<String, Object> map);

    Map<String, Object> findCarByEventNo(String eventNo);

    Map<String, Object> findCarById(Integer carId);

    Map<String, Object> findOne(Map<String, Object> map);

    Map<String, Object> findTotalCooperationByCustomerId(Map<String, Object> map);

    Map<String, Object> findExpenditure(Map<String, Object> map);

    Map<String, Object> findCarAmtShare(Map<String, Object> map);

    List<Map<String, Object>> getCarList(Integer customerId);

    List<Map<String, Object>> findOutList();

    List<Map<String, Object>> findCarList(Map<String, Object> map);

    List<Map<String, Object>> findCarList2(Map<String, Object> map);

    List<Map<String, Object>> findCarList3(Map<String, Object> map);

    List<Map<String, Object>> findPayCar(Map<String, Object> map);

    List<Map<String, Object>> findUnPaymentList(@Param("status") int status);

    List<Map<String, Object>> findCarOfCustomer(Map<String, Object> map);

    List<Map<String, Object>> findCarAmtShareGroupByMonth(Map<String, Object> map);

    List<Map<String, Object>> findCardetail(Map<String, Object> map);

    List<Map<String, Object>> chartGuarantee(Map<String, Object> map);

    List<Map<String, Object>> checkChemamaCarById(@Param("carId") String carId);

    /**
     * 将车辆修改为不可用状态
     *
     * @param paramModelMap
     */
    void modifyCarUnavailable(Map<String, Object> paramModelMap);

    /**
     * 根据车牌号查找
     *
     * @param over
     * @return
     */
    Map<String, Object> findCarByLicensePlateNumber(Map<String, Object> over);

    /**
     * 渠道拉新统计
     *
     * @param parameModelMap
     * @return
     */
    List<Map<String, Object>> channelData(Map<String, Object> parameModelMap);

    /**
     * 查询退出计划，方式未发送消息的车辆
     *
     * @author xiaowuge
     * @date 2018年9月14日
     * @version 1.0
     */
//	List<Map<String, Object>> findExitList();

    Map<String, Object> getCarByCustomerId(Map<String, Object> map);

    Map<String, Object> findCar(Map<String, Object> map);

    List<Map<String, Object>> selectCarAndUser();

    Map<String, Object> selectUserByCarId(Map<String, Object> map);

    /**
     * 统计邀请活动的数据
     *
     * @param parameModelMap
     * @return
     */
    Map<String, Object> countInvitedNumbers(Map<String, Object> parameModelMap);

    /**
     * 找到第一辆车
     *
     * @author xiaowuge
     * @date 2018年10月29日
     * @version 1.0
     */
    Map<String, Object> findFirstCar(Map<String, Object> parameModelMap);

    /**
     * 观察期车辆
     *
     * @param parameModelMap
     * @return
     */
    List<Map<String, Object>> getStatus13Cars(Map<String, Object> parameModelMap);

    /**
     * 保障中车辆
     *
     * @param parameModelMap
     * @return
     */
    List<Map<String, Object>> getStatus20Cars(Map<String, Object> parameModelMap);

    /**
     * 退出车辆
     *
     * @param parameModelMap
     * @return
     */
    List<Map<String, Object>> getStatus30Cars(Map<String, Object> parameModelMap);

    /**
     * 更新理赔次数
     *
     * @author xiaowuge
     * @date 2018年11月29日
     * @version 1.0
     */
    int updateCompensateNum(Map<String, Object> map);

    int updateCar4Year(@Param("id") int carId, @Param("newAmtCooperation") BigDecimal amtCooperation,
                       @Param("oldAmtCooperation") BigDecimal amtCooperation2,
                       @Param("telCarOwner") String telCarOwner, @Param("status") int status);
}