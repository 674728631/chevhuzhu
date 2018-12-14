package com.zccbh.demand.service.system;

import com.zccbh.demand.mapper.system.MenuMapper;
import com.zccbh.demand.pojo.system.Menu;
import com.zccbh.util.uploadImg.UploadFileUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MenuService {
    @Autowired
    private MenuMapper menuMapper;

    /**
     * 查询菜单
     * @param paramModelMap 查询条件
     * @return 菜单数据
     * @throws Exception
     */
    public List<Map<String, Object>> findMenuList(Map<String, Object> paramModelMap) throws Exception {
        List<Menu> menuList = menuMapper.findEntityMore(paramModelMap);
        List<Menu> oneMenu = new ArrayList<>();
        List<Menu> twoMenu = new ArrayList<>();
        //分化一级菜单和二级菜单
        for (Menu menu: menuList) {
            if(menu.getGrade() == 1 && !(menu.getName().equals("其他"))){
                oneMenu.add(menu);
            }
            if(menu.getGrade() == 2){
                twoMenu.add(menu);
            }
        }
        //归类二级菜单
        List<Map<String,Object>> reList = new ArrayList<Map<String,Object>>();
        for (Menu menu: oneMenu) {
            Map<String,Object> map = new HashMap<>();
            List<Menu> childMenu = new ArrayList<>();
            for (Menu menu2: twoMenu) {
                if(menu2.getParentId() == menu.getId())
                    childMenu.add(menu2); 
//                menu2.setIcon(UploadFileUtil.getImgURL("icon/", menu2.getIcon()));
            }
            map.put("parentMenu",menu);
            map.put("childMenu",childMenu);
            reList.add(map);
        }
        return reList;
    }
    
    /**
     * 查询菜单
     * @param paramModelMap 查询条件
     * @return 菜单数据
     * @throws Exception
     */
    public List<Map<String, Object>> findAppMenuList(Map<String, Object> paramModelMap) throws Exception {
        List<Menu> menuList = menuMapper.findEntityMore(paramModelMap);
        List<Menu> oneMenu = new ArrayList<>();
        List<Menu> twoMenu = new ArrayList<>();
        //分化一级菜单和二级菜单
        for (Menu menu: menuList) {
            if(menu.getGrade() == 1 && (menu.getName().equals("订单") || menu.getName().equals("其他"))){
                oneMenu.add(menu);
            }
            if(menu.getGrade() == 2){
                twoMenu.add(menu);
            }
        }
        //归类二级菜单
        List<Map<String,Object>> reList = new ArrayList<Map<String,Object>>();
        for (Menu menu: oneMenu) {
            Map<String,Object> map = new HashMap<>();
            List<Menu> childMenu = new ArrayList<>();
            for (Menu menu2: twoMenu) {
                if(menu2.getParentId() == menu.getId() && (menu.getName().equals("订单") || menu.getName().equals("其他")) ){
                	   menu2.setIcon(UploadFileUtil.getImgURL("icon/", menu2.getIcon()));
                       childMenu.add(menu2);
                }
            }
            map.put("parentMenu",menu);
            map.put("childMenu",childMenu);
            reList.add(map);
        }
        return reList;
    }
}


