package com.zhang.dao;

import com.zhang.entity.Menu;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 张会丽
 * @create 2019/8/6
 */
public interface MenuDao extends JpaRepository<Menu,Long> {
    public Menu getById(Long id);

    @Query(value = "select bm.* from base_role_menu brm inner join base_menu bm on brm.menuId = bm.id where brm.roleId = ?1", nativeQuery = true)
    List<Menu> getByRoleId(Long roleId);
    public List<Menu> getByParentId(Long parentId);
    @Query(value = "select bm.* from base_role_menu brm inner join base_menu bm on brm.menuId = bm.id where brm.roleId = ?1 and bm.leval = ?2", nativeQuery = true)
    List<Menu> menu(Long id, int i);
    @Query(value = "select * from base_menu menuName like concat ('%',?1,'%')", nativeQuery = true)
    public List<Menu> findByMenuName(String menuName, Pageable pageable);
    public int countByMenuName(String name);
    public List<Menu> getByUrl(String url);
    @Query(value = "SELECT * FROM base_role_menu brm INNER JOIN ( SELECT b.* FROM base_menu b, ( SELECT parentId FROM base_menu WHERE url = ?1 ) a WHERE b.parentId = a.parentId ) bm ON brm.menuId = bm.id WHERE brm.roleId = ?2",nativeQuery = true)
    public List<Menu> getByUrlAndRoleIdBrotherMenus(String url,Long roleId);
}
