package com.zhang.controller;
import com.zhang.ResponseResult;
import com.zhang.dao.MenuDao;
import com.zhang.entity.Menu;
import com.zhang.entity.Role;
import com.zhang.service.RoleService;
import com.zhang.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张会丽
 * @create 2019/8/8
 */
@Controller
@Api(tags = "This is RoleController")
public class RoleController {
    @Autowired
    RoleService rService;
    @Autowired
    UserService uService;
    /**
     * 角色列表
     * @param map
     * @return
     */
    @RequestMapping("getRoleList")
    @ResponseBody
    public ResponseResult getUserList(@RequestBody Map<String,Object> map){
        ResponseResult results=ResponseResult.getResponseResult();
        //获取所有的角色
        Page<Role> role = rService.getRole(map);
        Menu menu = uService.findMenuById(5L);
        Map<String,Object> map1=new HashMap<>();
        //获取所有的菜单
        List<Menu> list = rService.getAllMenu();
        map1.put("role",role);
        map1.put("menu",menu);
        map1.put("list",list);
        results.setResult(map1);
        return results;
    }
    /**
     * 根据id删除角色
     * @param map
     * @return
     */
    @RequestMapping("deleteRoleById")
    @ResponseBody
    public ResponseResult deleteRoleById(@RequestBody Map<String,Object> map){
        ResponseResult results=ResponseResult.getResponseResult();
        if (map!=null&&map.get("id")!=null){
            //根据id删除角色
            rService.deleteRoleById(Long.parseLong(map.get("id").toString()));
            results.setCode(200);
            results.setSuccess("角色删除成功");
        }else {
            results.setError("角色删除失败");
        }
        return results;
    }
    /**
     * 添加角色
     * @param role
     * @return
     */
    @RequestMapping("addRole")
    @ResponseBody
    public ResponseResult addRole(@RequestBody Role role){
        ResponseResult results=ResponseResult.getResponseResult();
        if (role!=null){
            int i = rService.checkedRoleName(role.getRoleName());
            if (i>0){
                results.setCode(500);
                results.setError("角色名已存在");
            }
            rService.addRole(role);
            results.setCode(200);
            results.setSuccess("角色添加成功");
        }else {
            results.setError("角色添加失败");
        }
        return  results;
    }
    /**
     * 绑定菜单
     * @param map
     * @return
     */
    @RequestMapping("putMenuByRoleId")
    @ResponseBody
    public ResponseResult putMenuByRoleId(@RequestBody Map<String,Object> map){
        ResponseResult results=ResponseResult.getResponseResult();
        if (map!=null){
            //绑定权限
            rService.putMenuByRoleId(map);
            results.setCode(200);
            results.setSuccess("保存成功");
        }else {
            results.setError("保存失败");
        }
        return results;
    }
}
