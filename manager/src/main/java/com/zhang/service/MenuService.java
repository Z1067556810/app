package com.zhang.service;

import com.zhang.dao.MenuDao;
import com.zhang.dao.RoleDao;
import com.zhang.dao.RoleMenuDao;
import com.zhang.entity.Menu;
import com.zhang.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 张会丽
 * @create 2019/8/12
 */
@Component
@Transactional
public class MenuService {

    @Autowired
    MenuDao mDao;
    @Autowired
    RoleMenuDao rmDao;
    @Autowired
    RoleDao rDao;
    //根据id删除菜单
    @Transactional
    public void deleteMenuById(String[] ids) {
        for (String id : ids) {
            rmDao.deleteByMenuId(Long.parseLong(id));
            mDao.deleteById(Long.parseLong(id));
        }

    }
    //获取此菜单的权限
    public Set<Role> getRoleByMenuId(String[] ids) {
        Set<Role> set = new HashSet<>();
        for (String id : ids) {
            List<Role> roleByMenuId = rDao.getRoleByMenuId(Long.parseLong(id));
            for (Role roleInfo : roleByMenuId) {
                set.add(roleInfo);
            }
        }
        return set;
    }

    public void addMenu(Menu menu){
        mDao.save(menu);
    }
}
