package com.waibao.project.controller;

import com.waibao.project.bean.Equipment;
import com.waibao.project.bean.Log;
import com.waibao.project.mapper.EquipmentMapper;
import com.waibao.project.mapper.LogMapper;
import com.waibao.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class EquipmentController {
    @Autowired
    EquipmentMapper equipmentMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LogMapper logMapper;

    @GetMapping("/equipmentlist")
    public String euipmentList(Model model,HttpSession session){
//        if(session.getAttribute("admin")==null){
//            model.addAttribute("info",equipmentMapper.ListEnableEquipment());
//        }else{
//            model.addAttribute("info",equipmentMapper.ListEquipment());
//        }
        model.addAttribute("info",equipmentMapper.ListEnableEquipment());
        return "new/equipmentList";
    }

    @GetMapping("/equipmentlist/all")
    public String euipmentListAll(Model model,HttpSession session){
        model.addAttribute("info",equipmentMapper.ListEquipment());
        return "new/equipmentListAll";
    }

    @GetMapping("/equipmentlist/running")
    public String equipmentListRunning(Model model,HttpSession session){
        model.addAttribute("info",equipmentMapper.ListEnableEquipment());
        return "new/equipmentListRunning";
    }

    @GetMapping("/equipmentlist/pause")
    public String equipmentListPause(Model model,HttpSession session){
        model.addAttribute("info",equipmentMapper.ListPauseEquipment());
        return "new/equipmentListPause";
    }


    @GetMapping("/equipmentlist/scrap")
    public String equipmentListScrap(Model model,HttpSession session){
        model.addAttribute("info",equipmentMapper.ListScrapEquipment());
        return "new/equipmentListScrap";
    }



    @GetMapping("/equipment/{id}")
    public String equipmentInfo(@PathVariable("id")Integer id,
                                Map<String,Object> map){
        map.put("equipment",equipmentMapper.getEquipmentById(id));
        return "new/equipmentInfo";

    }

    @GetMapping("/admin/equipment/update/{id}")
    public String equipmentUpdate(@PathVariable("id")Integer id,
                                  Map<String,Object>map){


        map.put("equipment",equipmentMapper.getEquipmentById(id));
        return "equipment/equipmentUpdate";
    }

    @PostMapping("admin/equipment/update")
    public String equipmentUpdatePost(@RequestParam("id")Integer id,
                                      @RequestParam("number")String number,
                                      @RequestParam("name")String name,
                                      @RequestParam("brand")String brand,
                                      @RequestParam("model")String model,
                                      @RequestParam("location")String location,
                                      @RequestParam("state")String state,
                                      Map<String,Object> map, HttpSession session){
        Equipment equipment = equipmentMapper.getEquipmentById(id);
        if(equipmentMapper.ifNumberExist(number)==1&&!number.equals(equipment.getNumber())){
            map.put("equipment",equipment);
            map.put("msg","编号已存在");
            return "equipment/equipmentUpdate";
        }
        Equipment e = new Equipment();
        e.setId(id);
        e.setNumber(number);
        e.setName(name);
        e.setBrand(brand);
        e.setModel(model);
        e.setLocation(location);
        e.setState(state);
        equipmentMapper.updateEquipment(e);

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
        log.setOperation("修改设备信息");
        log.setObject(Integer.valueOf(id));
        log.setType("equipment");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);

        return "redirect:/equipment/"+id;
    }

    @GetMapping("/admin/equipment/changeState/{id}")
    public String changeState(@PathVariable("id")Integer id,
                              Map<String,Object>map,HttpSession session){
        Equipment equipment = equipmentMapper.getEquipmentById(id);
        String message;
        if(equipment.getState().equals("报废")){
            map.put("msg","该设备已报废");
            return "error";
        } else if(equipment.getState().equals("启用")){
            String s = "停用";
            message="停用设备";
            equipmentMapper.changeState(id,s);
        }else{
            String s = "启用";
            message="启用设备";
            equipmentMapper.changeState(id,s);
        }

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
        log.setOperation(message);
        log.setObject(Integer.valueOf(id));
        log.setType("equipment");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);

        if(equipment.getState().equals("启用")){
            return "redirect:/equipmentlist/pause";
        }else{
            return "redirect:/equipmentlist/running";
        }


    }


    @GetMapping("/admin/equipment/changeStateIndex/{id}")
    public String changeStateIndex(@PathVariable("id")Integer id,
                              Map<String,Object>map,HttpSession session){
        Equipment equipment = equipmentMapper.getEquipmentById(id);
        String message;
        if(equipment.getState().equals("报废")){
            map.put("msg","该设备已报废");
            return "error";
        } else if(equipment.getState().equals("启用")){
            String s = "停用";
            message="停用设备";
            equipmentMapper.changeState(id,s);
        }else{
            String s = "启用";
            message="启用设备";
            equipmentMapper.changeState(id,s);
        }

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
        log.setOperation(message);
        log.setObject(Integer.valueOf(id));
        log.setType("equipment");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);

        return "redirect:/equipmentlist/all";
//        if(equipment.getState().equals("启用")){
//            return "redirect:/equipmentlist/pause";
//        }else{
//            return "redirect:/equipmentlist/running";
//        }


    }

    @GetMapping("/admin/equipment/scrap/{id}")
    public String equipmentScrap(@PathVariable("id")Integer id,
                                 Map<String,Object>map,HttpSession session){
        Equipment equipment = equipmentMapper.getEquipmentById(id);
        if(equipment.getState().equals("报废")){
            map.put("msg","该设备已报废");
            return "error";
        }

        String s = "报废";
        equipmentMapper.changeState(id,s);
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
        log.setOperation("报废设备");
        log.setObject(Integer.valueOf(id));
        log.setType("equipment");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);
        return "redirect:/equipmentlist/scrap";
    }

    @GetMapping("/admin/equipment/add")
    public String equipmentAdd(){
        return "new/equipmentAdd";
    }

    @PostMapping("/admin/equipment/add")
    public String equipmentAddPost(@RequestParam("number")String number,
                                   @RequestParam("name")String name ,
                                   @RequestParam("brand")String brand ,
                                   @RequestParam("model")String model ,
                                   @RequestParam("location")String location ,
                                   Map<String,Object> map, HttpSession session){

        if (equipmentMapper.ifNumberExist(number)==1){
            map.put("msg","编号已经存在");
            return "equipment/equipmentAdd";
        }
        Equipment equipment=new Equipment();
        equipment.setNumber(number);
        equipment.setName(name);
        equipment.setBrand(brand);
        equipment.setModel(model);
        equipment.setLocation(location);
        equipment.setState("启用");
        equipmentMapper.insertEquipment(equipment);
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
        log.setOperation("添加设备");
        log.setObject(Integer.valueOf(equipmentMapper.getEquipmentID(number)));
        log.setType("equipment");
        log.setTime(df.format(new Date()));
        logMapper.addLog(log);

        return "redirect:/equipmentlist";
    }


//    @GetMapping("/t1")
//    @ResponseBody
//    public String test(){
//        return String.valueOf(equipmentMapper.getEquipmentID("1"));
//    }


}
