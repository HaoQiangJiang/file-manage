package com.hs.controller;

import com.github.pagehelper.Page;
import com.hs.annotation.UserLoginToken;
import com.hs.entity.PageResult;
import com.hs.entity.Result;
import com.hs.entity.StatusCode;
import com.hs.pojo.Users;
import com.hs.service.TokenService;
import com.hs.service.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Api("用户接口")
@RestController
@RequestMapping("/api/v1")

public class UsersController {

    @Autowired
    TokenService tokenService;
    @Autowired
    private UsersService usersService;

    // 登录
    @ApiOperation(value = "登陆", notes = "使用用户名和密码登陆。成功，返回token；失败，返回错误原因")
    @PostMapping("/login")
    public Map login(@RequestParam String username, @RequestParam String passwd, HttpServletResponse response) {
        HashMap<Object, Object> map = new HashMap<>();
        Users userForBase = usersService.findByName(username);
        if (userForBase != null) {
            if (!userForBase.getPasswd().equals(DigestUtils.md5DigestAsHex(passwd.getBytes()))) {
                map.put("code", 400);
                map.put("token", "");
                map.put("error", "密码错误");
                return map;
            } else {
                String token = tokenService.getToken(userForBase);
                Cookie cookie = new Cookie("token", token);
                cookie.setPath("/");
                response.addCookie(cookie);
                map.put("code", 200);
                map.put("token", token);
                map.put("error", "");
                return map;
            }
        } else {
            map.put("code", 400);
            map.put("token", "");
            map.put("error", "没有该用户！");
            return map;
        }
    }

    /**
     * 用户注册
     *
     * @return
     */

    @ApiOperation(value = "用户注册", notes = "注册。成功，返回201；失败，返回错误原因。只能注册教师或学生类型的账户（1 老师，2 学生）", produces = "application/json")
    @PostMapping(value = "/regist")
    public Map regist(@RequestParam String username, @RequestParam String passwd, @RequestParam String email, @RequestParam String usertype) {
        Users users = new Users(username, passwd, email, usertype);
        HashMap<Object, Object> map = new HashMap<>();
        if ("1".equals(users.getUsertype())) {
            map.put("code", 400);
            map.put("error", "只能注册教师或学生类型的账户!");
            return map;
        }
        if (usersService.findByName(users.getUsername()) == null) {
            users.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            users.setPasswd(DigestUtils.md5DigestAsHex(users.getPasswd().getBytes()));
            try {
                usersService.add(users);
            } catch (Exception e) {
                map.put("code", 400);
                map.put("error", e.getMessage());
                return map;
            }
            map.put("code", 201);
            map.put("error", "");
            return map;
        }
        map.put("code", 400);
        map.put("error", "用户已存在！");
        return map;
    }

