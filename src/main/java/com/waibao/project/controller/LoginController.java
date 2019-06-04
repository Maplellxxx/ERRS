package com.waibao.project.controller;

import com.waibao.project.bean.User;
import com.waibao.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    UserMapper userMapper;

    @GetMapping("/system/login")
    public String systemLogin(){
        return "system/systemlogin";
    }

    @PostMapping("/system/login")
    public String systemLogin(@RequestParam("username")String username,
                              @RequestParam("password1")String password1,
                              @RequestParam("password2")String password2,
                              Map<String,Object> map, HttpSession session){
        if (userMapper.ifSystemAdminLogin(username, DigestUtils.md5DigestAsHex(password1.getBytes()),DigestUtils.md5DigestAsHex(password2.getBytes()))!=0){
            session.setAttribute("loginUser",username);
            session.setAttribute("admin","管理员");
            session.setAttribute("systemAdmin","系统管理员");
            return "redirect:/homepage";
        }else{
            map.put("msg","密码错误");
            return "system/systemlogin";
        }
    }

    @GetMapping("/system/index")
    public String system_index(){
        return "system/index";
    }



    @PostMapping(value="/login")
    public String login(@RequestParam("username")String username,
                        @RequestParam("password")String password,
                        Map<String,Object> map, HttpSession session){
//        if(!StringUtils.isEmpty(username)&&"123456".equals(password)){
        //清空session
        session.removeAttribute("loginUser");
        session.removeAttribute("admin");
        session.removeAttribute("systemAdmin");
        if(userMapper.ifLogin(username,password)!=0){
            //success,防止表单重复提交，重定向
            User user = userMapper.getUser(username);
            session.setAttribute("username",username);
            if(user.getAuthority().equals("普通用户")){
                session.setAttribute("loginUser",username);
                return "redirect:/homepage";
            }else{
                session.setAttribute("loginUser",username);
                session.setAttribute("admin","管理员");
                return "redirect:/homepage";
            }
        }else{
            map.put("msg","用户名密码错误");
            return "login";
        }
    }
}
