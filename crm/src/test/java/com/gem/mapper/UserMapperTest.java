package com.gem.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.gem.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 测试类
 */
@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    //测试登录方法
    @Test
    public void selectOne() {
        /*QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", "admin2").eq("password", "654321");*/

        //JDK8.0 Lambda语法(自带防误写功能)
        User user = new LambdaQueryChainWrapper<>(userMapper)
                .eq(User::getUsername, "admin2")
                .eq(User::getPassword, "654321")
                .one();
        System.out.println(user);
    }

    //测试注册功能
    @Test
    public void inser() {
        //模拟注册的用户
        User user = new User();
        user.setUsername("admin3");
        user.setPassword("987654");
        int result = userMapper.insert(user);
        Long id = user.getId();
        System.out.println("添加成功,一共添加了:" + result + "行记录.");
        System.out.println(id);

    }

    @Test
    public void selectAll() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", "adm");
        List<User> users = userMapper.selectList(queryWrapper);
        System.out.println("-------------------");
        users.forEach(System.out::println);
    }
}