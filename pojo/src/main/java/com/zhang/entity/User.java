package com.zhang.entity;

import com.zhang.base.BaseAuditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
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

    @Column(name = "imgUrl")
    private String imgUrl;

    @Column(name = "createTime")
    private Date createTime;

    @Column(name = "updateTime")
    private Date updateTime;

    @Transient
    private List<Menu> menuList;

    @Transient
    private Role role;

    @Transient
    private Map<String,String> authmap;

}
