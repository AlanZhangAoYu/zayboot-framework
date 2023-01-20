package controller;

import cn.zay.zayboot.annotation.ioc.Autowired;
import cn.zay.zayboot.annotation.ioc.Component;
import cn.zay.zayboot.annotation.springmvc.*;
import com.alibaba.fastjson2.JSON;
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
    @PostMapping("/setStudentInfo")
    public AjaxResult setStudentInfo(@RequestParam("name") String name){
        HashMap<String,String> resultMap = new HashMap<>(1);
        resultMap.put("message","设置学生属性成功! 姓名为: "+name);
        return AjaxResult.success(resultMap);
    }
    @GetMapping("/getStudentInfoById/{id}")
    public AjaxResult getStudentInfoById(@PathVariable("id") String id){
        HashMap<String,String> resultMap = new HashMap<>(1);
        resultMap.put("message","获取id为"+id+"的学生的信息");
        return AjaxResult.success(resultMap);
    }
}
