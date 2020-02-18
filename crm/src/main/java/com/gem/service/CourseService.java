package com.gem.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gem.entity.Course;
import com.gem.mapper.CourseMapper;
import org.springframework.stereotype.Service;

@Service
public class CourseService extends ServiceImpl<CourseMapper, Course> {
}
