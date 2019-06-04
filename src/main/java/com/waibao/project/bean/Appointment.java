package com.waibao.project.bean;

import com.waibao.project.mapper.AppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private Integer id;
    private String username;
    private Integer projectID;
    private Integer equipmentID;
    private String date;
    private String time;
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Autowired
    AppointmentMapper appointmentMapper;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(Integer equipmentID) {
        this.equipmentID = equipmentID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


//    public List getAppointmentTime(Integer equipmentID,String date){
//        ArrayList t = appointmentMapper.getAppointmentByequipmentIdAndDate(equipmentID,date);
//        ArrayList li = new ArrayList();
//        for(int i=0;i<24;i++){
//
//        }
//        return t;
//    }


}
