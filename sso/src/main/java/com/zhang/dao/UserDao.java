package com.zhang.dao;

import com.zhang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 张会丽
 * @create 2019/8/9
 */
public interface UserDao extends JpaRepository<User,Long> {

    @Query(value = "select * from base_user where loginName=?1",nativeQuery = true)
    public User findByLoginName(String loginName);

}
