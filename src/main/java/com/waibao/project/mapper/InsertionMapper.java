package com.waibao.project.mapper;

import com.waibao.project.bean.Appointment;
import com.waibao.project.bean.Insertion;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InsertionMapper {
    @Select("select * from insertion")
    public List<Insertion> insertionList();

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into insertion(username,projectID,equipmentID,date,time,reason,result,original) values(#{username},#{projectID},#{equipmentID},#{date},#{time},#{reason},'审核中',#{original})")
    public int insertionAdd(Insertion insertion);

    @Select("select * from insertion where username=#{username}")
    public List<Insertion> myInsertionRequired(String username);

    @Select("select * from insertion where original=#{original}")
    public List<Insertion> InsertionRequired(String original);

    @Select("select * from insertion where id=#{id}")
    public Insertion getInsertion(Integer id);
    @Update("update insertion set result='通过' where id=#{id}")
    public int allowRequired(Integer id);
    @Update("update insertion set result='拒绝' where id=#{id}")
    public int denyRequired(Integer id);
    @Update("update insertion set result='过期' where id=#{id}")
    public int overtimeRequired(Integer id);
}
