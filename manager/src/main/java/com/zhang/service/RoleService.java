package com.zhang.service;

import com.zhang.dao.*;
import com.zhang.pojo.entity.Menu;
import com.zhang.pojo.entity.Role;
import com.zhang.pojo.entity.RoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 张会丽
 * @create 2019/8/8
 */
@Component
public class RoleService {
    @Autowired
    MenuDao mDao;
    @Autowired
    RoleDao rDao;
    @Autowired
    UserDao uDao;
    @Autowired
    UserRoleDao urDao;
    @Autowired
    RoleMenuDao rmDao;
    public Page<Role> getRole(Map<String,Object> map){
        Integer page=0;
        Integer pageSize=5;
        if (map.get("page")!=null&&map.get("pageSize")!=null){
            page=Integer.parseInt(map.get("page").toString());
            pageSize=Integer.parseInt(map.get("pageSize").toString());
        }
        Page<Role> all = rDao.getByRoleNameLike("%" + map.get("mohu") + "%", PageRequest.of(page, pageSize));
        for (Role role:all){
            role.setMenuList(mDao.getByRoleId(role.getId()));
            role.setUserList(uDao.getUserInfoByRoleId(role.getId()));
        }
        return all;
    }
    public void deleteRoleById(long id){
        rDao.deleteById(id);
    }
    public void addRole(Role role){
        rDao.save(role);
    }
    public List<Menu> getAllMenu(){
        List<Menu> all = mDao.findAll();
        List<Menu> menus=new ArrayList<>();
        for (Menu menu:all){
            if (menu.getLeval()==1){
                menus.add(menu);
            }
        }
        this.getForMenu(menus,all);
        return menus;
    }
    public void getForMenu(List<Menu> list,List<Menu> menuList){
        for (Menu menu:list){
            List<Menu> menus=new ArrayList<>();
            for (Menu menu1:menuList){
                if (menu.getId()==menu1.getParentId()){
                    menus.add(menu1);
                }
            }
            menu.setMenuList(menus);
            menus=new ArrayList<>();
            if (menu.getMenuList().size()>0){
                getForMenu(menu.getMenuList(),menuList);
            }
        }
    }
    public void putMenuByRoleId(Map<String,Object> map){
        if (map.get("roleId")!=null&&map.get("menuId")!=null){
            rmDao.deleteByRoleId(Long.parseLong(map.get("roleId").toString()));
            String[] menuIds = map.get("menuId").toString().split(",");
            for (String menuId:menuIds){
                RoleMenu roleMenu=new RoleMenu();
                roleMenu.setRoleId(Long.parseLong(map.get("roleId").toString()));
                roleMenu.setRoleId(Long.parseLong(menuId));
                rmDao.save(roleMenu);
            }
        }
    }
}
