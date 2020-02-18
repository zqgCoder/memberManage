package com.gem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gem.entity.User;
import com.gem.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpSession;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    @Autowired
    UserService userService;

    @Test
    public void login(String username, String password) {
        username = "admin1";
        password = "d78ff0ef526543e2174540afdfea0154";
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("password", password);
        User user = userService.getOne(wrapper);
        if (user != null) {
            System.out.println("登录成功");

        } else {
            System.out.println("登录失败");
        }
    }

    @Test
    void update_pwd() {
    }

    @Test
    void update_password() {
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println(ldt + " " + ldt.getYear());

    }

}