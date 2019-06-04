package com.waibao.project.controller;

import com.waibao.project.Methods;
import com.waibao.project.bean.Backup;
import com.waibao.project.bean.Log;
import com.waibao.project.bean.SystemAdmin;
import com.waibao.project.bean.User;
import com.waibao.project.mapper.BackupMapper;
import com.waibao.project.mapper.LogMapper;
import com.waibao.project.mapper.SystemAdminMapper;
import com.waibao.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class SystemController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    LogMapper logMapper;

    @Autowired
    SystemAdminMapper systemAdminMapper;

    @Autowired
    BackupMapper backupMapper;

    @RequestMapping("/system/userlist")
    public String Show(Model model){
        model.addAttribute("info",userMapper.ListUser());
        return "new/userList";
    }

    @GetMapping("/system/userinfo/{id}")
    public String userinfo(@PathVariable("id")Integer id,
                           Map<String,Object> map, HttpSession session){
        User user = userMapper.getUserById(id);
        map.put("user",user);
        return "new/userinfo";
    }

    @GetMapping("/system/updateUser/{id}")
    public String updateUser(@PathVariable("id")Integer id,
                             Map<String,Object> map, HttpSession session){
        User user = userMapper.getUserById(id);
        map.put("user",user);
        return "system/updateUser";
    }

    @PostMapping("/system/updateUser")
    public String updateUser(@RequestParam("id")String id,
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
        map.put("user",user);
        if(userMapper.IfUsernameExist(username)!=0&&!user.getUsername().equals(username)){
            map.put("msg","账号已存在");
            return "system/updateUser";
        }
        if (password.length()<6){
            map.put("msg","密码太短了，最少需要6位");
            return "system/updateUser";
        }
        User u = new User();
        u.setId(Integer.valueOf(id));
        u.setUsername(username);
        u.setPassword(password);
        u.setAuthority(authority);
        u.setName(name);
        u.setGender(gender);
        u.setProjectteam(projectteam);
        u.setEmail(email);
        u.setDepartment(department);
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

        return "redirect:/system/userinfo/"+id;
    }



    @GetMapping("/system/backup")
    public String backup(Map map){
        List<Backup> list = backupMapper.getBackupList();
        map.put("time",list.get(list.size()-1).getTime());
        map.put("info",list.get(list.size()-1).getInfo());
        return "system/backup";
    }

    @RequestMapping(value = "system/databasebackup",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String databasebackup(HttpServletResponse response) throws Exception{
        String filePath="C:\\Users\\13108\\Desktop\\预约系统备份\\";
        /*String fileName="批量上传模板.xlsx";*/
        String fileName="预约系统备份文件";
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        String time=df.format(new Date());
        String dbName="project";
        File uploadDir = new File(filePath);
        if (!uploadDir.exists())
            uploadDir.mkdirs();

        try { Process process = Runtime.getRuntime().exec(
                "cmd /c c:\\mysqldump -h localhost -u root -proot " + dbName + " > "
                        + filePath + "/" + fileName+time
                        + ".sql");
            System.out.println("success!!!");
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            backupMapper.BackupAdd(df1.format(new Date()),"系统管理员备份");
                return "success";
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "error";
        }

    }


    @GetMapping("/system/systemAdmin/{username}")
    public String systemAdminUserinfo(@PathVariable("username")String username,
                                      Model model,Map map){
        map.put("info",systemAdminMapper.getSystemAdminByUsername(username));
        return "system/systemAdminInfo";
    }


    @GetMapping("/system/register")
    public String systemAdminRegister(){
        return "system/systemRegister";
    }


    @PostMapping("/system/register")
    public String systemAdminRegisterPost(@RequestParam("secretkey")String secretkey,
                                          @RequestParam("username")String username,
                                          @RequestParam("password1")String password1,
                                          @RequestParam("password2")String password2,
                                          @RequestParam("name1")String name1,
                                          @RequestParam("name2")String name2,
                                          @RequestParam("gender1")String gender1,
                                          @RequestParam("gender2")String gender2,
                                          @RequestParam("projectteam1")String projectteam1,
                                          @RequestParam("projectteam2")String projectteam2,
                                          @RequestParam("department1")String department1,
                                          @RequestParam("department2")String department2,
                                          @RequestParam("email1")String email1,
                                          @RequestParam("email2")String email2,
                                          RedirectAttributes attributes,HttpSession session){

        String a = "zjnu";
        if (!secretkey.equals(a)){
            attributes.addFlashAttribute("msg","密钥错误");
            return "redirect:/system/register";
        }
        if (systemAdminMapper.ifSystemAdminExist(username)!=0){
            attributes.addFlashAttribute("msg","用户名已存在");
            return "redirect:/system/register";
        }
        SystemAdmin systemAdmin = new SystemAdmin();
        systemAdmin.setUsername(username);
        systemAdmin.setName1(name1);
        systemAdmin.setName2(name2);
        String md5Password1 = DigestUtils.md5DigestAsHex(password1.getBytes());
        String md5Password2 = DigestUtils.md5DigestAsHex(password2.getBytes());
        systemAdmin.setPassword1(md5Password1);
        systemAdmin.setPassword2(md5Password2);
        systemAdmin.setGender1(gender1);
        systemAdmin.setGender2(gender2);
        systemAdmin.setDepartment1(department1);
        systemAdmin.setDepartment2(department2);
        systemAdmin.setProjectteam1(projectteam1);
        systemAdmin.setProjectteam2(projectteam2);
        systemAdmin.setEmail1(email1);
        systemAdmin.setEmail2(email2);
        systemAdminMapper.systemAdminRegister(systemAdmin);
        session.setAttribute("loginUser",username);
        session.setAttribute("admin","管理员");
        session.setAttribute("systemAdmin","系统管理员");

        //添加日志
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
        log.setOperation("系统管理员注册");
        log.setObject(userMapper.getSystemAdminID(String.valueOf(session.getAttribute("loginUser"))));
        log.setType("user");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);

        return "redirect:/system/index";
    }
}
