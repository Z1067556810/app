package com.zhang.service;

import com.zhang.dao.MenuDao;
import com.zhang.dao.RoleDao;
import com.zhang.dao.UserDao;
import com.zhang.pojo.entity.Menu;
import com.zhang.pojo.entity.Role;
import com.zhang.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张会丽
 * @create 2019/8/9
 */
@Component
public class LoginService {
    @Autowired
    UserDao uDao;
    @Autowired
    RoleDao rDao;
    @Autowired
    MenuDao mDao;
    //登录
    public User login(String loginname) {
        User userInfo = uDao.findByLoginName(loginname);
        if(userInfo != null){
            Role role = rDao.role(userInfo.getId());
            userInfo.setRole(role);
            if(role != null){
                List<Menu> allMenus = mDao.getByRoleId(role.getId());
                List<Menu> parList = new ArrayList<>();
                for (Menu allMenu : allMenus) {
                    if(allMenu.getLeval() == 1){
                        parList.add(allMenu);
                    }
                }
                Map<String,String> map = new HashMap<>();
                this.getForMenuInfo(parList,map,allMenus);
                System.out.println(map);
                userInfo.setAuthmap(map);
                userInfo.setMenuList(parList);
            }
        }
        return userInfo;
    }

    public void getForMenuInfo(List<Menu> list, Map<String, String> map,List<Menu> allMenus){
        for (Menu menuInfo : list) {
            if(menuInfo.getLeval() == 4){
                map.put(menuInfo.getUrl(),"");
            }
            List<Menu> chilMenus=new ArrayList<>();
            for (Menu allMenu:allMenus){
                if (menuInfo.getId() == allMenu.getParentId()){
                    chilMenus.add(allMenu);
                }
            }
            menuInfo.setMenuList(chilMenus);
            if(menuInfo.getMenuList().size() > 0){
                getForMenuInfo(menuInfo.getMenuList(),map,allMenus);
            }
        }
    }
}