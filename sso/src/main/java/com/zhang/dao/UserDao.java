package com.zhang.dao;

import com.zhang.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author 张会丽
 * @create 2019/8/9
 */
public interface UserDao extends JpaRepository<User,Long> {

    //登录
    @Query(value = "select * from base_user where loginName=?1",nativeQuery = true)
    public User findByLoginName(String loginName);

    //根据id查询用户信息
    @Query(value = "select * from base_user where id=?1",nativeQuery = true)
    public Map<String,Object> getUserById(String userId);

    //根据手机号查询用户
    @Query(value = "select * from base_user where tel=?1",nativeQuery = true)
    public User findUserByTel(String tel);

    //根据邮箱查询用户
    @Query(value = "select * from base_user where email=?1",nativeQuery = true)
    public User findUserByEmail(String email);

    //根据code码查询用户
    @Query(value = "select * from base_user where code=?1",nativeQuery = true)
    public User findUserByCode(String code);

    //根据code码修改密码
    @Transactional
    @Modifying
    @Query(value = "update base_user set password=?1 where code=?2",nativeQuery = true)
    public void updatePasswordByCode(String password,String code);


    //根据用户id修改code码
    @Transactional
    @Modifying
    @Query(value = "update base_user set code=?1 where id=?2",nativeQuery = true)
    public void updateCodeById(String code,Long id);

}
