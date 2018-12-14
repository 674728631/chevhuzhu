package com.zccbh.demand.service.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zccbh.demand.mapper.system.RightsMapper;
import com.zccbh.demand.mapper.user.UserAdminMapper;
import com.zccbh.util.base.MapUtil;
import com.zccbh.util.base.SmsDemo;
import com.zccbh.util.collect.Constant;
import com.zccbh.util.collect.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserAdminService {
    @Autowired
    private UserAdminMapper adminMapper;

    @Autowired
    private RightsMapper rightsMapper;

    /**
     * 添加管理员
     * @param paramModelMap 管理员用户信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String saveAdmin(Map<String, Object> paramModelMap) throws Exception{
        if(Constant.toEmpty(paramModelMap.get("adminUN")) && Constant.toEmpty(paramModelMap.get("adminPN"))){
            Map<String, Object> map = new HashMap<>();
            map.put("adminUN",paramModelMap.get("adminUN"));
            map.put("adminPN",paramModelMap.get("adminPN"));
            List<Map<String, Object>> adminList = adminMapper.isExist(map);
            if(adminList.size()>0){
                return "4003";
            }else {
                paramModelMap.put("adminPW", MD5Util.getMD5Code((String) paramModelMap.get("adminPW")));
                adminMapper.saveSingle(paramModelMap);
                return "0";
            }
        }else {
            return "4001";
        }
    }

    /**
     * 重置密码
     */
    @Transactional
    public Map<String,Object> resetPassword(Map<String, Object> paramModelMap) throws Exception{
        Map resultMap = new HashMap();
        if(Constant.toEmpty(paramModelMap.get("adminPW")) && Constant.toEmpty(paramModelMap.get("adminPN"))){
            List<Map<String, Object>> adminList = adminMapper.isExist(paramModelMap);
            if(adminList.size()>0){
                paramModelMap.put("adminPW", MD5Util.getMD5Code((String) paramModelMap.get("adminPW")));
                adminMapper.resetPassword(paramModelMap);
                resultMap.put("code","0");
                resultMap.put("message","SUCCESSFUL!");
                return resultMap;
            }else {
                resultMap.put("code","4002");
                resultMap.put("message","该用户不存在");
                return resultMap;
            }
        }else {
            resultMap.put("code","4001");
            resultMap.put("message","密码不能为空");
            return resultMap;
        }
    }

    /**
     * 修改管理员
     * @param paramModelMap 管理员用户信息
     * @return
     * @throws Exception
     */
    @Transactional
    public String updateAdmin(Map<String, Object> paramModelMap) throws Exception{
        if(Constant.toEmpty(paramModelMap.get("status"))){
            Map<String, Object> map = new HashMap<>();
            map.put("id",paramModelMap.get("id"));
            map.put("status",paramModelMap.get("status"));
            adminMapper.updateModel(map);
            return "0";
        }else {
            if(Constant.toEmpty(paramModelMap.get("adminUN")) && Constant.toEmpty(paramModelMap.get("adminPN"))){
                Map<String, Object> map = new HashMap<>();
                map.put("adminUN",paramModelMap.get("adminUN"));
                map.put("adminPN",paramModelMap.get("adminPN"));
                List<Map<String, Object>> adminList = adminMapper.isExist(map);
                if(adminList.size()>1){
                    return "4003";
                }else {
                    adminMapper.updateModel(paramModelMap);
                    return "0";
                }
            }else {
                return "4001";
            }
        }
    }

    /**
     * 删除管理员
     * @param adminId 管理员用户id
     * @return
     * @throws Exception
     */
    @Transactional
    public String deleteAdmin(Integer adminId) throws Exception{
        adminMapper.deleteModel(adminId);
        return "0";
    }

    /**
     * 管理员端登录认证
     * @param parameterModelMap 登录信息
     * @return 用户信息
     * @throws Exception
     */
    public Map<String,Object> findLoginAdmin(Map<String, Object> parameterModelMap) throws Exception {
        return adminMapper.findLoginAdmin(parameterModelMap);
    }

    /**
     * 查询管理员
     * @param paramModelMap 查询条件
     * @return 管理员用户集合
     * @throws Exception
     */
    public PageInfo<Map<String, Object>> findAdminList(Map<String, Object> paramModelMap) throws Exception {
        int pageNo = Constant.toEmpty(paramModelMap.get("pageNo"))?Integer.parseInt(paramModelMap.get("pageNo").toString()):1;
        int pageSize = Constant.toEmpty(paramModelMap.get("pageSize"))?Integer.parseInt(paramModelMap.get("pageSize").toString()):10;
        PageHelper.startPage(pageNo, pageSize);
        List<Map<String, Object>> adminList = adminMapper.findMore(paramModelMap);
        PageInfo<Map<String, Object>> adminInfo = new PageInfo<>(adminList);
        return adminInfo;
    }

    /**
     * 查询拥有;保险理赔和互助理赔权限的后台用户
     * @param type 保险理赔:order;互助理赔:event
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> findAdminListByEventAndOrderRights(String type) throws Exception {
        List<Map<String, Object>> rightsList = rightsMapper.findMore(MapUtil.build().over());
        if (rightsList == null || rightsList.size() == 0){
            return null;
        }
        List<Integer> roleIds = new ArrayList<>();
        for (Map<String, Object> right : rightsList){
            if (Constant.toEmpty(right.get("rightsMenu"))){
                String rightsMenu = (String) right.get("rightsMenu");
                List<String> rightsMenuList = Arrays.asList(rightsMenu.split(","));
                // 互助理赔:4,保险理赔:5的权限id(4,5)
                // 互助理赔
                if ("event".equals(type)){
                    if (rightsMenuList.contains("4")){
                        roleIds.add((Integer) right.get("id"));
                    }
                    // 保险理赔
                } else if ("order".equals(type)){
                    if (rightsMenuList.contains("5")){
                        roleIds.add((Integer) right.get("id"));
                    }
                }
            }
        }
        if (rightsList.size() == 0){
            return null;
        }
        List<Map<String, Object>> result = adminMapper.listAdminUserByRoles(roleIds);
        return result;
    }

    /**
     * 给管理端app推送消息(拥有互助理赔和保险理赔)
     * @param type type 保险理赔:order;互助理赔:event
     * @param title 消息的标题
     * @param content 消息的内容
     * @param parameterMap 传递的参数
     * @throws Exception
     */
    public void pushMessageToManager(String type, String title, String content, Map<String, Object> parameterMap ) throws Exception {
        List<Map<String, Object>> allUsers = findAdminListByEventAndOrderRights(type);
        if (allUsers == null || allUsers.size() == 0){
            return ;
        }
        List<String> iosDevicesList = new ArrayList<>();
        List<String> androidDevicesList = new ArrayList<>();
        for (Map<String, Object> user : allUsers){
            if (Constant.toEmpty(user.get("iosDeviceId"))){
                iosDevicesList.add((String)user.get("iosDeviceId"));
            }
            if (Constant.toEmpty(user.get("androidDeviceId"))){
                androidDevicesList.add((String)user.get("androidDeviceId"));
            }
        }
        String iosDevices = StringUtils.join(iosDevicesList,",");
        String androidDevices = StringUtils.join(androidDevicesList,",");
        // 推送消息
        if (Constant.toEmpty(iosDevices)){
            System.out.println("IOS推送的设备id为:" + iosDevices);
            SmsDemo.mobilePushMessageForManager(10, title,iosDevices, content, parameterMap);
        }
        if (Constant.toEmpty(androidDevices)){
            System.out.println("Android推送的设备id为:" + androidDevices);
            SmsDemo.mobilePushMessageForManager(20, title,androidDevices, content, parameterMap);
        }

    }

    /**
     * 根据用户名查询用户
     * @param parameterModelMap
     * @return
     */
    public Map<String, Object> selectByUsername(Map<String, Object> parameterModelMap) {
        return adminMapper.selectByUsername(parameterModelMap);
    }
}


