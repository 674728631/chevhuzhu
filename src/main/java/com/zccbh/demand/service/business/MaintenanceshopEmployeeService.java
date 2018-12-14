package com.zccbh.demand.service.business;

import com.zccbh.demand.mapper.business.MaintenanceshopEmployeeMapper;
import com.zccbh.util.base.CommonField;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MaintenanceshopEmployeeService {
    @Autowired
    private MaintenanceshopEmployeeMapper employeeMapper;

    /**
     * 添加维修人员
     * @param paramModelMap 维修人员信息
     * @return 添加结果信息
     * @throws Exception
     */
    @Transactional
    public String saveEmployee(Map<String, Object> paramModelMap) throws Exception {
        employeeMapper.saveSingle(paramModelMap);
        return "0";
    }

    /**
     * 修改维修人员
     * @param paramModelMap 维修人员信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String updateEmployee(Map<String, Object> paramModelMap) throws Exception{
        employeeMapper.updateModel(paramModelMap);
        return "0";
    }

    /**
     * 删除维修人员
     * @param adminId 维修人员id
     * @return
     * @throws Exception
     */
    @Transactional
    public String deleteEmployee(Integer adminId) throws Exception{
        employeeMapper.deleteModel(adminId);
        return "0";
    }

    /**
     * 查询维修团队数据
     * @param paramModelMap 查询条件
     * @return 维修团队信息
     * @throws Exception
     */
    public List<Map<String, Object>> findEmployeeList(Map<String, Object> paramModelMap) throws Exception {
        return employeeMapper.findMore(paramModelMap);
    }

    /**
     * 查询维修团队数据
     * @param paramModelMap 查询条件
     * @return 维修团队信息
     * @throws Exception
     */
    public Map<String, Object> findEmployeeDetail(Map<String, Object> paramModelMap) throws Exception {
        Map<String, Object> employeeDetail = employeeMapper.findSingle(paramModelMap);
        if(Constant.toEmpty(employeeDetail.get("img"))){
            employeeDetail.put("img", CommonField.getMaintenanceShopImgList(2,(String) employeeDetail.get("img")));
        }
        return employeeDetail;
    }
}


