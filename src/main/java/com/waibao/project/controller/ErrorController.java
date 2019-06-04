package com.waibao.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class ErrorController {
    @GetMapping("/AppointError")
    public String error(){
        return "msg";
    }


//    @GetMapping("/errorinfo")
//    public String errorinfo(HttpServletResponse response) throws IOException {
//        response.setContentType("text/html;charset=utf-8");
//        PrintWriter out = response.getWriter();
//        out.print("<script language=\"javascript\">alert('登录失败！');</script>");
//        return "redirect:/main.html";
//    }
}
