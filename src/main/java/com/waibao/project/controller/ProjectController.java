package com.waibao.project.controller;

import com.waibao.project.bean.Log;
import com.waibao.project.bean.Project;
import com.waibao.project.bean.ProjectAdmin;
import com.waibao.project.mapper.LogMapper;
import com.waibao.project.mapper.ProjectMapper;
import com.waibao.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.waibao.project.Methods;

@Controller
public class ProjectController {
    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LogMapper logMapper;

    @GetMapping("/projectlist/all")
    public String projectlist(Model model,HttpSession session){
//        model.addAttribute("info",projectMapper.projectList());
        model.addAttribute("running",projectMapper.getRunningProject());
        model.addAttribute("pause",projectMapper.getPauseProject());
        model.addAttribute("complete",projectMapper.getCompleteProject());
        model.addAttribute("establish",projectMapper.getEstablish((String)session.getAttribute("loginUser")));
        List<Project> list = new ArrayList<>();
        list.addAll(projectMapper.getRunningProject());
        list.addAll(projectMapper.getPauseProject());
        list.addAll(projectMapper.getCompleteProject());
        model.addAttribute("info",list);
        return "new/allproject";
        //        return "project/projectList";
    }

    @GetMapping("/projectlist/running")
    public String projectlistRunning(Model model,HttpSession session){
        model.addAttribute("running",projectMapper.getRunningProject());
        return "new/projectRunning";
        //        return "project/projectList";
    }

    @GetMapping("/projectlist/complete")
    public String projectlistComplete(Model model,HttpSession session){
        model.addAttribute("complete",projectMapper.getCompleteProject());
        return "new/projectComplete";
    }

    @GetMapping("/projectlist/pause")
    public String projectlistPause(Model model,HttpSession session){
        model.addAttribute("pause",projectMapper.getPauseProject());
        return "new/projectPause";
    }

