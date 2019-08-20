package com.zhang.dao;

import com.zhang.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 张会丽
 * @create 2019/8/9
 */
public interface MenuDao extends JpaRepository<Menu,Long> {
    //根据角色id查询菜单
    @Query(value = "select bm.* from base_role_menu brm inner join base_menu bm on brm.menuId = bm.id where brm.roleId = ?1",nativeQuery = true)
    List<Menu> getByRoleId(Long id);
}
