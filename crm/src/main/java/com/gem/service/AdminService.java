package com.gem.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.Admin;
import com.gem.mapper.AdminMapper;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends ServiceImpl<AdminMapper, Admin> {
}
