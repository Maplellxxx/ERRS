package com.waibao.project.controller;

import com.waibao.project.bean.Appointment;
import com.waibao.project.bean.Equipment;
import com.waibao.project.bean.Project;
import com.waibao.project.bean.User;
import com.waibao.project.mapper.AppointmentMapper;
import com.waibao.project.mapper.EquipmentMapper;
import com.waibao.project.mapper.ProjectMapper;
import com.waibao.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class TestController {

    @Autowired
    AppointmentMapper appointmentMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    EquipmentMapper equipmentMapper;

    @GetMapping("/t1")
    public String Test1(HttpSession session){

        return "test/t1";


    }




    @GetMapping("/t2")
    @ResponseBody
    public List<Appointment> test2(HttpSession session){
        //近期项目
        //获取最近的项目
        List<Appointment> list2 = appointmentMapper.getAppointmentByUsername((String) session.getAttribute("loginUser"));
        List list3 = new ArrayList();
        List<Project> list4 = new ArrayList<Project>();
        if(list2.size()>0){

            for (int i=0;i<list2.size();i++){
                if (!list3.contains(list2.get(i).getProjectID())) {
                    list3.add(list2.get(i).getProjectID());
                }
            }

            for (int i=0;i<list3.size();i++) {
                list4.add(projectMapper.getProjectById((Integer) list3.get(i)));
                if (list4.size()==3){
                    break;
                }
            }
        }

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");//设置日期格式
        List<Appointment> list =  appointmentMapper.getRecentAppointment((String)session.getAttribute("loginUser"),Integer.parseInt(df.format(new Date())));

        return list;



    }

}


