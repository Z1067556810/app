package com.zhang.service;

import com.zhang.dao.MenuDao;
import com.zhang.dao.RoleDao;
import com.zhang.dao.UserDao;
import com.zhang.dao.UserRoleDao;
import com.zhang.pojo.entity.Menu;
import com.zhang.pojo.entity.Role;
import com.zhang.pojo.entity.User;
import com.zhang.pojo.entity.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/*
*
 * @author 张会丽
 * @create 2019/8/7
*/

@Component
public class UserService {
    @Resource
    UserDao uDao;
    @Resource
    RoleDao rDao;
    @Resource
    MenuDao mDao;
    @Resource
    UserRoleDao urDao;
    public Page<User> userList(Map<String,Object> map){
        Integer page=0;
        Integer pageSize=5;
        if (map.get("page")!=null&&map.get("pageSize")!=null){
            page=Integer.parseInt(map.get("page").toString());
            pageSize=Integer.parseInt(map.get("pageSize").toString());
        }
        Page<User> mohu = uDao.findByUserNameLike("%" + map.get("mohu").toString() + "%", PageRequest.of(page, pageSize));
        for (User user:mohu){
            user.setRole(rDao.role(user.getId()));
        }
        return mohu;
    }
    public Menu findMenuById(Long id){
        Menu byId = mDao.getById(id);
        List<Menu> byParentId = mDao.getByParentId(byId.getId());
        byId.setMenuList(byParentId);
        return byId;
    }
    public List<Role> findAllRole() {
        List<Role> all = rDao.findAll();
        return all;
    }
    public void deleteUserById(Long id){
        uDao.deleteById(id);
    }
    public void addUser(User user){
        uDao.save(user);
    }
    public void purRoleByUserId(long userId,long roleId){
        UserRole userRole=new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);
        urDao.deleteByUserId(userId);
        urDao.save(userRole);
    }
}
