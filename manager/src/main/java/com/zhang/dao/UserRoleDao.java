package com.zhang.dao;

import com.zhang.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 张会丽
 * @create 2019/8/8
 */
@Transactional
public interface UserRoleDao extends JpaRepository<UserRole,Long> {
    public void deleteByUserId(Long userId);
}
