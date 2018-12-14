package com.zccbh.demand.service.system;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.system.RightsMapper;
import com.zccbh.demand.mapper.system.RoleMapper;
import com.zccbh.util.collect.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RightsMapper rightsMapper;

    /**
     * 添加角色
     * @param paramModelMap 角色信息
     * @return 添加结果信息
     * @throws Exception
     */
    @Transactional
    public String saveRole(Map<String, Object> paramModelMap) throws Exception {
        roleMapper.saveSingle(paramModelMap);
        Map map = new HashMap();
        map.put("roleId",paramModelMap.get("id"));
        map.put("rightsMenu",paramModelMap.get("rightsMenu"));
        rightsMapper.saveSingle(map);
        return "0";
    }

    /**
     * 修改角色
     * @param paramModelMap 角色信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String updateRole(Map<String, Object> paramModelMap) throws Exception{
        roleMapper.updateModel(paramModelMap);
        Map map = new HashMap();
        map.put("roleId",paramModelMap.get("id"));
        map.put("rightsMenu",paramModelMap.get("rightsMenu"));
        rightsMapper.updateByRoleId(map);
        return "0";
    }

    /**
     * 删除角色
     * @param roleId 角色id
     * @return
     * @throws Exception
     */
    @Transactional
    public String deleteRole(Integer roleId) throws Exception{
        roleMapper.deleteModel(roleId);
        return "0";
    }

    /**
     * 查询角色数据
     * @param paramModelMap 查询条件
     * @return 角色信息
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findRoleList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> roleList = roleMapper.findMore(paramModelMap);
        PageInfo<Map<String, Object>> roleInfo = new PageInfo<>(roleList);
        return roleInfo;
    }
}


