package com.waibao.project.mapper;

import com.waibao.project.bean.Appointment;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface AppointmentMapper {
    @Select("select * from appointment")
    public List<Appointment> appointmentList();

    @Select("select * from appointment where username=#{username}")
    public List<Appointment> appointmentListByUsername(String username);

    @Select("select * from appointment where id=#{id}")
    public Appointment getAppointmentById(Integer id);
    @Select("select * from appointment where projectID=#{projectID}")
    public List<Appointment> getAppointmentByprojectID(Integer projectID);
    @Select("select * from appointment where equipmentID=#{equipmentID}")
    public List<Appointment> getAppointmentByequipmentID(Integer equipmentID);
    @Select("select * from appointment where date=#{date}")
    public List<Appointment> getAppointmentByDate(String date);
    @Select("select * from appointment where equipmentID=#{equipmentID} and date=#{date}")
    public List<Appointment> getAppointmentByequipmentIdAndDate(Integer equipmentID, String date);

    @Select("select count(*) from appointment where equipmentID=#{equipmentID} and date=#{date} and time=#{time}")
    public int ifTimeAppointed(Appointment appointment);

    @Select("select * from appointment where equipmentID=#{equipmentID} and date=#{date} and time=#{time}")
    public Appointment getAppointmentByAjax(Integer equipmentID,String date,String time);


    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into appointment(username,projectID,equipmentID,date,time,info) values(#{username},#{projectID},#{equipmentID},#{date},#{time},#{info})")
    public int appointment(Appointment appointment);

    @Delete("delete from appointment where equipmentID=#{equipmentID} and date=#{date} and time=#{time}")
    public int deleteAppointment(Integer equipmentID,String date,String time);

    @Select("select * from appointment where equipmentID=#{equipmentID} and date=#{date} and time=#{time}")
    public Appointment getAppointmentByEquipmentIDAndDateAndTime(Integer equipmentID,String date,String time);

    @Select("select * from appointment where username=#{username}")
    public List<Appointment> getUserAppointmentTime(String username);

    @Select("select * from appointment where username=#{username} order by id desc")
    public List<Appointment> getAppointmentByUsername(String username);

    @Select("select count(*) from appointment where username=#{username}")
    public Integer getUserAppointmentAllTime(String username);

    @Select("select * from appointment where username=#{username} and info>=#{info}")
    public List<Appointment> getRecentAppointment(String username,Integer info);

    @Select("select count(*) from appointment where username=#{username} and info>=#{info}")
    public Integer getAppointmentRecentTimes(String username,Integer info);

    @Select("select count(*) from appointment where info=#{info}")
    public Integer getRecentOneHourAppointmentNum(Integer info);

    @Select("select count(*) from appointment where username=#{username} and date=#{date}")
    public Integer getUserDayAppointmentTime(String username,String date);

    @Select("select * from appointment where username=#{username} and date=#{date} order by time")
    public List<Appointment> getUserDayAppointment(String username,String date);

}
