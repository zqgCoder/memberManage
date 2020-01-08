package com.gem.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.Customer;
import com.gem.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends ServiceImpl<CustomerMapper, Customer> {

}
