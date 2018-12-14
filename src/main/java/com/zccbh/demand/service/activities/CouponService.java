package com.zccbh.demand.service.activities;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.activities.CouponMapper;
import com.zccbh.demand.mapper.activities.CouponModelMapper;
import com.zccbh.demand.mapper.activities.MiddleCouponCustomerMapper;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.qrcode.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class CouponService {
    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponModelMapper couponModelMapper;

    @Autowired
    private MiddleCouponCustomerMapper middleCouponCustomerMapper;

    /**
     * 创建一个活动
     *
     * @param paramModelMap 活动信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String saveCoupon(Map<String, Object> paramModelMap) throws Exception {
        //判断输入的面值是否合法
        if (Constant.toEmpty(paramModelMap.get("amount"))) {
            try {
                BigDecimal amount = new BigDecimal((String) paramModelMap.get("amount"));
                if (amount.compareTo(new BigDecimal(0)) < 0) {
                    return "4001";
                }
                paramModelMap.put("amount", new BigDecimal((String) paramModelMap.get("amount")));
            } catch (Exception e) {
                return "4001";
            }
        }
        if (Constant.toEmpty(paramModelMap.get("meetPrice"))) {
            try {
                BigDecimal meetPrice = new BigDecimal((String) paramModelMap.get("meetPrice"));
                if (meetPrice.compareTo(new BigDecimal(0)) < 0) {
                    return "4001";
                }
                paramModelMap.put("meetPrice", new BigDecimal((String) paramModelMap.get("meetPrice")));
            } catch (Exception e) {
                return "4001";
            }
        }
        paramModelMap.put("couponNo", Constant.createCouponNo());
        paramModelMap.put("surplusNum", paramModelMap.get("num"));
        couponMapper.saveSingle(paramModelMap);
        String activtyUrl = Constant.toReadPro("address1") + "?sid=" + paramModelMap.get("shopId") + "&cid=" + paramModelMap.get("couponNo");
        //创建活动二维码
        String qrcodeName = QRCodeUtil.saveQrcode(activtyUrl, (String) paramModelMap.get("logoPath"), "activity/addressQrcode/");
        //保存活动二维码
        paramModelMap.put("qrcode", qrcodeName);
        couponMapper.updateModel(paramModelMap);
        return "0";
    }

    /**
     * 修改活动
     *
     * @param paramModelMap 活动信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String updateCoupon(Map<String, Object> paramModelMap) throws Exception {
        //判断输入的面值是否合法
        if (Constant.toEmpty(paramModelMap.get("amount"))) {
            try {
                BigDecimal amount = new BigDecimal((String) paramModelMap.get("amount"));
                if (amount.compareTo(new BigDecimal(0)) < 0) {
                    return "4001";
                }
                paramModelMap.put("amount", new BigDecimal((String) paramModelMap.get("amount")));
            } catch (Exception e) {
                return "4001";
            }
        }
        if (Constant.toEmpty(paramModelMap.get("meetPrice"))) {
            try {
                BigDecimal meetPrice = new BigDecimal((String) paramModelMap.get("meetPrice"));
                if (meetPrice.compareTo(new BigDecimal(0)) < 0) {
                    return "4001";
                }
                paramModelMap.put("meetPrice", new BigDecimal((String) paramModelMap.get("meetPrice")));
            } catch (Exception e) {
                return "4001";
            }
        }
        couponMapper.updateModel(paramModelMap);
        return "0";
    }

    /**
     * 删除活动
     *
     * @param paramModelMap 活动信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String deleteCoupon(Map<String, Object> paramModelMap) throws Exception {
        couponMapper.updateModel(paramModelMap);
        return "0";
    }

    /**
     * 启用一个活动
     *
     * @param paramModelMap 活动模板信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String startCoupon(Map<String, Object> paramModelMap) throws Exception {
        couponMapper.updateModel(paramModelMap);
        return "0";
    }

    /**
     * 停用一个活动
     *
     * @param paramModelMap 活动模板信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String endCoupon(Map<String, Object> paramModelMap) throws Exception {
        couponMapper.updateModel(paramModelMap);
        return "0";
    }

    /**
     * 查询所有活动数据
     *
     * @param paramModelMap 查询条件
     * @return 活动集合
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findCouponList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> couponList = couponMapper.findMore(paramModelMap);
        return new PageInfo<>(couponList);
    }

    /**
     * 加载活动详情数据
     *
     * @param paramModelMap 查询条件
     * @return 活动详情
     * @throws Exception
     */
    public Map<String, Object> findCouponDetail(Map<String, Object> paramModelMap) throws Exception {
        Map<String, Object> couponDetail = couponMapper.findSingle(paramModelMap);
        return couponDetail;
    }

    /**
     * 创建一个活动模板
     *
     * @param paramModelMap 活动模板信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String saveCouponModel(Map<String, Object> paramModelMap) throws Exception {
        //判断输入的面值是否合法
        if (Constant.toEmpty(paramModelMap.get("amount"))) {
            try {
                BigDecimal amount = new BigDecimal((String) paramModelMap.get("amount"));
                if (amount.compareTo(new BigDecimal(0)) < 0) {
                    return "4001";
                }
                paramModelMap.put("amount", new BigDecimal((String) paramModelMap.get("amount")));
            } catch (Exception e) {
                return "4001";
            }
        }
        if (Constant.toEmpty(paramModelMap.get("meetPrice"))) {
            try {
                BigDecimal meetPrice = new BigDecimal((String) paramModelMap.get("meetPrice"));
                if (meetPrice.compareTo(new BigDecimal(0)) < 0) {
                    return "4001";
                }
                paramModelMap.put("meetPrice", new BigDecimal((String) paramModelMap.get("meetPrice")));
            } catch (Exception e) {
                return "4001";
            }
        }
        couponModelMapper.saveSingle(paramModelMap);
        return "0";
    }

    /**
     * 修改活动模板
     *
     * @param paramModelMap 活动模板信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String updateCouponModel(Map<String, Object> paramModelMap) throws Exception {
        //判断输入的面值是否合法
        if (Constant.toEmpty(paramModelMap.get("amount"))) {
            try {
                BigDecimal amount = new BigDecimal((String) paramModelMap.get("amount"));
                if (amount.compareTo(new BigDecimal(0)) < 0) {
                    return "4001";
                }
                paramModelMap.put("amount", new BigDecimal((String) paramModelMap.get("amount")));
            } catch (Exception e) {
                return "4001";
            }
        }
        if (Constant.toEmpty(paramModelMap.get("meetPrice"))) {
            try {
                BigDecimal meetPrice = new BigDecimal((String) paramModelMap.get("meetPrice"));
                if (meetPrice.compareTo(new BigDecimal(0)) < 0) {
                    return "4001";
                }
                paramModelMap.put("meetPrice", new BigDecimal((String) paramModelMap.get("meetPrice")));
            } catch (Exception e) {
                return "4001";
            }
        }
        couponModelMapper.updateModel(paramModelMap);
        return "0";
    }

    /**
     * 删除活动模板
     *
     * @param paramModelMap 活动模板信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String deleteCouponModel(Map<String, Object> paramModelMap) throws Exception {
        couponModelMapper.updateModel(paramModelMap);
        return "0";
    }

    /**
     * 查询所有活动模板数据
     *
     * @param paramModelMap 查询条件
     * @return 活动模板集合
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findCouponModelList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo")) ? Integer.parseInt(paramModelMap.get("pageNo").toString()) : 1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize")) ? Integer.parseInt(paramModelMap.get("pageSize").toString()) : 10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> couponModelList = couponModelMapper.findMore(paramModelMap);
        return new PageInfo<>(couponModelList);
    }

    /**
     * 加载活动模板详情数据
     *
     * @param paramModelMap 查询条件
     * @return 模板详情
     * @throws Exception
     */
    public Map<String, Object> findModelDetail(Map<String, Object> paramModelMap) throws Exception {
        Map<String, Object> modelDetail = couponModelMapper.findSingle(paramModelMap);
        return modelDetail;
    }

    @Transactional
    public int insertCoupon(Map<String, Object> paramModelMap) throws Exception {
        return couponMapper.saveSingle(paramModelMap);
    }

    /**
     * 通过活动模板id找到活动详情
     */
    public Map<String, Object> selectByModelId(Map<String, Object> paramModelMap) throws Exception {
        Map<String, Object> coupon = couponMapper.selectByModelId(paramModelMap);
        return coupon;
    }

    /**
     * 获取用户邀请活动编码
     *
     * @return
     */
    public int getUserInvitationModelId() {
        List<Map<String, Object>> list = couponModelMapper.selectUserCoupon();
        if (!CollectionUtils.isEmpty(list)) {
            return (int) list.get(0).get("modelId");
        }
        return -1;
    }

    /**
     * 根据shopid获取活动
     *
     * @return
     */
    public String getShopCouponNo(Map<String, Object> param) throws Exception {
        List<Map<String, Object>> list = couponMapper.findMore(param);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0).get("couponNo").toString();
        }
        return "-1";
    }
}


