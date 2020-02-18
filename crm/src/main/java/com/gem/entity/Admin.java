package com.gem.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ADMIN")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String password;
    private String qq;
    private String email;
    @TableLogic
    private Integer deleted;    //逻辑删除字段
}
