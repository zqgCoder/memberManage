package com.gem.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gem.entity.Customer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class CustomerMapperTest {

    @Autowired
    CustomerMapper customerMapper;

    @Test
    public void selectList() {
        customerMapper.selectList(null)
                .forEach(System.out::println);
    }

    @Test
    public void selectOne() {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userid", 2);
        System.out.println(customerMapper.selectOne(queryWrapper));
        //System.out.println("---------------------");
        //System.out.println(customer);
    }

    @Test
    public void selectAll() {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", 2);
        List<Customer> customers = customerMapper.selectList(queryWrapper);
        System.out.println("-------------------");
        customers.forEach(System.out::println);
    }
}