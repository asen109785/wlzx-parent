package net.wanho.controller;

import net.wanho.po.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class FreemarkerController {

    @GetMapping("/student")
    public String student(Map map){
        //map.put("name","张三");

        Student stu = new Student();
        stu.setName("万和");
        stu.setAge(26);

        Student stu1 = new Student();
        stu1.setName("张三");
        stu1.setAge(18);


        Student stu2 = new Student();
        stu2.setName("李四");
        stu2.setAge(18);

        stu.setFriends(Arrays.asList(stu1,stu2));

        map.put("stu",stu);

        HashMap stuMap = new HashMap();
        stuMap.put(stu1.getName(),stu1);
        stuMap.put(stu2.getName(),stu2);
        map.put("stuMap",stuMap);
        //跳转到视图，如果在pom中加上了模板引擎，则跳转到模板视图
        return "student";
    }


    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/banner")
    public String banner(Map map){
        Map object = restTemplate.getForObject("http://localhost:31001/cms/config/getmodel/1", Map.class);
//        直接传给前端一个  map键值对，与 put（“”，xxx）一样
        map.putAll(object);
        return "banner";
    }

}
