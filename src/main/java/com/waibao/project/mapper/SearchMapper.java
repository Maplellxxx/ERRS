package com.waibao.project.mapper;

import com.waibao.project.bean.Equipment;
import com.waibao.project.bean.Project;
import com.waibao.project.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SearchMapper {
    //设备查询
    @Select("select * from equipment where number like concat('%',#{keyword},'%')")
    public List<Equipment> getEquipmentListByNumber(String keyword);
    @Select("select * from equipment where name like concat('%',#{keyword},'%')")
    public List<Equipment> getEquipmentListByName(String keyword);
    @Select("select * from equipment where brand like concat('%',#{keyword},'%')")
    public List<Equipment> getEquipmentListByBrand(String keyword);
    @Select("select * from equipment where location like concat('%',#{keyword},'%')")
    public List<Equipment> getEquipmentListByLocation(String keyword);
    //项目查询
    @Select("select * from project where number like concat('%',#{keyword},'%')")
    public List<Project> getPorjectListByNumber(String keyword);
    @Select("select * from projct where name like concat('%',#{keyword},'%')")
    public List<Project> getProjectListByName(String keyword);
    //用户查询
    @Select("select * from user where username concat('%',#{keyword},'%')")
    public List<User> getUserListByUsername(String keyword);






}
