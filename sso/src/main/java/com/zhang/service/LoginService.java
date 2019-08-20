package com.zhang.service;

import com.zhang.dao.MenuDao;
import com.zhang.dao.RoleDao;
import com.zhang.dao.UserDao;
import com.zhang.entity.Menu;
import com.zhang.entity.Role;
import com.zhang.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author 张会丽
 * @create 2019/8/9
 */
@Component
public class LoginService {
    @Autowired
    UserDao uDao;
    @Autowired
    RoleDao rDao;
    @Autowired
    MenuDao mDao;

    /**
     * 登录
     * @param loginname
     * @return
     */
    public User login(String loginname) {
        User user = uDao.findByLoginName(loginname);
        if(user != null){
            //获取权限
            Role role = rDao.role(user.getId());
            user.setRole(role);
            if(role != null){
                //获取菜单
                List<Menu> allMenus = mDao.getByRoleId(role.getId());
                List<Menu> parList = new ArrayList<>();
                for (Menu allMenu : allMenus) {
                    if(allMenu.getLeval() == 1){
                        parList.add(allMenu);
                    }
                }
                Map<String,String> map = new HashMap<>();
                this.getForMenuInfo(parList,map,allMenus);
                System.out.println(map);
                user.setAuthmap(map);
                user.setMenuList(parList);
            }
        }
        return user;
    }

    /**
     * 递归获取菜单
     * @param list
     * @param map
     * @param allMenus
     */
    public void getForMenuInfo(List<Menu> list, Map<String, String> map,List<Menu> allMenus){
        for (Menu menu : list) {
            if(menu.getLeval() == 4){
                map.put(menu.getUrl(),"");
            }
            List<Menu> chilMenus=new ArrayList<>();
            for (Menu allMenu:allMenus){
                if (menu.getId() == allMenu.getParentId()){
                    chilMenus.add(allMenu);
                }
            }
            menu.setMenuList(chilMenus);
            if(menu.getMenuList().size() > 0){
                getForMenuInfo(menu.getMenuList(),map,allMenus);
            }
        }
    }
	/**
     * 根据手机号登录
     * @param tel
     * @return
     */
    public User getUserByTel(String tel){
        //获取用户信息
        User byLoginName = uDao.findUserByTel(tel);
        System.out.println("service:"+tel+"用户信息："+byLoginName);
        if(byLoginName!=null){
            //将所有角色赋值给用户
            byLoginName.setRoleList(rDao.findAll());
            //获取用户的角色信息
            Role roleInfoByUserId = rDao.role(byLoginName.getId());
            //设置用户的角色信息
            byLoginName.setRole(roleInfoByUserId);
            if (roleInfoByUserId!=null){
                //获取用户的权限信息
                List<Menu> firstMenuInfo = mDao.getByRoleId(roleInfoByUserId.getId());
                List<Menu> parList=new ArrayList<>();
                for (Menu allMenu:firstMenuInfo) {
                    if (allMenu.getLeval()==1) {
                        parList.add(allMenu);
                    }
                }
                //递归的查询子菜单权限
                Map<String,String> authmap=new Hashtable<>();
                this.getForMenuInfo(parList,authmap,firstMenuInfo);
                System.out.println(authmap);
                //设置菜单的子权限
                byLoginName.setAuthmap(authmap);
                byLoginName.setMenuList(parList);
            }
        }
        return byLoginName;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Map<String,Object> getUserById(String id){
        Map<String, Object> userById = uDao.getUserById(id);
        return userById;
    }
    /**
     * 根据code码查询用户
     * @param code
     * @return
     */
    public User findUserByCode(String code){
        return uDao.findUserByCode(code);
    }

    /**
     * 根据code码更改用户密码
     * @param password
     * @param code
     */
    public void updatePasswordByCode(String password,String code){
        uDao.updatePasswordByCode(password,code);
    }

    /**
     * 根据用户ID修改code码
     * @param id
     * @param code
     */
    public void updateCodeById(Long id,String code){
        uDao.updateCodeById(code,id);
    }

    /**
     * 根据email查询用户
     * @param email
     * @return
     */
    public User getUserByEmail(String email){
        return uDao.findUserByEmail(email);
    }

}