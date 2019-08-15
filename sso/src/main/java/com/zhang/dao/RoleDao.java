package com.zhang.dao;

import com.zhang.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author 张会丽
 * @create 2019/8/9
 */
public interface RoleDao extends JpaRepository<Role,Long> {
    @Query(value = "select br.* from base_user_role bur INNER JOIN base_role br ON bur.roleId=br.id where bur.userId=?1",nativeQuery = true)
    public Role role(Long userId);
}
