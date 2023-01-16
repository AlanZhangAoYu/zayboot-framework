package controller;

import cn.zay.zayboot.annotation.ioc.Autowired;
import cn.zay.zayboot.annotation.ioc.Component;
import cn.zay.zayboot.annotation.springmvc.*;
import com.alibaba.fastjson2.JSON;
import pojo.Student;

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
    public String setStudentInfo(@RequestParam("name") String name){
        return "设置学生属性成功! 姓名为: "+name;
    }
    @GetMapping("/getStudentInfoById/{id}")
    public String getStudentInfoById(@PathVariable("id") String id){
        return "获取id为"+id+"的学生的信息";
    }
}
