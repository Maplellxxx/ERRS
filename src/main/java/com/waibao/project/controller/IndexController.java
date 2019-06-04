package com.waibao.project.controller;


import com.sun.javafx.collections.MappingChange;
import com.waibao.project.bean.Appointment;
import com.waibao.project.bean.Equipment;
import com.waibao.project.bean.Project;
import com.waibao.project.mapper.AppointmentMapper;
import com.waibao.project.mapper.EquipmentMapper;
import com.waibao.project.mapper.ProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class IndexController {
    @Autowired
    EquipmentMapper equipmentMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    AppointmentMapper appointmentMapper;

    @GetMapping("/homepage")
    public String homepage(){
        return "new/HomePage";
    }

    @GetMapping("/index")
    public String index(Map<String,Object> map, HttpSession session, Model model){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");//设置日期格式
        String username = (String) session.getAttribute("loginUser");
        map.put("equipmentNum",equipmentMapper.getNumOfEquipment());
        map.put("projectNum",projectMapper.getProjectOfNum());
//        map.put("appointmentTime",appointmentMapper.getUserAppointmentAllTime(username));
        map.put("appointmentTime",appointmentMapper.getAppointmentRecentTimes(username,Integer.parseInt(df.format(new Date()))));


        List<Appointment> list =  appointmentMapper.getRecentAppointment((String)session.getAttribute("loginUser"),Integer.parseInt(df.format(new Date())));
        Collections.sort(list, new Comparator<Appointment>(){
            public int compare(Appointment p1, Appointment p2) {
                if(Integer.parseInt(p1.getInfo()) > Integer.parseInt(p2.getInfo())){
                    return 1;
                }
                if(Integer.parseInt(p1.getDate()) == Integer.parseInt(p2.getDate())){
                    return 0;
                }
                return -1;
            }
        });

        if(list.size()>0){
            Equipment equipment=equipmentMapper.getEquipmentById(list.get(0).getEquipmentID());
            Project project=projectMapper.getProjectById(list.get(0).getProjectID());
            String time1 = list.get(0).getTime();
            String time2 = String.valueOf(Integer.parseInt(time1)+1);
            map.put("Appointment",list.get(0));
            map.put("Equipment",equipment);
            map.put("Project",project);
            map.put("time1",time1);
            map.put("time2",time2);
            map.put("t1",list.size());
        }else{
            map.put("t1",list.size());
        }


       List<Appointment> list2 = appointmentMapper.getAppointmentByUsername((String) session.getAttribute("loginUser"));
       map.put("RecentProject",list2);


        //最近的项目
        List list3 = new ArrayList();
        List<Project> list4 = new ArrayList<Project>();
        int t=0;
        if(list2.size()>0){
            for (int i=0;i<list2.size();i++){
                if (!list3.contains(list2.get(i).getProjectID())) {
                    list3.add(list2.get(i).getProjectID());
                }
            }
            for (int i=0;i<list3.size();i++) {
                list4.add(projectMapper.getProjectById((Integer) list3.get(i)));
                t++;
                if (list4.size()==3){
                    break;
                }
            }
        }

        map.put("t2",t);
        model.addAttribute("recentProject",list4);


        if (list.size()>0){
            map.put("t3",list.size());
            model.addAttribute("recentAppointment",list);
        }else{
            map.put("t3",0);
        }


        Integer now = Integer.parseInt(df.format(new Date()));
        Integer using = appointmentMapper.getRecentOneHourAppointmentNum(now+1);
        Integer free = equipmentMapper.getNumOfEquipment() - using;
        map.put("using",using);
        map.put("free",free);

        List<Integer> timelog = new ArrayList<>();
        List<Integer> timelist = new ArrayList<>();
        Calendar rightNow=Calendar.getInstance();
        int day=rightNow.get(rightNow.DAY_OF_WEEK)-1;
        if(day==-1){
            day=6;
        }

        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        Integer n=Integer.parseInt(df1.format(new Date()));
//        System.out.println(n);

        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time=cal.getTime();
        String nn =  new SimpleDateFormat("yyyyMMdd").format(time);
        for(int i=0;i<7;i++){
            timelist.add(Integer.parseInt(df1.format(new Date(time.getTime() + i * 24 * 60 * 60 * 1000))));
            timelog.add(appointmentMapper.getUserDayAppointmentTime((String)session.getAttribute("loginUser"),df1.format(new Date(time.getTime() + i * 24 * 60 * 60 * 1000))));
        }

//        System.out.println(timelog);
        map.put("timelist",timelist);
        map.put("timelog",timelog);

        return "new/index";
    }


    @GetMapping("/index1")
    public String index1(Map<String,Object> map, HttpSession session, Model model){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");//设置日期格式
        String username = (String) session.getAttribute("loginUser");
        map.put("equipmentNum",equipmentMapper.getNumOfEquipment());
        map.put("projectNum",projectMapper.getProjectOfNum());
//        map.put("appointmentTime",appointmentMapper.getUserAppointmentAllTime(username));
        map.put("appointmentTime",appointmentMapper.getAppointmentRecentTimes(username,Integer.parseInt(df.format(new Date()))));


        List<Appointment> list =  appointmentMapper.getRecentAppointment((String)session.getAttribute("loginUser"),Integer.parseInt(df.format(new Date())));
        Collections.sort(list, new Comparator<Appointment>(){
            public int compare(Appointment p1, Appointment p2) {
                if(Integer.parseInt(p1.getInfo()) > Integer.parseInt(p2.getInfo())){
                    return 1;
                }
                if(Integer.parseInt(p1.getDate()) == Integer.parseInt(p2.getDate())){
                    return 0;
                }
                return -1;
            }
        });

        if(list.size()>0){
            Equipment equipment=equipmentMapper.getEquipmentById(list.get(0).getEquipmentID());
            Project project=projectMapper.getProjectById(list.get(0).getProjectID());
            String time1 = list.get(0).getTime();
            String time2 = String.valueOf(Integer.parseInt(time1)+1);
            map.put("Appointment",list.get(0));
            map.put("Equipment",equipment);
            map.put("Project",project);
            map.put("time1",time1);
            map.put("time2",time2);
            map.put("t1",list.size());
        }else{
            map.put("t1",list.size());
        }


        List<Appointment> list2 = appointmentMapper.getAppointmentByUsername((String) session.getAttribute("loginUser"));
        map.put("RecentProject",list2);


        //最近的项目
        List list3 = new ArrayList();
        List<Project> list4 = new ArrayList<Project>();
        int t=0;
        if(list2.size()>0){
            for (int i=0;i<list2.size();i++){
                if (!list3.contains(list2.get(i).getProjectID())) {
                    list3.add(list2.get(i).getProjectID());
                }
            }
            for (int i=0;i<list3.size();i++) {
                list4.add(projectMapper.getProjectById((Integer) list3.get(i)));
                t++;
                if (list4.size()==3){
                    break;
                }
            }
        }

        map.put("t2",t);
        model.addAttribute("recentProject",list4);


        if (list.size()>0){
            map.put("t3",list.size());
            model.addAttribute("recentAppointment",list);
        }else{
            map.put("t3",0);
        }


        Integer now = Integer.parseInt(df.format(new Date()));
        Integer using = appointmentMapper.getRecentOneHourAppointmentNum(now+1);
        Integer free = equipmentMapper.getNumOfEquipment() - using;
        map.put("using",using);
        map.put("free",free);

        List<Integer> timelog = new ArrayList<>();
        List<Integer> timelist = new ArrayList<>();
        Calendar rightNow=Calendar.getInstance();
        int day=rightNow.get(rightNow.DAY_OF_WEEK)-1;
        if(day==-1){
            day=6;
        }

        SimpleDateFormat df1 = new SimpleDateFormat("yyyyMMdd");
        Integer n=Integer.parseInt(df1.format(new Date()));
//        System.out.println(n);

        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time=cal.getTime();
        String nn =  new SimpleDateFormat("yyyyMMdd").format(time);
        for(int i=0;i<7;i++){
            timelist.add(Integer.parseInt(df1.format(new Date(time.getTime() + i * 24 * 60 * 60 * 1000))));
            timelog.add(appointmentMapper.getUserDayAppointmentTime((String)session.getAttribute("loginUser"),df1.format(new Date(time.getTime() + i * 24 * 60 * 60 * 1000))));
        }

//        System.out.println(timelog);
        map.put("timelist",timelist);
        map.put("timelog",timelog);

        return "index1";
    }


    @GetMapping("/analyse")
    public String analyse(Map map){
        map.put("equipmentRun",equipmentMapper.getNumOfEquipment());
        map.put("equipmentPause",equipmentMapper.getNumOfEquipmentP());
        map.put("equipmentB",equipmentMapper.getNumOfEquipmentB());
        return "new/analyse";
    }

    @GetMapping("/safe")
    public String safe(){
        return "new/safe";
    }
}
