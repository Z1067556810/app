
package com.zhang.controller;

import com.zhang.pojo.ResponseResult;
import com.zhang.pojo.entity.Menu;
import com.zhang.pojo.entity.Role;
import com.zhang.pojo.entity.User;
import com.zhang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import zhang.utils.MD5;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张会丽
 * @create 2019/8/7
 */

@Controller
public class UserController {
    @Autowired
    UserService uService;
    @RequestMapping("getUserList")
    @ResponseBody
    public ResponseResult userList(@RequestBody Map<String,Object> map){
        ResponseResult results=ResponseResult.getResponseResult();
        Page<User> users = uService.userList(map);
        for (User user:users){
            System.out.println(user+"<<<s11111");
        }
        Menu menuById = uService.findMenuById(3L);
        List<Role> role = uService.findAllRole();
        Map<String,Object> map1=new HashMap<>();
        map1.put("users",users);
        map1.put("menuById",menuById);
        map1.put("role",role);
        results.setResult(map1);
        return results;
    }
    @RequestMapping("upload")
    @ResponseBody
    public void upload(@RequestParam("file")MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        file.transferTo(new File("F:/image/"+filename));
    }
    @RequestMapping("deleteUserById")
    @ResponseBody
    public ResponseResult deleteUserById(@RequestBody Map<String,Object> map){
        ResponseResult results=ResponseResult.getResponseResult();
        if (map!=null&&map.get("id")!=null){
            uService.deleteUserById(Long.parseLong(map.get("id").toString()));
            results.setCode(200);
            results.setSuccess("删除成功");
        }else{
            results.setError("删除失败");
        }
        return results;
    }
    @RequestMapping("addUser")
    @ResponseBody
    public ResponseResult addUser(@RequestBody User user){
        ResponseResult results=ResponseResult.getResponseResult();
        if (user!=null){
            user.setPassword(MD5.encryptPassword(user.getPassword(),"lcg"));
            uService.addUser(user);
            results.setCode(200);
            results.setSuccess("添加成功");
        }else{
            results.setError("添加失败");
        }
        return results;
    }
    @RequestMapping("updateUser")
    @ResponseBody
    public ResponseResult updateUser(@RequestBody User user){
        ResponseResult results=ResponseResult.getResponseResult();
        if (user!=null){
            uService.addUser(user);
            results.setCode(200);
            results.setSuccess("修改成功");
        }else {
            results.setError("修改失败");
        }
        return results;
    }
    @RequestMapping("putRoleByUserId")
    @ResponseBody
    public ResponseResult putRoleByUserId(@RequestBody Map<String,Object> map){
        ResponseResult results=ResponseResult.getResponseResult();
        if (map!=null){
            uService.purRoleByUserId(Long.parseLong(map.get("userId").toString()),Long.parseLong(map.get("roleId").toString()));
            results.setCode(200);
            results.setSuccess("绑定成功");
        }else {
            results.setError("绑定失败");
        }
        return results;
    }
}
