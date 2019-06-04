package com.waibao.project.mapper;

import com.waibao.project.bean.SystemAdmin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SystemAdminMapper {
    @Select("select * from systemadmin")
    public List<SystemAdmin> getAllSystemAdmin();

    @Select("select * from systemadmin where username=#{username}")
    public SystemAdmin getSystemAdminByUsername(String username);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into systemadmin(username,password1,password2,name1,name2,gender1,gender2,projectteam1,projectteam2,department1,department2,email1,email2) " +
            "values(#{username},#{password1},#{password2},#{name1},#{name2},#{gender1},#{gender2},#{projectteam1},#{projectteam2},#{department1},#{department2},#{email1},#{email2})")
    public int systemAdminRegister(SystemAdmin systemAdmin);

    @Select("select count(*) from systemadmin where username=#{username}")
    public int ifSystemAdminExist(String username);
}
