package com.gem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@TableName("COURSE")
public class Course {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String courseName;
    private Integer nowNum;
    private LocalDate startTime;
    private String statement;
    private String courseInfo;
    private Double money;
    @TableLogic
    private Integer deleted;

}
