package com.zhang.controller;

import com.zhang.ResponseResult;
import com.zhang.entity.Menu;
import com.zhang.entity.Role;
import com.zhang.service.MenuService;
import com.zhang.service.RoleService;
import com.zhang.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author 张会丽
 * @create 2019/8/12
 */
@Controller
@Api(tags = "This is MenuController")
public class MenuController {
    @Autowired
    MenuService mService;
    @Autowired
    RoleService rService;
    @Autowired
    UserService uService;

    /**
     * 获取菜单列表
     * @param map
     * @return
     */
    @RequestMapping("getMenuList")
    @ResponseBody
    public ResponseResult getMenuList(@RequestBody Map<String, Object> map) {
        ResponseResult results = ResponseResult.getResponseResult();
        Map<String, Object> map1 = new HashMap<>();
        if (map != null && map.get("userId") != null && map.get("roleId") != null) {
            List<Menu> menuList = uService.getBrotherMenusByUrl("getMenuList", map.get("roleId").toString());
            map1.put("currentPageMenus", menuList);
        } else {
            results.setCode(500);
            results.setError("获取列表错误");
            return results;
        }
        //获取所有的菜单集合
        List<Menu> allMenu = rService.getAllMenu();
        map1.put("allMenu", allMenu);
        results.setCode(200);
        results.setResult(map1);
        return results;
    }

    /**
     * 根据id删除
     * @param map
     * @return
     */
    @RequestMapping("deleteMenuById")
    @ResponseBody
    public ResponseResult deleteMenu(@RequestBody Map<String, Object> map) {
        ResponseResult results = ResponseResult.getResponseResult();
        if (map != null && map.get("ids") != null && "1".equals(map.get("flag").toString())) {
            String[] ids = map.get("ids").toString().split(",");
            //获取当前角色绑定的权限
            Set<Role> roleByMenuId = mService.getRoleByMenuId(ids);
            if (roleByMenuId != null && roleByMenuId.size() > 0) {
                for (Role roleInfo : roleByMenuId) {
                    System.out.println("roleByMenuId///////" + roleInfo);
                }
                results.setCode(300);
                results.setError("此权限正绑定用户");
                results.setResult(roleByMenuId);
            } else {
                mService.deleteMenuById(ids);
                results.setCode(200);
                results.setSuccess("删除成功");
            }
        } else if (map != null && map.get("ids") != null && "0".equals(map.get("flag").toString())) {
            String[] ids = map.get("ids").toString().split(",");
            mService.deleteMenuById(ids);
            results.setCode(200);
            results.setSuccess("删除成功");
        }
        return results;
    }

    /**
     * 添加菜单
     * @param menu
     * @return
     */
    @ResponseBody
    @RequestMapping("addMenu")
    public ResponseResult addMenu(@RequestBody Menu menu) {
        ResponseResult results = ResponseResult.getResponseResult();
        if (menu != null) {
            mService.addMenu(menu);
            results.setCode(200);
            results.setSuccess("新增成功");
            results.setResult(menu);
        } else {
            results.setError("新增失败");
        }
        return results;
    }

    /**
     * 修改菜单
     * @param menu
     * @return
     */
    @ResponseBody
    @RequestMapping("updateMenuById")
    public ResponseResult updateMenuById(@RequestBody Menu menu) {
        ResponseResult results = ResponseResult.getResponseResult();
        if (menu != null) {
            mService.addMenu(menu);
            results.setCode(200);
            results.setSuccess("修改成功");
            results.setResult(menu);
        } else {
            results.setError("修改失败");
        }
        return results;
    }
}
