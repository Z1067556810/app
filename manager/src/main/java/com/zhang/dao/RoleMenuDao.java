package com.zhang.dao;

import com.zhang.entity.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

/**
 * @author 张会丽
 * @create 2019/8/8
 */
@Transactional
public interface RoleMenuDao extends JpaRepository<RoleMenu,Long> {
    //根据角色id删除
    public void deleteByRoleId(Long roleId);
    //根据菜单id删除
    public void deleteByMenuId(Long menuId);
}
