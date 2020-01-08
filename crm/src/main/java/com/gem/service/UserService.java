package com.gem.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.User;
import com.gem.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * 业务类
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

}
