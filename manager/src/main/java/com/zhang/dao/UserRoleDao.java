package com.zhang.dao;

import com.zhang.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * @author 张会丽
 * @create 2019/8/8
 */
@Transactional
public interface UserRoleDao extends JpaRepository<UserRole,Long> {
    //根据用户id删除角色
    public void deleteByUserId(Long userId);
}
