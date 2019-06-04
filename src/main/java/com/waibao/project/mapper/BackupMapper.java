package com.waibao.project.mapper;

import com.waibao.project.bean.Backup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BackupMapper {
    @Select("select * from backup")
    public List<Backup> getBackupList();

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Select("insert into backup(time,info) values(#{datetime},#{info})")
    public Integer BackupAdd(String datetime,String info);
}
