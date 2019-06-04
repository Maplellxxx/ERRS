package com.waibao.project.controller;

import com.waibao.project.bean.*;
import com.waibao.project.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
public class InsertionController {
    @Autowired
    LogMapper logMapper;
    @Autowired
    InsertionMapper insertionMapper;

    @Autowired
    AppointmentMapper appointmentMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    EquipmentMapper equipmentMapper;

    @Autowired
    ProjectMapper projectMapper;

    @PostMapping("/appointment/insertion")
    public String requestInsertAppointment(@RequestParam("InsertEquipmentID")Integer InsertEquipmentID,
                                           @RequestParam("Inserttime")Integer t, RedirectAttributes attributes,
                                           @RequestParam("Insertproject")Integer Insertproject,
                                           @RequestParam("Insertreason")String Insertreason, HttpSession session){
        Insertion insertion = new Insertion();
        insertion.setUsername((String)session.getAttribute("loginUser"));
        insertion.setProjectID(Insertproject);
        insertion.setEquipmentID(InsertEquipmentID);
        insertion.setReason(Insertreason);
        String date=String.valueOf(t/100);
        insertion.setDate(date);
        Integer dd = t%100;
        String time;
        if (dd<10){
            insertion.setTime('0'+String.valueOf(dd));
            time='0'+String.valueOf(dd);
        }else{
            insertion.setTime(String.valueOf(dd));
            time=String.valueOf(dd);
        }
        Appointment original = appointmentMapper.getAppointmentByEquipmentIDAndDateAndTime(InsertEquipmentID,date,time);
        insertion.setOriginal(original.getUsername());
        insertionMapper.insertionAdd(insertion);

        //添加log
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
        log.setOperation("申请紧急插入");
        log.setObject(insertion.getId());
        log.setType("insertion");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);

//        attributes.addFlashAttribute("insertionInfo","请求成功，等待处理");
        return "redirect:/appointment/"+InsertEquipmentID+"/"+date;
    }


    @GetMapping("/appointment/insertionList")
    @ResponseBody
    public List<Insertion> insertions(Model model){
        List list = insertionMapper.insertionList();
        Collections.reverse(list);
        return list;
    }

    @GetMapping("/appointment/myInsertionRequired")
    public String myInsertionRequired(Model model,HttpSession session){
        List<Insertion> l = insertionMapper.myInsertionRequired((String) session.getAttribute("loginUser"));
        List<Put> list = new ArrayList<>();
        Put put;
        Project project;
        Equipment equipment;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");//设置日期格式
//        System.out.println(df.format(new Date()));
//        list.add()
        for(int i=l.size()-1;i>=0;i--){
            equipment = equipmentMapper.getEquipmentById(l.get(i).getEquipmentID());
            project = projectMapper.getProjectById(l.get(i).getProjectID());
            put = new Put();
            System.out.println("111111");
            put.setInfo1(equipment.getNumber());
            put.setInfo2(equipment.getName());
            put.setInfo3(equipment.getLocation());
            put.setInfo4(project.getName());
            int a,b,c;
            int d = Integer.parseInt(l.get(i).getDate());
            a=d/10000;
            b=(d%10000)/100;
            c=d%100;
            String datetime = String.valueOf(a)+"-"+String.valueOf(b)+"-"+String.valueOf(c);
            put.setInfo5(datetime);
            put.setInfo6(l.get(i).getTime());
            put.setInfo7(l.get(i).getReason());
            put.setInfo8(l.get(i).getResult());
            list.add(put);
        }
//        System.out.println(list.get(0).getInfo1());
//        System.out.println(list.get(0).getInfo2());
        model.addAttribute("insertion",list);
        return "new/myRequired";
    }

    @GetMapping("/appointment/InsertionRequired")
    public String InsertionRequiredList(HttpSession session,Model model){
        List<Insertion> list = insertionMapper.InsertionRequired((String)session.getAttribute("loginUser"));
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        for(int i=0;i<list.size();i++){
            String t1 = list.get(i).getDate()+list.get(i).getTime();
            String t2 =df.format(new Date());
            String a = "审核中";
            if(Integer.parseInt(t1)<Integer.parseInt(t2)&&list.get(i).getResult().equals(a)){
                insertionMapper.overtimeRequired(list.get(i).getId());
            }
        }
        List list1 =insertionMapper.InsertionRequired((String)session.getAttribute("loginUser"));
        Collections.reverse(list1);
        model.addAttribute("insertion",list1);
        return "new/receiveRequired";
    }

    @GetMapping("/allowRequired/{id}")
    public String  allowRequired(@PathVariable("id")Integer id,HttpSession session){
        Insertion insertion = insertionMapper.getInsertion(id);
        if(!insertion.getOriginal().equals(session.getAttribute("loginUser"))){
            return "redirect:/error";
        }
        String a = "审核中";
        if(!insertion.getResult().equals(a)){
            return "redirect:/error";
        }
        Appointment appointment = new Appointment();
        insertionMapper.allowRequired(id);
        appointmentMapper.deleteAppointment(insertion.getEquipmentID(),insertion.getDate(),insertion.getTime());
        appointment.setUsername(insertion.getUsername());
        appointment.setTime(insertion.getTime());
        appointment.setEquipmentID(insertion.getEquipmentID());
        appointment.setDate(insertion.getDate());
        appointment.setProjectID(insertion.getProjectID());
        appointment.setInfo(appointment.getDate()+appointment.getTime());
        appointmentMapper.appointment(appointment);

        //添加log
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
        log.setOperation("同意紧急插入请求");
        log.setObject(Integer.valueOf(id));
        log.setType("insertion");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);

        return "redirect:/appointment/InsertionRequired";

    }

    @GetMapping("/denyRequired/{id}")
    public String denyRequired(@PathVariable("id")Integer id,HttpSession session){
        Insertion insertion = insertionMapper.getInsertion(id);
        if(!insertion.getOriginal().equals(session.getAttribute("loginUser"))){
            return "redirect:/error";
        }
        String a = "审核中";
        if(!insertion.getResult().equals(a)){
            return "redirect:/error";
        }
        insertionMapper.denyRequired(id);
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
        log.setOperation("拒绝紧急插入请求");
        log.setObject(Integer.valueOf(id));
        log.setType("insertion");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);
        return "redirect:/appointment/InsertionRequired";
    }

