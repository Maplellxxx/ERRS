package com.waibao.project.mapper;

import com.waibao.project.bean.Project;
import com.waibao.project.bean.ProjectAdmin;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProjectMapper {
    //全部项目
    @Select("select * from project")
    public List<Project> projectList();
    //进行中的项目
    @Select("select * from project where state='进行中'")
    public List<Project> RunningRrojectList();
    //暂停的项目
    @Select("select * from project where state='暂停'")
    public List<Project> PauseProjectList();
    //完成的项目
    @Select("select * from project where state='完成'")
    public List<Project> CompleteProjectList();

    @Select("select count(*) from project where state='进行中'")
    public Integer getProjectOfNum();

    @Update("update project set state='进行中' where id=#{id}")
    public int ProjectRun(Integer id);

    @Update("update project set state='暂停' where id=#{id}")
    public int ProjectPause(Integer id);

    @Update("update project set state='完成',stoptime=#{stoptime} where id=#{id}")
    public int ProjectComplete(Integer id,String stoptime);

    @Select("select * from project where id=#{id}")
    public Project getProjectById(Integer id);

    @Select("select * from project where number=#{number}")
    public Project getProject(String number);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into project(number,name,starttime,state,originator) values(#{number},#{name},#{starttime},#{state},#{originator})")
    public int projectAdd(Project project);

    @Select("select count(*) from project where number=#{number}")
    public int ifProjectNumberExist(String number);

    @Select("select * from project where state='进行中'")
    public List<Project> getRunningProject();

    @Select("select * from project where state='完成'")
    public List<Project> getCompleteProject();

    @Select("select * from project where state='暂停'")
    public List<Project> getPauseProject();

    @Select("select * from project where originator=#{originator}")
    public List<Project> getEstablish(String originator);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("insert into projectAdmin(number,username) values(#{number},#{username})")
    public int addProjectAdmin(ProjectAdmin projectAdmin);

    @Select("select * from projectAdmin where number=#{number}")
    public List<ProjectAdmin> getProjectAdmin(String number);

    @Select("select count(*) from projectAdmin where number=#{number} and username=#{username}")
    public int ifProjectAdminAlreadyExist(String number,String username);

    @Select("select count(*) from projectAdmin where number=#{number} and username=#{username}")
    public int ifProjectAdmin(String number,String username);



}
