package com.zhang.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhang.ResponseResult;
import com.zhang.entity.User;
import com.zhang.service.LoginService;
import com.zhang.utils.EmailUtil;
import com.zhang.utils.SmsUtil;
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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 张会丽
 * @create 2019/8/9
 */
@Controller
public class LoginController {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
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
                    String userInfo = JSON.toJSONString(user);
                    String token = JWTUtils.generateToken(userInfo);
                    results.setToken(token);
                    //通过id获取的用户信息设置给token
                    redisTemplate.opsForValue().set("USER"+user.getId().toString(),token);
                    //通过id获取用户信息存储到map里
                    redisTemplate.opsForHash().putAll("USERDATAAUTH"+user.getId().toString(),user.getAuthmap());
                    System.out.println(user.getId().toString()+"<><><><><><>");
                    System.out.println(user.getAuthmap()+"><><><><><><>");
                    //过期时间
                    redisTemplate.expire("USER"+user.getId().toString(),1, TimeUnit.SECONDS);
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
        cookie.setDomain("127.0.0.1");
        response.addCookie(cookie);
        return results;
    }
    //退出登录
    @RequestMapping("loginout")
    @ResponseBody
    public ResponseResult loginout(@RequestBody Map<String,Object> map){
        ResponseResult results=ResponseResult.getResponseResult();
        redisTemplate.delete("USER"+map.get("id").toString());
        results.setCode(200);
        results.setSuccess("ok");
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
	/**
     * 短信验证登录
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("smsLogin")
    public ResponseResult smsLogin(@RequestBody Map<String,String> map){
        ResponseResult results = ResponseResult.getResponseResult();
        System.out.println("登录时："+map.get("phoneNumber"));
        User user = lService.getUserByTel(map.get("phoneNumber"));
        System.out.println("登录时："+user);
        if(user != null){
            String code = map.get("code");
            String redisCode = redisTemplate.opsForValue().get("CODE" + code);
            if(code.equals(redisCode)){
                //将用户信息转存为JSON串
                String userInfo = JSON.toJSONString(user);
                //将用户信息使用JWt进行加密，将加密信息作为票据
                String token = JWTUtils.generateToken(userInfo);
                System.out.println(token);
                //将加密信息存入statuInfo
                results.setToken(token);
                //将生成的token存储到redis库
                redisTemplate.opsForValue().set("USERINFO" + user.getId().toString(), token);
                //将该用户的数据访问权限信息存入缓存中
                redisTemplate.opsForHash().putAll("USERDATAAUTH" + user.getId().toString(), user.getAuthmap());
                //设置token过期 30分钟
                redisTemplate.expire("USERINFO" + user.getId().toString(), 600, TimeUnit.SECONDS);
                //设置返回值
                results.setResult(user);
                results.setCode(200);
                //设置成功信息
                results.setSuccess("登陆成功！^_^");
                return results;
            }else {
                results.setError("验证码超时!");
                return results;
            }
        }else{
            results.setError("手机号未注册!");
            return results;
        }
    }

    /**
     * 获取短信验证码
     * @param
     * @return 返回发送结果向前台
     */
    @ResponseBody
    @RequestMapping("getSmsCode")
    public ResponseResult getSmsCode(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String,String> map)  {
        System.out.println(map.get("phoneNumber")+"====手机号");
        User user = lService.getUserByTel(map.get("phoneNumber"));//根据手机号获取到这条用户信息
        System.out.println("user:"+user);
        ResponseResult responseResult = ResponseResult.getResponseResult();
        if(user != null){
            //生成一个长度为4的随机字符串
            String code = SmsUtil.getCode();
            System.out.println(code+"手机验证码");
            String status = SmsUtil.sendSms(map.get("phoneNumber"), code);
            System.out.println(status.equals("OK"));
            if(status.equals("OK")){
                Cookie[] cookies = request.getCookies();
                responseResult.setResult(code);
                String uidCode = "CODE"+code;
                //将生成的随机字符串标识后存入redis
                redisTemplate.opsForValue().set(uidCode, code);
                //设置过期时间
                redisTemplate.expire(uidCode, 50, TimeUnit.MINUTES);
                //回写cookie
                Cookie cookie = new Cookie("authcode", uidCode);
                cookie.setPath("/");
                cookie.setDomain("127.0.0.1");
                response.addCookie(cookie);
                responseResult.setCode(200);
                return responseResult;
            }else {
                responseResult.setError("验证码发送失败");
                return responseResult;
            }
        }else{
            responseResult.setError("手机号未注册！");
            return responseResult;
        }

    }

    /**
     * 根据id查询用户信息
     * @param map
     * @return
     */
    @RequestMapping("getUserById")
    public ResponseResult getUserById(@RequestBody Map<String,Object> map){
        ResponseResult result = ResponseResult.getResponseResult();
        System.out.println(map.get("userid")+"========");
        if (map.get("userid")!=null){
            Map<String, Object> userId = lService.getUserById(map.get("userid").toString());
            result.setResult(userId);
        }
        return result;
    }

    /**
     * 邮箱验证码
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping("sendemailCode")
    public  ResponseResult getemailCode(@RequestBody Map<String,String> map)  {

        ResponseResult responseResult = ResponseResult.getResponseResult();
        User byLogin = lService.login(map.get("account"));
        User byTel = lService.getUserByTel(map.get("account"));
        User byEmail = lService.getUserByEmail(map.get("account"));
        String code = UUID.randomUUID().toString();

        if (byLogin != null) {  //如果根据登录名查到这个用户
            new Thread(new EmailUtil(byLogin.getEmail(), code)).start();  //开启线程，发送验证码
            lService.updateCodeById(byLogin.getId(), code);  //根据用户id修改code码
            redisTemplate.opsForValue().set(code, "");  // 新增一个  code 是键，“” 是值
            redisTemplate.expire(code, 30, TimeUnit.MINUTES); //重新设置过期时间为30分钟，刷新时间
            responseResult.setCode(200);
        } else if (byTel != null) {  //如果根据手机号查到这个用户
            new Thread(new EmailUtil(byTel.getEmail(), code)).start();
            lService.updateCodeById(byTel.getId(), code);
            redisTemplate.opsForValue().set(code, "");
            redisTemplate.expire(code, 30, TimeUnit.MINUTES);
            responseResult.setCode(200);
        } else if (byEmail != null){  //如果根据邮箱查到这个用户
            new Thread(new EmailUtil(byEmail.getEmail(), code)).start();
            lService.updateCodeById(byEmail.getId(), code);
            redisTemplate.opsForValue().set(code, "");
            redisTemplate.expire(code, 30, TimeUnit.MINUTES);
            responseResult.setCode(200);
        } else {
            responseResult.setCode(500);
        }

        System.out.println("邮箱验证："+responseResult);
        return responseResult;
    }

    /**
     * 修改密码
     * @param map
     * @return
     */
    @RequestMapping("resetPwd")
    @ResponseBody
    public ResponseResult resetPwd(@RequestBody Map<String, String> map) {
        ResponseResult responseResult = ResponseResult.getResponseResult();
        User byCode = lService.findUserByCode(map.get("code"));
        if (redisTemplate.hasKey(map.get("code"))) {
            lService.updatePasswordByCode(MD5.encryptPassword(map.get("password"),"lcg"),map.get("code"));
            responseResult.setCode(200);
        } else {
            responseResult.setCode(500);
        }
        return responseResult;
    }
}

