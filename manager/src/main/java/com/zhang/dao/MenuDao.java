package com.zhang.dao;

import com.zhang.entity.Menu;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author 张会丽
 * @create 2019/8/6
 */
@Transactional
public interface MenuDao extends JpaRepository<Menu,Long> {
    //根据id查询menu表
    public Menu getById(Long id);
    //根据parentId查询menu
    public List<Menu> getByParentId(Long id);
   //根据roleId查询menu
    @Query(value = "select bm.* from base_role_menu brm INNER JOIN base_menu bm on brm.menuId = bm.id where brm.roleId = ?1",nativeQuery = true)
    public List<Menu> getByRoleId(Long id);
    //根据角色id查询菜单集合
    @Query(value = "SELECT * FROM base_role_menu brm INNER JOIN ( SELECT b.* FROM base_menu b, ( SELECT parentId FROM base_menu WHERE url = ?1 ) a WHERE b.parentId = a.parentId ) bm ON brm.menuId = bm.id WHERE brm.roleId = ?2",nativeQuery = true)
    public List<Menu> getByUrlAndRoleIdBrotherMenus(String url,Long roleId);

}
