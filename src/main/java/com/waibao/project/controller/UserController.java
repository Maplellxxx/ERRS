package com.waibao.project.controller;

import com.waibao.project.bean.Log;
import com.waibao.project.bean.User;
import com.waibao.project.mapper.LogMapper;
import com.waibao.project.mapper.SystemAdminMapper;
import com.waibao.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserMapper userMapper;

    @Autowired
    LogMapper logMapper;

    @Autowired
    SystemAdminMapper systemAdminMapper;


    @GetMapping("/myinfo")
    public String myInfo(Map<String,String> map, HttpSession session,Model model){
        String username = (String)session.getAttribute("loginUser");
        model.addAttribute("info",userMapper.getUser(username));
        return "new/myinfo";
    }

    @GetMapping("/userinfo/{username}")
    public String userInfo(@PathVariable("username") String username,
                           Map<String,String> map, HttpSession session){
//        if(!username.equals(session.getAttribute("loginUser"))&&session.getAttribute("systemAdmin")==null){
//            map.put("msg","非系统管理员，没有权限访问");
//            return "login";
//        }
        User user = userMapper.getUser(username);
        map.put("id",String.valueOf(user.getId()));
        map.put("username",user.getUsername());
        map.put("password",user.getPassword());
        map.put("authority",user.getAuthority());
        map.put("name",user.getName());
        map.put("gender",user.getGender());
        map.put("project_team",user.getProjectteam());
        map.put("department",user.getDepartment());
        map.put("email",user.getEmail());
        if(username.equals(session.getAttribute("loginUser"))){
            map.put("t","true");
        }
        return "userinfo";
    }

    @GetMapping("/register")
    public String Register(){
        return "register";
    }

    @PostMapping(value = "/register")
    public String AddUser(@RequestParam("username")String username,
                          @RequestParam("password1")String password1,
                          @RequestParam("password2")String password2,
                          @RequestParam("name")String name,
                          @RequestParam("gender")String gender,
                          @RequestParam("projectteam")String projectteam,
                          @RequestParam("department")String department,
                          @RequestParam("email")String email,
                          Map<String,Object> map, HttpSession session){
        if(userMapper.IfUsernameExist(username)!=0){
            map.put("msg","账号已存在");
            return "register";
        }
        if (password1.length()<6){
            map.put("msg","密码太短了，最少需要6位");
            return "register";
        }
        if(password1.equals(password2)){
            User user = new User();
            user.setUsername(username);
            user.setPassword(password1);
            user.setAuthority("普通用户");
            user.setName(name);
            user.setGender(gender);
            user.setProjectteam(projectteam);
            user.setDepartment(department);
            user.setEmail(email);
            userMapper.insertUser(user);
            session.setAttribute("loginUser",username);
//            session.setAttribute("admin","管理员");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log log = new Log();
            log.setAuthority("普通用户");
            log.setUserID(userMapper.getID(username));
            log.setOperation("注册用户");
            log.setObject(userMapper.getID(username));
            log.setType("user");
            log.setTime(df.format(new Date()));
            logMapper.addLog(log);
            return "redirect:/main.html";
        }else{
            map.put("msg","两次输入密码不一样");
            return "register";
        }
    }
