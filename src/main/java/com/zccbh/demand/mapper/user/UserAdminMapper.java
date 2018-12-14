package com.zccbh.demand.mapper.user;

import com.zccbh.demand.mapper.BaseMapper;
import com.zccbh.demand.pojo.user.UserAdmin;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserAdminMapper extends BaseMapper{
    int resetPassword(Map<String, Object> map) throws Exception;

    UserAdmin selectByToken(Map<String, Object> parameterModelMap);

    Map<String,Object> findLoginAdmin(Map<String, Object> parameterModelMap);
    Map<String, Object> findAdminPN();

    List<Map<String, Object>> isExist(Map<String, Object> parameterModelMap);

    Map<String,Object> homepage(String toKen);

    /**
     * 根据权限获取用户
     * @param roleIds
     * @return
     */
    List<Map<String, Object>> listAdminUserByRoles(@Param("roleIds") List<Integer> roleIds);

    /**
     * 根据用户名查询用户
     * @param parameterModelMap
     * @return
     */
    Map<String, Object> selectByUsername(Map<String, Object> parameterModelMap);
}