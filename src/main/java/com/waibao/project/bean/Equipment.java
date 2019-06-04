package com.waibao.project.bean;

public class Equipment {
    private Integer id;
    private String number;
    private String name;
    private String brand;
    private String model;
    private String location;
    private String state;

    public Integer getId() {
        return id;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

//    public String changeState(){
//        if (state.equals("启用")){
//            return "停用";
//        }else if(state.equals("停用")){
//            return "启用";
//        }else{
//            return "报废";
//        }
//    }
}