    /**
     * 获取用户列表
     *
     * @param page
     * @return
     */
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表。成功，返回用户列表；失败，返回错误原因。该操作需要管理员权限，必须在http头或者url中设置token值。", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", required = true)
    })
    @UserLoginToken
    @GetMapping(value = "/users")
    public PageResult findAll(String name, String usertype, @RequestParam(value = "page", defaultValue = "1") String page) {
        PageResult pageResult;
        try {
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("username", name);
            searchMap.put("usertype", usertype);
            Page<Users> pageList = usersService.findPage(searchMap, Integer.parseInt(page), 10);
            pageResult = new PageResult(200, pageList.getPages(), pageList.getResult(), "");
        } catch (NumberFormatException e) {
            return new PageResult(StatusCode.ERROR, 1, null, "");
        }
        return pageResult;
    }

    /**
     * 添加用户
     *
     * @return
     */
    @ApiOperation(value = "添加用户", notes = "添加用户。成功，返回201；失败，返回错误原因。该操作需要管理员权限，必须在http头或者formData设置token值。", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", required = true)
    })
    @UserLoginToken
    @PostMapping(value = "/users")
    public Map add(@RequestParam String username, @RequestParam String passwd, @RequestParam String email, @RequestParam String usertype) {
        HashMap<Object, Object> map = new HashMap<>();
        Users users = new Users(username, passwd, email, usertype);
        if (usersService.findByName(users.getUsername()) != null) {
            map.put("code", 400);
            map.put("error", "用户已存在！");
            return map;
        }
        try {
            users.setId(UUID.randomUUID().toString().replaceAll("-", ""));
            users.setPasswd(DigestUtils.md5DigestAsHex(users.getPasswd().getBytes()));
            usersService.add(users);
        } catch (Exception e) {
            map.put("code", 400);
            map.put("error", e.getMessage());
            return map;
        }
        map.put("code", 201);
        map.put("error", "");
        return map;
    }

    /**
     * 忘记密码
     *
     * @param email
     * @return
     */
    @ApiOperation(value = "忘记密码", notes = "忘记密码。成功，返回200，系统会向用户指定的email中发送一封邮件，用户通过该邮件可以重置密码；失败，返回错误原因。", produces = "application/json")
    @PostMapping(value = "/users/forgetpwd")
    public Map forgetpwd(@RequestParam String email) {
        HashMap<Object, Object> map = new HashMap<>();
        if (!usersService.findEmail(email)) {
            map.put("code", 400);
            map.put("error", "没有该邮箱！");
            return map;
        }
        try {
            usersService.sendEmail(email);
        } catch (MessagingException e) {
            map.put("code", 400);
            map.put("error", "发送失败");
            return map;
        }
        map.put("code", 200);
        map.put("error", "");
        return map;
    }

    /**
     * 重置密码
     *
     * @param code
     * @param passwd
     * @return
     */
    @ApiOperation(value = "重置密码", notes = "重置密码。成功，返回200，失败，返回错误原因。", produces = "application/json")
    @PostMapping(value = "/users/resetpwd")
    public Map resetpwd(@RequestParam String code, @RequestParam String passwd) {
        passwd = DigestUtils.md5DigestAsHex(passwd.getBytes());
        HashMap<Object, Object> map = new HashMap<>();
        try {
            usersService.resetPwd(code, passwd);
        } catch (Exception e) {
            map.put("code", 400);
            map.put("error", "code码不存在！");
            return map;
        }
        map.put("code", 200);
        map.put("error", "");
        return map;
    }

    /**
     * 获取指定用户
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取指定用户", notes = "获取指定用户。成功，返回用户；失败，返回错误原因。该操作需要管理员权限，必须在http头或者url中设置token值。", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", required = true)
    })
    @UserLoginToken
    @GetMapping("/users/{id}")
    public Result findById(@PathVariable String id) {
        Users users = usersService.findById(id);
        if (users != null) {
            return new Result(StatusCode.OK, "", users);
        }
        return new Result(StatusCode.ERROR, "没有该用户", users);
    }

    /**
     * 更新用户
     *
     * @param id
     * @return
     */
    @UserLoginToken
    @ApiOperation(value = "更新用户", notes = "更新用户。成功，返回200；失败，返回错误原因。该操作需要管理员权限，必须在http头或者url中设置token值。注意：后台管理员更新密码也使用该接口，此时只需要发送 id， token， passwd 三个参数即可", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", required = true)
    })
    @PutMapping(value = "/users/{id}")
    public Map update(String username, String passwd, String email, String usertype, @PathVariable String id) {
        Users users = new Users(username, passwd, email, usertype);
        users.setId(id);
        HashMap<Object, Object> map = new HashMap<>();
        if (username == null) {
            users = usersService.findById(id);
        }

        try {
            users.setPasswd(DigestUtils.md5DigestAsHex(users.getPasswd().getBytes()));
            usersService.update(users);
        } catch (Exception e) {
            map.put("code", 400);
            map.put("error", e.getMessage());
            return map;
        }
        map.put("code", 200);
        map.put("error", "");
        return map;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @UserLoginToken
    @ApiOperation(value = "删除用户", notes = "删除用户。成功，返回200；失败，返回错误原因。该操作需要管理员权限，必须在http头或者url中设置token值。", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", dataType = "String", name = "token", required = true)
    })
    @DeleteMapping(value = "/users/{id}")
    public Map delete(@PathVariable String id) {
        HashMap<Object, Object> map = new HashMap<>();
        int delete = usersService.delete(id);
        if (delete == 0) {
            map.put("code", 400);
            map.put("error", "不存在该用户！");
            return map;
        }
        map.put("code", 200);
        map.put("error", "");
        return map;
    }
}
