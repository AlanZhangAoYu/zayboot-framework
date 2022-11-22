package cn.zay.demo.java.controller;

import cn.zay.demo.java.pojo.Student;
import cn.zay.zayboot.annotation.ioc.Autowired;
import cn.zay.zayboot.annotation.ioc.Component;
import cn.zay.zayboot.annotation.springmvc.GetMapping;
import cn.zay.zayboot.annotation.springmvc.PostMapping;
import cn.zay.zayboot.annotation.springmvc.RestController;
import com.alibaba.fastjson2.JSON;

/**
 * @author ZAY
 */
@Component
@RestController("/student")
public class StudentController {
    @Autowired
    Student ZAY;
    @GetMapping("/getStudentInfo")
    public String getStudentInfo(){
        return JSON.toJSONString(ZAY);
    }
    @PostMapping("/setStudentInfo")
    public String setStudentInfo(){
        return "设置学生属性成功!";
    }
}
