package com.waibao.project.mapper;

import com.waibao.project.bean.Equipment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EquipmentMapper {
    @Select("select * from equipment")
    public List<Equipment> ListEquipment();

    @Select("select * from equipment where state = '启用'")
    public List<Equipment> ListEnableEquipment();
    @Select("select * from equipment where state = '停用'")
    public List<Equipment> ListPauseEquipment();
    @Select("select * from equipment where state = '报废'")
    public List<Equipment> ListScrapEquipment();

    @Select("select * from equipment where number=#{number}")
    public int getEquipmentID(String number);

    @Select("select * from equipment where id=#{id}")
    public Equipment getEquipmentById(Integer id);

    @Select("select * from equipment where number=#{number}")
    public Equipment getEquipmentByNumber();

    @Select("select count(*) from equipment where id=#{id} and state='启用'")
    public int ifEnable();

    @Select("select count(*) from equipment where number=#{number}")
    public int ifNumberExist(String number);

    @Delete("delete from equipment where id=#{id}")
    public int deleteEquipment();

    @Update("update equipment set number=#{number},name=#{name},brand=#{brand},model=#{model},location=#{location},state=#{state} where id=#{id}")
    public int updateEquipment(Equipment equipment);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into equipment(number,name,brand,model,location,state) values(#{number},#{name},#{brand},#{model},#{location},#{state})")
    public int insertEquipment(Equipment equipment);

    @Update("update equipment set state=#{state} where id = #{id}")
    public int changeState(Integer id,String state);

    @Select("select count(*) from equipment where state='启用'")
    public int getNumOfEquipment();
    @Select("select count(*) from equipment where state='停用'")
    public int getNumOfEquipmentP();
    @Select("select count(*) from equipment where state='报废'")
    public int getNumOfEquipmentB();


}
