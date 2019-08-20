package com.zhang.entity;

import com.zhang.base.BaseAuditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "base_user")
public class User extends BaseAuditable {

    @Column(name = "userName")
   private String userName;

    @Column(name = "loginName")
   private String loginName;

    @Column(name = "password")
   private String password;

    @Column(name = "tel")
   private String tel;

    @Column(name = "sex")
   private int sex;

    @Column(name = "parentId")
    private Long parentId;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private String code;

    @Transient
    private List<Menu> menuList;

    @Transient
    private String roleName;
    //实体类中使用了@Table注解后，想要添加表中不存在的字段，
    // 就要使用@Transient这个注解了。
    @Transient
    private Role role;
    @Transient
    private List<Role> roleList;
    @Transient
    private Map<String,String> authmap;
    @Transient
    private Object[] dengnumkey;//天数信息
    @Transient
    private Object[] dengnumvalues;//每天登录数
}
