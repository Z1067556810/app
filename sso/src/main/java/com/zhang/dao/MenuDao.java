package com.zhang.dao;

import com.zhang.pojo.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 张会丽
 * @create 2019/8/9
 */
public interface MenuDao extends JpaRepository<Menu,Long> {

    @Query(value = "select bm.* from base_role_menu brm inner join base_menu bm on brm.menuId = bm.id where brm.roleId = ?1 and bm.leval = ?2",nativeQuery = true)
    List<Menu> getFirstMenuInfo(Long id, int leval);
    @Query(value = "select bm.* from base_role_menu brm inner join base_menu bm on brm.menuId = bm.id where brm.roleId = ?1",nativeQuery = true)
    List<Menu> getByRoleId(Long id);
}