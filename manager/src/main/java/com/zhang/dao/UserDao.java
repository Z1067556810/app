package com.zhang.dao;

import com.zhang.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 张会丽
 * @create 2019/8/5
 */
public interface UserDao extends JpaRepository<User,Long> {
    public Page<User> findByUserNameLike(String mohu, Pageable pageable);
    //根据登录名查询
    @Query(value = "select * from base_user where loginName=?1",nativeQuery = true)
    public User findByLoginName(String loginName);
    //角色id查用户
    @Query(value = "select bu.* from base_user_role bur INNER JOIN base_user bu ON bur.userId = bu.id where bur.roleId = ?1",nativeQuery = true)
    public List<User> getUserByRoleId(Long roleId);
    public int countByLoginName(String name);
}
