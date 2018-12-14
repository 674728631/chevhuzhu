package com.zccbh.demand.service.business;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.business.MiddleBusinessMaintenanceshopMapper;
import com.zccbh.demand.mapper.business.UserBusinessMapper;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserBusinessService {
    @Autowired
    private UserBusinessMapper businessMapper;

    @Autowired
    private MiddleBusinessMaintenanceshopMapper middleBusinessMaintenanceshopMapper;

    /**
     * 添加商家用户
     * @param paramModelMap 商家用户信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String saveBusiness(Map<String, Object> paramModelMap) throws Exception{
        Map<String, Object> map = new HashMap<>();
        Integer typeUser = (Integer) paramModelMap.get("typeUser");
        if(typeUser==1){
            map.put("maintenanceshopId",paramModelMap.get("shopId"));
            List businessList = businessMapper.haveAdministrator(map);
            if(businessList.size()>0){
                return "4004";
            }
        }
        if(Constant.toEmpty(paramModelMap.get("businessUN")) && Constant.toEmpty(paramModelMap.get("businessPN"))){
            map.clear();
            map.put("businessUN",paramModelMap.get("businessUN"));
            map.put("businessPN",paramModelMap.get("businessPN"));
            List<Map<String, Object>> businessList = businessMapper.isExist(map);
            if(businessList.size()>0){
                return "4003";
            }else {
                paramModelMap.put("businessPW", MD5Util.getMD5Code((String) paramModelMap.get("businessPW")));
                businessMapper.saveSingle(paramModelMap);
                Map<String, Object> map2 = new HashMap<>();
                map2.put("businessId",paramModelMap.get("id"));
                map2.put("maintenanceshopId",paramModelMap.get("shopId"));
                middleBusinessMaintenanceshopMapper.saveSingle(map2);
                return "0";
            }
        }else {
            return "4001";
        }

    }

    /**
     * 修改商家用户
     * @param paramModelMap 商家用户信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String updateBusiness(Map<String, Object> paramModelMap) throws Exception{
        if(Constant.toEmpty(paramModelMap.get("status"))){
            Map<String, Object> map = new HashMap<>();
            map.put("id",paramModelMap.get("id"));
            map.put("status",paramModelMap.get("status"));
            businessMapper.updateModel(map);
            return "0";
        }else {
            Map<String, Object> map = new HashMap<>();
            Integer typeUser = (Integer) paramModelMap.get("typeUser");
            if(typeUser==1){
                map.put("maintenanceshopId",paramModelMap.get("shopId"));
                map.put("businessId",paramModelMap.get("id"));
                List businessList = businessMapper.haveAdministrator(map);
                if(businessList.size()>0){
                    return "4004";
                }
            }
            if(Constant.toEmpty(paramModelMap.get("businessUN")) && Constant.toEmpty(paramModelMap.get("businessPN"))){
                map.clear();
                map.put("businessUN",paramModelMap.get("businessUN"));
                map.put("businessPN",paramModelMap.get("businessPN"));
                List<Map<String, Object>> businessList = businessMapper.isExist(map);
                if(businessList.size()>1){
                    return "4003";
                }else {
                    businessMapper.updateModel(paramModelMap);
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("businessId",paramModelMap.get("id"));
                    map2.put("maintenanceshopId",paramModelMap.get("shopId"));
                    middleBusinessMaintenanceshopMapper.updateModel(map2);
                    return "0";
                }
            }else {
                return "4001";
            }
        }
    }

    /**
     * 删除商家用户
     * @param businessId 商家用户id
     * @return
     * @throws Exception
     */
    @Transactional
    public String deleteBusiness(Integer businessId) throws Exception{
        businessMapper.deleteModel(businessId);
        return "0";
    }

    /**
     * 查询商家用户
     * @param paramModelMap 查询条件
     * @return 商家用户集合
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findBusinessList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> businessList = businessMapper.findMore(paramModelMap);
        PageInfo<Map<String, Object>> businessInfo = new PageInfo<>(businessList);
        return businessInfo;
    }
}


