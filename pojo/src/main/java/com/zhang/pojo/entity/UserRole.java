package com.zhang.pojo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "base_user_role")
public class UserRole {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "roleId")
  private Long roleId;

  @Column(name = "userId")
  private Long userId;
}