    @GetMapping("/projectPause/{id}")
    public String projectPause(@PathVariable("id")Integer id, Map<String,Object> map,HttpSession session){
        Project project=projectMapper.getProjectById(id);
        if (project.getState().equals("暂停")){
            map.put("msg","该项目已暂停");
            return "error";
        }
        projectMapper.ProjectPause(id);
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
        log.setOperation("项目暂停");
        log.setObject(Integer.valueOf(id));
        log.setType("project");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);
        return "redirect:/projectlist/pause";
    }

    @GetMapping("/projectRunIndex/{id}")
    public String projectRunIndex(@PathVariable("id")Integer id, Map<String,Object> map,HttpSession session){
        Project project=projectMapper.getProjectById(id);
        if (project.getState().equals("进行中")){
            map.put("msg","该项目已在进行中");
            return "error";
        }
        projectMapper.ProjectRun(id);
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
        log.setOperation("项目进行");
        log.setObject(Integer.valueOf(id));
        log.setType("project");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);
        return "redirect:/projectlist/all";
    }

    @GetMapping("/projectCompleteIndex/{id}")
    public String projectCompleteIndex(@PathVariable("id")Integer id, Map<String,Object> map,HttpSession session){
        Project project=projectMapper.getProjectById(id);
        if (project.getState().equals("完成")){
            map.put("msg","该项目已完成");
            return "error";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        projectMapper.ProjectComplete(id,df.format(new Date()));
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
        log.setOperation("项目完成");
        log.setObject(Integer.valueOf(id));
        log.setType("project");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);
        return "redirect:/projectlist/all";
    }


    @GetMapping("/projectPauseIndex/{id}")
    public String projectPauseIndex(@PathVariable("id")Integer id, Map<String,Object> map,HttpSession session){
        Project project=projectMapper.getProjectById(id);
        if (project.getState().equals("暂停")){
            map.put("msg","该项目已暂停");
            return "error";
        }
        projectMapper.ProjectPause(id);
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
        log.setOperation("项目暂停");
        log.setObject(Integer.valueOf(id));
        log.setType("project");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);
        return "redirect:/projectlist/all";
    }

    @GetMapping("/projectRun/{id}")
    public String projectRun(@PathVariable("id")Integer id, Map<String,Object> map,HttpSession session){
        Project project=projectMapper.getProjectById(id);
        if (project.getState().equals("进行中")){
            map.put("msg","该项目已在进行中");
            return "error";
        }
        projectMapper.ProjectRun(id);
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
        log.setOperation("项目进行");
        log.setObject(Integer.valueOf(id));
        log.setType("project");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);
        return "redirect:/projectlist/running";
    }

    @GetMapping("/projectComplete/{id}")
    public String projectComplete(@PathVariable("id")Integer id, Map<String,Object> map,HttpSession session){
        Project project=projectMapper.getProjectById(id);
        if (project.getState().equals("完成")){
            map.put("msg","该项目已完成");
            return "error";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        projectMapper.ProjectComplete(id,df.format(new Date()));
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
        log.setOperation("项目完成");
        log.setObject(Integer.valueOf(id));
        log.setType("project");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);
        return "redirect:/projectlist/complete";
    }


    @GetMapping("/project/add")
    public String projectAdd(){
        return "new/projectAdd";
    }

    @PostMapping("/project/add")
    public String projectAddPost(@RequestParam("number")String number,
                                 @RequestParam("name")String name, Map<String,String>map,
                                 HttpSession session){
        if (projectMapper.ifProjectNumberExist(number)!=0){
            map.put("msg","该项目代码已存在");
            return "new/projectAdd";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Project project=new Project();
        project.setNumber(number);
        project.setName(name);
        project.setStarttime(df.format(new Date()));
        project.setStoptime("");
        project.setState("进行中");
        String originator = (String)session.getAttribute("loginUser");
        project.setOriginator(originator);
        projectMapper.projectAdd(project);
        //log
        Project p = projectMapper.getProject(number);
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
        log.setOperation("项目添加");
        log.setObject(p.getId());
        log.setType("project");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);
        return "redirect:/projectlist/running";
    }

    @GetMapping("/project/info/{number}")
    public String projectInfo(@PathVariable("number")String number,
                              Map<String,Object>map,Model model,HttpSession session){
        Project project = projectMapper.getProject(number);
        if(projectMapper.ifProjectAdmin(number,(String) session.getAttribute("loginUser"))!=0||
                session.getAttribute("loginUser").equals(project.getOriginator())||
                userMapper.ifAdmin((String) session.getAttribute("loginUser"))!=0||
                session.getAttribute("systemAdmin")!=null){
            map.put("a","a");
        }
        if (session.getAttribute("loginUser").equals(project.getOriginator())){
            map.put("originator",project.getOriginator());
        }
        map.put("info",project);
        model.addAttribute("ProjectAdmin",projectMapper.getProjectAdmin(number));
        return "project/projectInfo";
    }

    @GetMapping("/project/info/id/{id}")
    public String projectInfo(@PathVariable("id")Integer id,
                              Map<String,Object>map,Model model,HttpSession session){
        Project project = projectMapper.getProjectById(id);
        String number = project.getNumber();
        if(projectMapper.ifProjectAdmin(number,(String) session.getAttribute("loginUser"))!=0||
                session.getAttribute("loginUser").equals(project.getOriginator())||
                userMapper.ifAdmin((String) session.getAttribute("loginUser"))!=0||
                session.getAttribute("systemAdmin")!=null){
            map.put("a","a");
        }
        if (session.getAttribute("loginUser").equals(project.getOriginator())){
            map.put("originator",project.getOriginator());
        }
        map.put("info",project);
        model.addAttribute("ProjectAdmin",projectMapper.getProjectAdmin(number));
        return "project/projectInfo";
    }


    @PostMapping("/project/addAdmin/{number}")
    public String addProjectAdmin(@PathVariable("number")String number,
                                  @RequestParam("username")String username,
                                  Map<String,Object>map, RedirectAttributes attributes,
                                  Model model,HttpSession session){
        Project project = projectMapper.getProject(number);
        if(userMapper.IfUsernameExist(username)==0){
            attributes.addFlashAttribute("msg", "该用户名不存在");
            return "redirect:/project/info/"+number;
        }
        if(projectMapper.ifProjectAdminAlreadyExist(number,username)!=0||username.equals(projectMapper.getProject(number).getOriginator())){
            attributes.addFlashAttribute("msg", "用户已在管理队列");
            return "redirect:/project/info/"+number;
        }
        ProjectAdmin projectAdmin = new ProjectAdmin();
        projectAdmin.setNumber(number);
        projectAdmin.setUsername(username);
        projectMapper.addProjectAdmin(projectAdmin);

        //log
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
        log.setOperation("项目暂停");
        log.setObject(projectMapper.getProject(number).getId());
        log.setType("project");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);
        return "redirect:/project/info/"+number;
    }

}
