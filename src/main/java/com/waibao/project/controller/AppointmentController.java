package com.waibao.project.controller;

import com.waibao.project.Methods;
import com.waibao.project.bean.*;
import com.waibao.project.mapper.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class AppointmentController {
    @Autowired
    LogMapper logMapper;

    @Autowired
    AppointmentMapper appointmentMapper;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    EquipmentMapper equipmentMapper;

    @Autowired
    UserMapper userMapper;

    @GetMapping("/appointment/{EquipmentID}/{date}")
    public String appointmentByDay(@PathVariable("EquipmentID")Integer equipmentID,RedirectAttributes attributes,
                                   @PathVariable("date")String date, Map<String,Object>map, Model model){
        Equipment equipment = equipmentMapper.getEquipmentById(equipmentID);
        Methods methods = new Methods();
        if(!equipment.getState().equals("启用")){
            attributes.addFlashAttribute("msg","该设备不可用");
            return "redirect:/AppointError";
        }
        if(!methods.isValidDate(date)){
            attributes.addFlashAttribute("msg","无效日期");
            return "redirect:/AppointError";
        }
        List<Appointment> t = appointmentMapper.getAppointmentByequipmentIdAndDate(equipmentID,date);
        ArrayList li = new ArrayList();
        ArrayList datelist = new ArrayList();
        for(int i=0 ; i<24;i++){
            li.add("1");
        }
        int j;
        for(int i=0;i<24;i++){
            for(j=0;j<t.size();j++){
                if (i==Integer.valueOf(t.get(j).getTime())){
                    li.set(i,"0");
                }
            }
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        String time=df.format(new Date());
        map.put("equipment",equipment);
        map.put("time",Integer.parseInt(time));
        map.put("li",li);
        datelist.add(date);
        map.put("datelist",datelist);
        map.put("equipmentID",equipmentID);
        map.put("date",date);
        int a,b,c;
        int d = Integer.parseInt(date);
        a=d/10000;
        b=(d%10000)/100;
        c=d%100;
        String datetime = String.valueOf(a)+"-"+String.valueOf(b)+"-"+String.valueOf(c);
        map.put("datetime",datetime);
        model.addAttribute("project",projectMapper.getRunningProject());
        return "appointment/appointment";
    }


    @PostMapping("/appoint/{equipmentID}/{date}")
    public String appoint(@RequestParam("l")List l,
                          @RequestParam("project")String project,
                          @PathVariable("equipmentID")Integer equipmentID, RedirectAttributes attributes,
                          @PathVariable("date")String date, HttpSession session){
        if(project.equals("")){
            attributes.addFlashAttribute("msg", "请选择项目");
            return "redirect:/appointment/"+equipmentID+"/"+date;
        }
        //判断选取时间段是否为空
        int t = 0;
        for(int i=0;i<l.size();i++){
            if(Integer.parseInt((String)l.get(i))!=0){
                t++;
            }
        }
        if (t==0){
            attributes.addFlashAttribute("msg", "请选择预约时间");
            return "redirect:/appointment/"+equipmentID+"/"+date;
        }
        ArrayList li = new ArrayList();
        Appointment appointment = new Appointment();

        appointment.setProjectID(projectMapper.getProject(project).getId());
        appointment.setUsername((String)session.getAttribute("loginUser"));
        appointment.setEquipmentID(equipmentID);
        for(int i=0;i<l.size();i++){
            String j = (String) l.get(i);
            Integer d = Integer.parseInt(j);
            appointment.setDate(String.valueOf(d/100));
            Integer dd = d%100;
            if (d/100==0){
                continue;
            }
            if (dd<10){
                appointment.setTime('0'+String.valueOf(dd));
            }else{
                appointment.setTime(String.valueOf(dd));
            }
            String info = appointment.getDate()+appointment.getTime();
            appointment.setInfo(info);

            if (appointmentMapper.ifTimeAppointed(appointment)==0){
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
                log.setOperation("预约设备");
                log.setObject(appointment.getId());
                log.setType("appointment");
                log.setTime(df.format(new Date()));
                logMapper.addLog(log);

            }else{
                li.add(l.get(i));
            }
        }
        if (li.size()>0){
            attributes.addFlashAttribute("msg", li+"已被选择");
        }
        return "redirect:/appointment/"+equipmentID+"/"+date;
    }


    @GetMapping("/appointment/{equipmentID}/today")
    public String TheDay(@PathVariable("equipmentID")Integer equipmentID){
        int y,m,d;
        Calendar cal=Calendar.getInstance();
        y=cal.get(Calendar.YEAR);
        m=cal.get(Calendar.MONTH)+1;
        d=cal.get(Calendar.DATE);
        int day = y*10000+m*100+d;
        String date = String.valueOf(day);
        return "redirect:/appointment/"+equipmentID+"/"+date;
    }

    @GetMapping("/appointment/{equipmentID}/{date}/before")
    public String TheDayBefore(@PathVariable("equipmentID")Integer equipmentID,
                               @PathVariable("date")String startTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date startdate = sdf.parse(startTime);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(startdate);
            calendar.add(calendar.DATE,-1);
            Date d = calendar.getTime();
            String date = sdf.format(d);
            return "redirect:/appointment/"+equipmentID+"/"+date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        return "redirect:/appointment/"+equipmentID+"/"+date;
    }

    @GetMapping("/appointment/{equipmentID}/{date}/after")
    public String TheDayAfter(@PathVariable("equipmentID")Integer equipmentID,
                               @PathVariable("date")String startTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date startdate = sdf.parse(startTime);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(startdate);
            calendar.add(calendar.DATE,1);
            Date d = calendar.getTime();
            String date = sdf.format(d);
            return "redirect:/appointment/"+equipmentID+"/"+date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        return "redirect:/appointment/"+equipmentID+"/"+date;
    }

    //用于ajax获取预约信息
    @PostMapping("/appointmentInfo/{equipmentID}")
    @ResponseBody
    public List appointInfo(@RequestParam("time")String data,
                   @PathVariable("equipmentID")Integer equipmentID){
        Integer d = Integer.parseInt(data)/100;
        Integer t = Integer.parseInt(data)%100;
        String date = String.valueOf(d);
        String time;
        if(t<10){
            time = '0'+String.valueOf(t);
        }else{
            time=String.valueOf(t);
        }
        Appointment appointment = appointmentMapper.getAppointmentByAjax(equipmentID,date,time);
        User user = userMapper.getUser(appointment.getUsername());
        Project project = projectMapper.getProjectById(appointment.getProjectID());
        ArrayList list = new ArrayList();
        list.add(appointment.getUsername());
        list.add(user.getName());
        list.add(project.getNumber());
        list.add(project.getName());
        return list;
    }




    @GetMapping("/user/appointment/{username}")
    public String userAppointTime(@PathVariable("username")String username,Model model,Map map){
        List<Appointment> l = appointmentMapper.getUserAppointmentTime(username);
        List<Put> list = new ArrayList<>();
        Put put = new Put();
        Equipment equipment;
        Project project;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");//设置日期格式
        System.out.println(df.format(new Date()));
//        list.add()
        for(int i=l.size()-1;i>=0;i--){
            equipment = equipmentMapper.getEquipmentById(l.get(i).getEquipmentID());
            project = projectMapper.getProjectById(l.get(i).getProjectID());
            put = new Put();
            put.setInfo1(equipment.getNumber());
            put.setInfo2(equipment.getName());
            put.setInfo3(equipment.getModel());
            put.setInfo4(equipment.getLocation());
            int a,b,c;
            int d = Integer.parseInt(l.get(i).getDate());
            a=d/10000;
            b=(d%10000)/100;
            c=d%100;
            String datetime = String.valueOf(a)+"-"+String.valueOf(b)+"-"+String.valueOf(c);
            put.setInfo5(datetime);
            put.setInfo6(l.get(i).getTime());
            put.setInfo7(project.getName());
            String time = l.get(i).getDate()+l.get(i).getTime();
            if (Integer.parseInt(time)<Integer.parseInt(df.format(new Date()))){
                put.setInfo8("已完成");
            }else{
                put.setInfo8("未完成");
            }

            list.add(put);
        }
//        appointmentMapper.getUserAppointmentTime(username)
        model.addAttribute("times",list);
        map.put("username",username);
//        System.out.println(list.get(0).getInfo1());
//        System.out.println(list.get(0).getInfo2());
//        System.out.println(list.get(1).getInfo1());
//        System.out.println(list.get(1).getInfo2());
//        return "appointment/personAppointment";
        return "new/appointmentList";
    }



    @GetMapping("/appointment/day/{date}")
    public String dayAppointment(@PathVariable("date")String date, HttpSession session,Map map,Model model){
        String username = (String)session.getAttribute("loginUser");
        int a,b,c;
        int d = Integer.parseInt(date);
        a=d/10000;
        b=(d%10000)/100;
        c=d%100;
        String datetime = String.valueOf(a)+"-"+String.valueOf(b)+"-"+String.valueOf(c);
        map.put("datetime",datetime);
        map.put("date",date);

        List<Appointment> li = appointmentMapper.getUserDayAppointment(username,date);
        List<Appointment> list = new ArrayList<>();

        for(int i=0;i<li.size();i++){

            if(i!=0){
                if(!li.get(i).getTime().equals(li.get(i-1).getTime())){
                    list.add(li.get(i));
                }
            }else{
                list.add(li.get(i));
            }
        }

        List<Appointment> list2 = new ArrayList<>();
        List<Equipment> list3 = new ArrayList<>();
        List<Project> list4 = new ArrayList<>();
        List<Integer> l = new ArrayList<>();
        int t=0;
        if (list.size()==0){
            for(int i=0;i<24;i++){
                list2.add(new Appointment());
                list3.add(new Equipment());
                list4.add(new Project());
            }
        }else{
            for(int i=0;i<24;i++){
                if(i==Integer.parseInt(list.get(t).getTime())){
                    list2.add(list.get(t));
                    list3.add(equipmentMapper.getEquipmentById(list.get(t).getEquipmentID()));
                    list4.add(projectMapper.getProjectById(list.get(t).getProjectID()));
                    if (t<list.size()-1){
                        t++;
                    }
                }else {
                    list2.add(new Appointment());
                    list3.add(new Equipment());
                    list4.add(new Project());
                }
            }
        }
        map.put("username",username);
        map.put("alist",list2);
        map.put("elist",list3);
        map.put("plist",list4);


        int day = Integer.parseInt(date);
        int m=day%10000/100;
        int dd=day%100;

        if (m==1){
            map.put("month","January");
        }else if (m==2){
            map.put("month","February");
        }else if (m==3){
            map.put("month","March");
        }else if (m==4){
            map.put("month","April ");
        }else if (m==5){
            map.put("month","May");
        }else if (m==6){
            map.put("month","June");
        }else if (m==7){
            map.put("month","July");
        }else if (m==8){
            map.put("month","August");
        }else if (m==9){
            map.put("month","September");
        }else if (m==10){
            map.put("month","October");
        }else if (m==11){
            map.put("month","November");
        }else if (m==12){
            map.put("month","December");
        }
        map.put("day",dd);


       try{
           String str = date;
           Calendar calendar = Calendar.getInstance();
           SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
           calendar.setTime(sdf.parse(str));
           int i =calendar.get(Calendar.DAY_OF_WEEK);
           System.out.println(i);
           if(i == 1){
               map.put("weekday","星期天 Sunday");
           }else{
               System.out.println("今天是星期"+(i-1));
               if (i-1==1){
                   map.put("weekday","星期一 Monday");
               }else if(i-1==2){
                   map.put("weekday","星期二 Tuesday");
               }else if(i-1==3){
                   map.put("weekday","星期三 Wednesday");
               }else if(i-1==4){
                   map.put("weekday","星期四 Thursday");
               }else if(i-1==5){
                   map.put("weekday","星期五 Friday");
               }else if(i-1==6){
                   map.put("weekday","星期六 Saturday");
               }




           }
       }catch (Exception e){
           // TODO Auto-generated catch block
           e.printStackTrace();
           return "error";
       }


        return "appointment/dayAppointment";
    }


    @GetMapping("/appointment/day/{date}/before")
    public String dayBefore(@PathVariable("date")String startTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date startdate = sdf.parse(startTime);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(startdate);
            calendar.add(calendar.DATE,-1);
            Date d = calendar.getTime();
            String date = sdf.format(d);
            return "redirect:/appointment/day/"+date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        return "redirect:/appointment/day/"+date;
    }


    @GetMapping("/appointment/day/{date}/after")
    public String dayAfter(@PathVariable("date")String startTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        try {
            Date startdate = sdf.parse(startTime);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(startdate);
            calendar.add(calendar.DATE,1);
            Date d = calendar.getTime();
            String date = sdf.format(d);
            return "redirect:/appointment/day/"+date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date = new Date();
        return "redirect:/appointment/day/"+date;
    }
}