//    @GetMapping("/admin/appointment/insertionRequired")
//    public String InsertionRequiredList(Model model){
//        List list = insertionMapper.insertionList();
//        model.addAttribute("insertion",list);
//        return "appointment/insertionRequired";
//    }
//
//    @GetMapping("/admin/allowRequired/{id}")
//    public String allowRequired(@PathVariable("id")Integer id){
//        Insertion insertion = insertionMapper.getInsertion(id);
//        Appointment appointment = new Appointment();
//        insertionMapper.allowRequired(id);
//        appointmentMapper.deleteAppointment(insertion.getEquipmentID(),insertion.getDate(),insertion.getTime());
//        appointment.setUsername(insertion.getUsername());
//        appointment.setTime(insertion.getTime());
//        appointment.setEquipmentID(insertion.getEquipmentID());
//        appointment.setDate(insertion.getDate());
//        appointment.setProjectID(insertion.getProjectID());
//        appointmentMapper.appointment(appointment);
//        return "redirect:/admin/appointment/insertionRequired";
//    }
//
//    @GetMapping("/admin/denyRequired/{id}")
//    public String denyRequired(@PathVariable("id")Integer id){
//        Insertion insertion = insertionMapper.getInsertion(id);
//        insertionMapper.denyRequired(id);
//        return "redirect:/admin/appointment/insertionRequired";
//    }
}
