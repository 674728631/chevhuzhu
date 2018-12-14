package com.zccbh.demand.service.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.system.RightsMapper;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RightsService {
    @Autowired
    private RightsMapper rightsMapper;

    /**
     * 添加权限
     * @param paramModelMap 权限信息
     * @return 添加结果信息
     * @throws Exception
     */
    @Transactional
    public String saveRole(Map<String, Object> paramModelMap) throws Exception {
        rightsMapper.saveSingle(paramModelMap);
        return "0";
    }

    /**
     * 修改权限
     * @param paramModelMap 权限信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String updateRole(Map<String, Object> paramModelMap) throws Exception{
        rightsMapper.updateModel(paramModelMap);
        return "0";
    }

    /**
     * 删除权限
     * @param rightsId 权限id
     * @return
     * @throws Exception
     */
    @Transactional
    public String deleteRole(Integer rightsId) throws Exception{
        rightsMapper.deleteModel(rightsId);
        return "0";
    }

    /**
     * 查询单条权限数据
     * @param paramModelMap 查询条件
     * @return 权限信息
     * @throws Exception
     */
    public Map<String, Object> findRights(Map<String, Object> paramModelMap) throws Exception {
        Map<String, Object> rights = rightsMapper.findSingle(paramModelMap);
        return rights;
    }

    /**
     * 查询权限数据
     * @param paramModelMap 查询条件
     * @return 权限信息
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findRightsList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> rightsList = rightsMapper.findMore(paramModelMap);
        PageInfo<Map<String, Object>> rightsInfo = new PageInfo<>(rightsList);
        return rightsInfo;
    }
}