//系统管理员才有权限删除用户
    @GetMapping("/system/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Integer id,
                             HttpSession session){
        userMapper.deleteUserById(id);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        if(session.getAttribute("systemAdmin")!=null){
            log.setAuthority("系统管理员");
        }else if(session.getAttribute("systemAdmin")==null&&session.getAttribute("admin")!=null){
            log.setAuthority("管理员");
        }else {
            log.setAuthority("普通用户");
        }
        log.setUserID(userMapper.getSystemAdminID(String.valueOf(session.getAttribute("loginUser"))));
        log.setOperation("删除用户");
        log.setObject(id);
        log.setType("user");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);
        return "redirect:/system/userlist";
    }

    @GetMapping("/updateUser/{username}")
    public String updateUser(@PathVariable("username")String username,
                             Map<String,String>map, HttpSession session){
        if(!username.equals(session.getAttribute("loginUser"))&&session.getAttribute("systemAdmin")==null){
            map.put("msg","非系统管理员，没有权限访问");
            return "login";
        }
        User user = userMapper.getUser(username);
        map.put("id",String.valueOf(user.getId()));
        map.put("username",user.getUsername());
        map.put("password",user.getPassword());
        map.put("authority",user.getAuthority());
        map.put("name",user.getName());
        map.put("gender",user.getGender());
        map.put("projectteam",user.getProjectteam());
        map.put("department",user.getDepartment());
        map.put("email",user.getEmail());
        return "updateUser";
    }

    @PostMapping("/updateUser")
    public String updateUserPost(@RequestParam("id")String id,
                                 @RequestParam("username")String username,
                                 @RequestParam("password")String password,
                                 @RequestParam("authority")String authority,
                                 @RequestParam("name")String name,
                                 @RequestParam("gender")String gender,
                                 @RequestParam("projectteam")String projectteam,
                                 @RequestParam("department")String department,
                                 @RequestParam("email")String email,
                                 Map<String,Object> map,HttpSession session){
        User user = userMapper.getUserById(Integer.valueOf(id));
        map.put("id",Integer.valueOf(id));
        map.put("username",user.getUsername());
        map.put("password",user.getPassword());
        map.put("authority",user.getAuthority());
        map.put("name",user.getName());
        map.put("gender",user.getGender());
        map.put("projectteam",user.getProjectteam());
        map.put("department",user.getDepartment());
        map.put("email",user.getEmail());
        if(userMapper.IfUsernameExist(username)!=0&&!user.getUsername().equals(username)){
            map.put("msg","账号已存在");
            return "updateUser";
        }
        if (password.length()<6){
            map.put("msg","密码太短了，最少需要6位");
            return "updateUser";
        }
        User u = new User();
        u.setId(Integer.valueOf(id));
        u.setUsername(username);
        u.setPassword(password);
        u.setAuthority(authority);
        u.setName(name);
        u.setGender(gender);
        u.setProjectteam(projectteam);
        u.setDepartment(department);
        u.setEmail(email);
        userMapper.updateUser(u);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log log = new Log();
        if(session.getAttribute("systemAdmin")!=null){
            log.setAuthority("系统管理员");
        }else if(session.getAttribute("systemAdmin")==null&&session.getAttribute("admin")!=null){
            log.setAuthority("管理员");
        }else {
            log.setAuthority("普通用户");
        }
        if(session.getAttribute("systemAdmin")!=null){
            log.setUserID(userMapper.getSystemAdminID(String.valueOf(session.getAttribute("loginUser"))));
        }else{
            log.setUserID(userMapper.getID(String.valueOf(session.getAttribute("loginUser"))));
        }
        log.setOperation("修改用户信息");
        log.setObject(Integer.valueOf(id));
        log.setType("user");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);

        if (session.getAttribute("admin")==null||session.getAttribute("systemAdmin")==null){
            session.setAttribute("loginUser",username);
        }
        return "redirect:/userinfo/"+username;
    }

    @GetMapping("/changePassword/{username}")
    public String changePassowrd(@PathVariable("username")String username,
                                 Map<String,Object>map, HttpSession session){
        if (!username.equals(session.getAttribute("loginUser"))&&session.getAttribute("systemAdmin")==null){
            map.put("msg","非本人或系统管理员，没有权限访问");
            return "login";
        }
        return "changePassword";
    }

    @PostMapping("/changePassword/{username}")
    public String changePasswordPost(@PathVariable("username")String username,
                                    @RequestParam("oldpassword")String oldpassword,
                                    @RequestParam("newpassword1")String newpassword1,
                                    @RequestParam("newpassword2")String newpassword2,
                                    Map<String,Object>map, HttpSession session){
        User user=userMapper.getUser(username);
        if (userMapper.ifPasswordTrue(oldpassword,user.getId())==0){
            map.put("msg","旧密码错误");
            return "changePassword";
        }

        if(!newpassword1.equals(newpassword2)){
            map.put("msg","两次密码不一样");
            return "changePassword";        }

        userMapper.changePassword(newpassword1,user.getId());
        return "redirect:/userinfo/"+username;
    }

    @GetMapping("/cancel")
    public String cancel(HttpSession session){
        session.removeAttribute("loginUser");
        session.removeAttribute("admin");
        session.removeAttribute("systemAdmin");
        return "redirect:/";
    }


}


//public void ttt(){
//    Method m = new Methods();
//}