package com.gem.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ADMIN")
public class Admin {
    private Long id;
    private String username;
    private String password;
}
