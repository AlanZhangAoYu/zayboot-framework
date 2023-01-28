package controller;

import cn.zay.zayboot.annotation.ioc.Autowired;
import cn.zay.zayboot.annotation.ioc.Component;
import cn.zay.zayboot.annotation.springmvc.*;
import pojo.AjaxResult;
import pojo.Student;

import java.util.HashMap;

/**
 * @author ZAY
 */
@Component
@RestController("/student")
public class StudentController {
    @Autowired
    Student ZAY;
    @GetMapping("/getStudentInfo")
    public AjaxResult getStudentInfo(){
        return AjaxResult.success(ZAY);
    }
    @GetMapping("/getStudentInfoByPara")
    public AjaxResult getStudentInfo(@RequestBody Student student){
        return AjaxResult.success(student);
    }
    @PostMapping("/setStudentInfo")
    public AjaxResult setStudentInfo(@RequestParam("name") String name,@RequestParam("age") Integer age){
        HashMap<String,String> resultMap = new HashMap<>(1);
        resultMap.put("message","设置学生属性成功! 姓名为: "+name+", 年龄为: "+age);
        return AjaxResult.success(resultMap);
    }
    @GetMapping("/getStudentInfoById/{id}")
    public AjaxResult getStudentInfoById(@PathVariable("id") String id){
        HashMap<String,String> resultMap = new HashMap<>(1);
        resultMap.put("message","获取id为"+id+"的学生的信息");
        return AjaxResult.success(resultMap);
    }
}
