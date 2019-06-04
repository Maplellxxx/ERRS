package com.waibao.project;

import com.waibao.project.bean.Log;
import com.waibao.project.mapper.LogMapper;
import com.waibao.project.mapper.ProjectMapper;
import com.waibao.project.mapper.UserMapper;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Methods {
    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    LogMapper logMapper;

    @Autowired
    UserMapper userMapper;

    public  boolean isValidDate(String str) {
        boolean convertSuccess=true;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            convertSuccess=false;
        }
        return convertSuccess;
    }


    public String alert(HttpServletResponse response, String msg, String url) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print("<script>alert('" + msg + "');window.location.href='"
                    + url + "';</script>");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
        return "common/suc";

    }



}
