package com.zhang.service;

import com.zhang.dao.MenuDao;
import com.zhang.dao.RoleDao;
import com.zhang.dao.UserDao;
import com.zhang.dao.UserRoleDao;
import com.zhang.entity.Menu;
import com.zhang.entity.Role;
import com.zhang.entity.User;
import com.zhang.entity.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
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

    /**
     * 用户列表
     * @param map
     * @return
     */
    public Page<User> userList(Map<String,Object> map){
        Integer page=0;
        Integer pageSize=5;
        if (map.get("page")!=null&&map.get("pageSize")!=null){
            page=Integer.parseInt(map.get("page").toString());
            pageSize=Integer.parseInt(map.get("pageSize").toString());
            System.out.println("page:"+page+"pageSize:"+pageSize);
        }
        Page<User> mohu = uDao.findByUserNameLike("%" + map.get("mohu").toString() + "%", PageRequest.of(page, pageSize, Sort.by(Sort.Order.desc("updateTime"))));
        for (User user:mohu){
            user.setRole(rDao.role(user.getId()));
        }
        return mohu;
    }

    /**
     * 根据菜单查找
     * @param id
     * @return
     */
    public Menu findMenuById(Long id){
        Menu byId = mDao.getById(id);
        List<Menu> byParentId = mDao.getByParentId(byId.getId());
        byId.setMenuList(byParentId);
        return byId;
    }

    /**
     * 角色
     * @return
     */
    public List<Role> findAllRole() {
        List<Role> all = rDao.findAll();
        return all;
    }

    /**
     * 删除用户
     * @param id
     */
    public void deleteUserById(Long id){
        uDao.deleteById(id);
    }

    /**
     * 添加用户
     * @param user
     */
    public void addUser(User user){
        uDao.save(user);
    }

    /**
     * 绑定角色
     * @param userId
     * @param roleId
     */
    public void purRoleByUserId(Long userId,Long roleId){
        UserRole userRole=new UserRole();
        userRole.setRoleId(roleId);
        userRole.setUserId(userId);
        urDao.deleteByUserId(userId);
        urDao.save(userRole);
    }

    /**
     * 判断登录名唯一性
     * @param loginName
     * @return
     */
    public boolean addLoginName(String loginName ){
        User user  = uDao.findByLoginName(loginName);
        if (user==null ){
            return true; //如果数据库没有就是true
        }else {
            return false;  //如果数据库有就是false
        }
    }
    public int checkedUserLoginName(String loginName) {
        int i = uDao.countByLoginName(loginName);
        return i;
    }
    public List<Menu> getBrotherMenusByUrl(String url, String roleId) {
        List<Menu> byUrlAndRoleIdBrotherMenus = mDao.getByUrlAndRoleIdBrotherMenus(url, Long.parseLong(roleId));
        return byUrlAndRoleIdBrotherMenus;
    }
}
