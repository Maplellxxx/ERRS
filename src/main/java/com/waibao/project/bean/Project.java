package com.waibao.project.bean;

import com.waibao.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class Project {

    private Integer id;
    private String number;
    private String name;
    private String starttime;
    private String stoptime;
    private String state;
    private String originator;

    @Autowired
    UserMapper userMapper;



    public Integer getId() {
        return id;
    }

    public String getOriginator() {
        return originator;
    }

    public void setOriginator(String originator) {
        this.originator = originator;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getStoptime() {
        return stoptime;
    }

    public void setStoptime(String stoptime) {
        this.stoptime = stoptime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
