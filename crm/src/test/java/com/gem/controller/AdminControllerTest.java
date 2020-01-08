package com.gem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.entity.Customer;
import com.gem.mapper.CustomerMapper;
import com.gem.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {

    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerMapper customerMapper;


}