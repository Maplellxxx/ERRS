package com.waibao.project.controller;

import com.waibao.project.mapper.LogMapper;
import com.waibao.project.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class LogController {
    @Autowired
    LogMapper logMapper;

    @GetMapping("/admin/loglist")
    public String logList(Model model){
        model.addAttribute("info",logMapper.ListLog());
        return "new/loglist";
    }

    @GetMapping("/admin/log/{id}")
    public String logInfo(@PathVariable("id")Integer id,
                          Map<String,Object>map){
        map.put("log",logMapper.getLogById(id));
        return "log/logInfo";
    }

}
