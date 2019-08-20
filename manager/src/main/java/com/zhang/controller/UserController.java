
package com.zhang.controller;

import com.zhang.ResponseResult;
import com.zhang.entity.Menu;
import com.zhang.entity.Role;
import com.zhang.entity.User;
import com.zhang.entity.UserRole;
import com.zhang.service.RoleService;
import com.zhang.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zhang.utils.MD5;
import zhang.utils.TwitterIdWorker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 张会丽
 * @create 2019/8/7
 */

@Controller
@Api(tags = "This is UserController")
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
        //获取用户的信息
        Page<User> users = uService.userList(map);
        for (User user:users){
            System.out.println(user+"<<<s11111");
        }
        //根据id查询菜单
        Menu menuById = uService.findMenuById(3L);
        //获取全部的角色
        List<Role> role = uService.findAllRole();
        Map<String,Object> map1=new HashMap<>();
        //用于前台取值
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
        //getOriginalFilename:得到上传时的文件名
        String filename = "F://image//"+file.getOriginalFilename();
        File file1 = new File(filename);
        //transferTo保存文件
        file.transferTo(file1);
        // Thumbnails工具类  按照scale(比例)进行缩放
        Thumbnails.of(filename).scale(0.25f).toFile(file1.getAbsolutePath() + "_25.jpg");
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
            //根据用户id删除
            uService.deleteUserById(Long.parseLong(map.get("id").toString()));
            results.setCode(200);
            results.setSuccess("删除成功");
            return results;
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
            //根据登录名查询
            /*int i = uService.checkedUserLoginName(loginName);
            if (i>0){*/
                //给密码加密  SALT值属于随机值。用户注册时，系统用来和用户密码进行组合而生成的随机数值，称作salt值，通称为加盐值。
                user.setPassword(MD5.encryptPassword(user.getPassword(), "lcg"));
                uService.addUser(user);
                results.setCode(200);
                results.setSuccess("新增成功");
                return results;
            }else {
                results.setError("此用户已注册");
            }
        //}
        return results;
    }
    /**
     * 修改用户
     * @param user
     * @return
     */
    @RequestMapping("updateUserById")
    @ResponseBody
    public ResponseResult updateUser(@RequestBody User user){
        ResponseResult results=ResponseResult.getResponseResult();
        if (user!=null){
            /*int i = uService.checkedUserLoginName(user.getLoginName());
            if (i>0){
                results.setCode(500);*/
                /*results.setError("登录名已存在");
                return results;*/
            //}
            uService.addUser(user);
            results.setCode(200);
            results.setSuccess("修改成功");
            return results;
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
            //根据用户id进行绑定
            uService.purRoleByUserId(Long.parseLong(map.get("userId").toString()),Long.parseLong(map.get("roleId").toString()));
            results.setCode(200);
            results.setSuccess("绑定成功");
            return results;
        }else {
            results.setError("绑定失败");
        }
        return results;
    }
}
