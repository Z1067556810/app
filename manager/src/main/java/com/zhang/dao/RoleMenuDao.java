package com.zhang.dao;

import com.zhang.pojo.entity.RoleMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 张会丽
 * @create 2019/8/8
 */
@Transactional
public interface RoleMenuDao extends JpaRepository<RoleMenu,Long> {
    public void  deleteByRoleId(Long roleId);
}
