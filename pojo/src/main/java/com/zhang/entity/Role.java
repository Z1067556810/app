package com.zhang.entity;

import com.zhang.base.BaseAuditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

@Entity
@Data
@Table(name = "base_role")
public class Role extends BaseAuditable {

    @Column(name = "roleName")
    private String roleName;

    @Column(name = "miaoShu")
    private String miaoShu;

    @Transient
    private List<Menu> menuList;

    @Transient
    private List<User> userList;

    @Transient
    private String ids[];
}
