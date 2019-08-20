package com.zhang.service;

import com.zhang.dao.*;
import com.zhang.entity.Menu;
import com.zhang.entity.Role;
import com.zhang.entity.RoleMenu;
import com.zhang.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
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

    /**
     * 获取角色列表
     * @param map
     * @return
     */
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
            role.setUserList(uDao.getUserByRoleId(role.getId()));
        }
        return all;
    }

    /**
     * 根据id删除角色
     * @param id
     */
    public void deleteRoleById(long id){
        rDao.deleteById(id);
    }

    /**
     * 添加角色
     * @param role
     */
    public void addRole(Role role){
        rDao.save(role);
    }

    /**
     * 菜单集合
     * @return
     */
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

    /**
     * 递归获取菜单
     * @param list
     * @param menuList
     */
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

    /**
     * 绑定角色
     * @param map
     */
    public void putMenuByRoleId(Map<String,Object> map){
        if (map.get("roleId")!=null&&map.get("menuIds")!=null){
            rmDao.deleteByRoleId(Long.parseLong(map.get("roleId").toString()));
            String[] menuIds = map.get("menuIds").toString().split(",");
            for (String menuId:menuIds){
                RoleMenu roleMenu=new RoleMenu();
                roleMenu.setRoleId(Long.parseLong(map.get("roleId").toString()));
                roleMenu.setMenuId(Long.parseLong(menuId));
                rmDao.save(roleMenu);
            }
        }
    }
    public int checkedRoleName(String roleName) {
        int i = rDao.countByRoleName(roleName);
        return i;
    }
}
