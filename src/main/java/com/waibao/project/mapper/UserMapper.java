package com.waibao.project.mapper;

import com.waibao.project.bean.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user")
    public List<User> ListUser();

    @Select("select * from user where username=#{username}")
    public User getUser(String username);

    @Select("select * from user where id=#{id}")
    public User getUserById(Integer id);

    @Update("update user set username=#{username},password=#{password},authority=#{authority},name=#{name},gender=#{gender},projectteam=#{projectteam},department=#{department},email=#{email} where id =#{id}")
    public int updateUser(User user);

    @Update("update user set password=#{password} where id=#{id}")
    public int changePassword(String password,Integer id);

    @Select("select count(*) from user where password=#{password} and id=#{id}")
    public int ifPasswordTrue(String password,Integer id);

    @Select("select count(*) from user where username=#{username} and password=#{password}")
    public int ifLogin(String username,String password);

    @Select("select count(*) from systemAdmin where username=#{username} and password1=#{password1} and password2=#{password2}")
    public int ifSystemAdminLogin(String username,String password1,String password2);

    @Select("select count(*) from user where username=#{username} and password1=#{password1} and password2=#{password2}")
    public int adminlogin(String username,String password1,String password2);

    @Select("select * from user where username=#{username}")
    public int getID(String username);

    @Select("select * from systemadmin where username=#{username}")
    public int getSystemAdminID(String username);

    //自动生成自增id
    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into user(username,password,authority,name,gender,projectteam,department,email) values(#{username},#{password},#{authority},#{name},#{gender},#{projectteam},#{department},#{email})")
    public int insertUser(User user);

    @Select("select count(*) from user where username=#{username}")
    public int IfUsernameExist(String username);

    @Delete("delete from user where id=#{id}")
    public int deleteUserById(Integer id);

    @Select("select count(*) from user where username=#{username} and authority='管理员'")
    public int ifAdmin(String username);

}
