package net.wanho.po;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Student {
    private String name;//姓名
    private int age;//年龄
    private Date birthday;//生日
    private Float money;//钱
    private List<Student> friends;//朋友列表
    private Student bestFriend;//最好的朋友
}
