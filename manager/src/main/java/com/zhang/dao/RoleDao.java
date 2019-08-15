package com.zhang.dao;

import com.zhang.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author 张会丽
 * @create 2019/8/6
 */
public interface RoleDao extends JpaRepository<Role,Long> {
    @Query(value = "select br.* from base_user_role bur INNER JOIN base_role br ON bur.roleId=br.id where bur.userId=?1",nativeQuery = true)
    public Role role(Long userId);
    @Query
    public Page<Role> getByRoleNameLike(String mohu, Pageable pageable);
    @Query(value = "select br.* from base_user_role bur INNER JOIN base_role br ON bur.roleId=br.id where bur.userId=?1",nativeQuery = true)
    Role getRoleByUserId(Long id);
    @Query(value = "select br.* from base_role_menu brm INNER JOIN base_role br on brm.roleId = br.id where brm.menuId = ?1",nativeQuery = true)
    public List<Role> getRoleByMenuId(Long id);
    public int countByRoleName(String name);
    @Query(value = "select bm.id from base_role_menu brm INNER JOIN base_menu bm on brm.menuId = bm.id where brm.roleId = ?1",nativeQuery = true)
    public List<Map<String,Long>> getMenuIdByRoleId(Long roleId);
}
