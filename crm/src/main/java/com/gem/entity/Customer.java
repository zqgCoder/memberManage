package com.gem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;

/**
 * 用来作为用户的消费日志类
 */
@Data
@TableName("CUSTOMER")
public class Customer {
    @TableId(type = IdType.AUTO)
    private Long id;    //消费记录的ID
    private String username;    //用户名
    private String courseName;  //课程ID course_id
    private LocalDate time; //购买产品的时间
    @TableLogic
    private Integer deleted;    //逻辑删除字段
}
