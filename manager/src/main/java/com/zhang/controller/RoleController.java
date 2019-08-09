package com.zhang.controller;
import com.zhang.pojo.ResponseResult;
import com.zhang.pojo.entity.Menu;
import com.zhang.pojo.entity.Role;
import com.zhang.service.RoleService;
import com.zhang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张会丽
 * @create 2019/8/8
 */
@Controller
public class RoleController {
    @Autowired
    RoleService rService;
    @Autowired
    UserService uService;
    @RequestMapping("getRoleList")
    @ResponseBody
    public ResponseResult getUserList(@RequestBody Map<String,Object> map){
        ResponseResult results=ResponseResult.getResponseResult();
        Page<Role> role = rService.getRole(map);
        Menu menu = uService.findMenuById(5L);
        Map<String,Object> map1=new HashMap<>();
        List<Menu> list = rService.getAllMenu();
        map1.put("role",role);
        map1.put("menu",menu);
        map1.put("list",list);
        results.setResult(map1);
        return results;
    }
    @RequestMapping("deleteRoleById")
    public ResponseResult deleteRoleById(@RequestBody Map<String,Object> map){
        ResponseResult results=ResponseResult.getResponseResult();
        if (map!=null&&map.get("id")!=null){
            rService.deleteRoleById(Long.parseLong(map.get("id").toString()));
            results.setCode(200);
            results.setSuccess("角色删除成功");
        }else {
            results.setError("角色删除失败");
        }
        return results;
    }
    @RequestMapping("addRole")
    public ResponseResult addRole(@RequestBody Role role){
        ResponseResult results=ResponseResult.getResponseResult();
        if (role!=null){
            rService.addRole(role);
            results.setCode(200);
            results.setSuccess("角色添加成功");
        }else {
            results.setError("角色添加失败");
        }
        return  results;
    }
    @RequestMapping("updateRole")
    public ResponseResult updateRole(@RequestBody Role role){
        ResponseResult results=ResponseResult.getResponseResult();
        if (role!=null){
            rService.addRole(role);
            results.setCode(200);
            results.setSuccess("角色修改成功");
        }else{
            results.setError("角色修改失败");
        }
        return results;
    }
    @RequestMapping("putMenuByRoleId")
    public ResponseResult putMenuByRoleId(@RequestBody Map<String,Object> map){
        ResponseResult results=ResponseResult.getResponseResult();
        if (map!=null){
            rService.putMenuByRoleId(map);
            results.setCode(200);
            results.setSuccess("保存成功");
        }else {
            results.setError("保存失败");
        }
        return results;
    }
}
