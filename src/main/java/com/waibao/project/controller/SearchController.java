package com.waibao.project.controller;

import com.waibao.project.bean.Equipment;
import com.waibao.project.mapper.SearchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    SearchMapper searchMapper;


    @GetMapping("/search")
    public String searchIndex(){
        return "search/searchIndex";
    }

    @PostMapping("/searchInfo")
    public String searchInfo(@RequestParam("keyword")String keyword, Model model,
                           @RequestParam("condition")String condition){
        if(condition.equals("全局模糊")){
            List<Equipment> list1 = searchMapper.getEquipmentListByNumber(keyword);
            List<Equipment> list2 = searchMapper.getEquipmentListByName(keyword);
            List<Equipment> list3 = searchMapper.getEquipmentListByBrand(keyword);
            List<Equipment> list4 = searchMapper.getEquipmentListByLocation(keyword);
            list1.addAll(list2);
            list1.addAll(list3);
            list1.addAll(list4);
            model.addAttribute("info",list1);
            return "search/searchInfo";
        }else if(condition.equals("设备编号")){
            List<Equipment> list1 = searchMapper.getEquipmentListByNumber(keyword);
            model.addAttribute("info",list1);
            return "search/searchInfo";
        }else if(condition.equals("使用位置")){
            List<Equipment> list1 = searchMapper.getEquipmentListByLocation(keyword);
            model.addAttribute("info",list1);
            return "search/searchInfo";
        }else{
            return "search/searchInfo";
        }
    }


    @GetMapping("/searchinfo")
    public String search(){
        return "new/search";
    }
}
