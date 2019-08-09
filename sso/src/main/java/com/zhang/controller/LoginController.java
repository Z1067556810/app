package com.zhang.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhang.pojo.ResponseResult;
import com.zhang.pojo.entity.User;
import com.zhang.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zhang.exception.LoginException;
import zhang.jwt.JWTUtils;
import zhang.random.VerifyCodeUtils;
import zhang.utils.MD5;
import zhang.utils.UID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 张会丽
 * @create 2019/8/9
 */
@Controller
public class LoginController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    LoginService lService;

    /**
     * 登录操作
     * @param map
     * @return
     * @throws LoginException
     */
    @ResponseBody
    @RequestMapping("login")
    public ResponseResult login(@RequestBody Map<String,Object> map) throws LoginException {
        ResponseResult results=ResponseResult.getResponseResult();
        //获取生成的验证码
        String code = redisTemplate.opsForValue().get(map.get("codekey").toString())==null?"":redisTemplate.opsForValue().get(map.get("codekey").toString()).toString();
        System.out.println("<<<<<<<<<<<<"+code);
        //获取传入的验证码是否是生成后存在redis中的验证码
        if(code==null||!code.equals(map.get("code").toString())){
            results.setCode(500);
            results.setError("验证码错误,请重新刷新页面登陆");
            return results;
        }
        if(map!=null&&map.get("loginname")!=null){
            User user = lService.login(map.get("loginname").toString());
            if(user!=null){
                //加密密码
                String password = MD5.encryptPassword(map.get("password").toString(), "lcg");
                //判断密码
                if(user.getPassword().equals(password)){
                    String userinfo = JSON.toJSONString(user);
                    String token = JWTUtils.generateToken(userinfo);
                    results.setToken(token);
                    //通过id获取的用户信息设置给token
                    redisTemplate.opsForValue().set("USER"+user.getId().toString(),token);
                    //通过id获取用户信息存储到map里
                    redisTemplate.opsForHash().putAll(user.getId().toString(),user.getAuthmap());
                    System.out.println(user.getId().toString()+"<><><><><><>");
                    System.out.println(user.getAuthmap()+"><><><><><><>");
                    //过期时间
                    redisTemplate.expire("USER"+user.getId().toString(),10000, TimeUnit.SECONDS);
                    results.setResult(user);
                    results.setCode(200);
                    results.setSuccess("登录成功！^_^");
                    System.out.println(results);
                    return results;

                }else{
                    throw new LoginException("用户名或密码错误");
                }
            }else{
                throw new LoginException("用户名或密码错误");
            }
        }else{
            throw new LoginException("用户名或密码错误");
        }
    }
    /**
     * 获取滑动验证的验证码
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("getCode")
    @ResponseBody
    public ResponseResult getCode(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        //生成一个五位数的code
        String code = VerifyCodeUtils.generateVerifyCode(5);
        System.out.println(">>>>>>>>>>"+code);
        ResponseResult results=ResponseResult.getResponseResult();
        //把code存储到result里
        results.setResult(code);
        String uidCode="CODE"+ UID.getUUID16();
        //把code存储到redis
        redisTemplate.opsForValue().set(uidCode,code);
        //code的过期时间
        redisTemplate.expire(uidCode,10,TimeUnit.MINUTES);
        Cookie cookie=new Cookie("authcode",uidCode);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
        return results;
    }

    /**
     * 手动加载密码
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(MD5.encryptPassword("123456","lcg"));
        JSONObject jsonObject = JWTUtils.decodeJwtToken("eyJhbGciOiJIUzUxMiJ9.eyJjcmVhdGVkIjoxNTY0OTEzMzIzMzU4LCJleHAiOjE1NjQ5MTMzODMsInVzZXJpbmZvIjoie1wiaWRcIjpcIjY0NTU2NDY1NFwifSJ9.iT-NmNBkbjK29t4DLtyJvsAwp770QyYkUpEGB-Lmy-xDVH2NWUtPqQJmovV7PZV46IGPVUMvYMOAaEhbJ6voaA");
        System.out.println(jsonObject.get("id"));
    }
}

