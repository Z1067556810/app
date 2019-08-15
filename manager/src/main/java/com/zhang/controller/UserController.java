
package com.zhang.controller;

import com.zhang.ResponseResult;
import com.zhang.entity.Menu;
import com.zhang.entity.Role;
import com.zhang.entity.User;
import com.zhang.service.UserService;
import net.coobird.thumbnailator.Thumbnails;
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

    /**
     * 用户列表
     * @param map
     * @return
     */
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

    /**
     * 上传图片
     * @param file
     * @throws IOException
     */
    @RequestMapping("upload")
    @ResponseBody
    public void upload(@RequestParam("file")MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        File file1 = new File(filename);
        file.transferTo(file1);
        Thumbnails.of(filename).scale(0.25f).toFile(file1.getAbsolutePath() + "_25%.jpg");
    }

    /**
     * 根据id删除用户
     * @param map
     * @return
     */
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

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("addUser")
    @ResponseBody
    public ResponseResult addUser(@RequestBody User user){
        ResponseResult results=ResponseResult.getResponseResult();
        String loginName = user.getLoginName();
        if (user!=null) {
            int i = uService.checkedUserLoginName(loginName);
            if (i>0){
                user.setPassword(MD5.encryptPassword(user.getPassword(), "lcg"));
                uService.addUser(user);
                results.setCode(200);
                results.setSuccess("新增成功");
            }else {
                results.setError("此用户已注册");
            }
        }
        return results;
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @RequestMapping("updateUser")
    @ResponseBody
    public ResponseResult updateUser(@RequestBody User user){
        ResponseResult results=ResponseResult.getResponseResult();
        if (user!=null){
            int i = uService.checkedUserLoginName(user.getLoginName());
            if (i>0){
                results.setCode(500);
                results.setError("登录名已存在");
                return results;
            }
            results.setCode(200);
            results.setSuccess("修改成功");
        }else {
            results.setError("修改失败");
        }
        return results;
    }

    /**
     * 绑定角色
     * @param map
     * @return
     */
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
