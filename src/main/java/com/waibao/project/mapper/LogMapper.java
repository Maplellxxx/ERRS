package com.waibao.project.mapper;

import com.waibao.project.bean.Log;
import com.waibao.project.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {
    @Select("select * from log ORDER BY id DESC")
    public List<Log> ListLog();

    @Select("select * from log where id=#{id}")
    public Log getLogById(Integer id);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into log(authority,userID,operation,object,type,time) values(#{authority},#{userID},#{operation},#{object},#{type},#{time})")
    public int addLog(Log log);


}
