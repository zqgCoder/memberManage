package com.gem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.entity.Course;
import com.gem.entity.User;
import com.gem.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseService courseService;

    @GetMapping("/course_list")
    public String course_list(Model model, Integer pageNow, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime, String statement) {
        System.out.println("进入到controller包下的CourseController类中的 course_list（）方法---------->");
        Page<Course> page = null;
        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        if (startTime != null) {
            wrapper.eq("start_time", startTime);
        }
        if (statement != null && statement != "") {
            wrapper.eq("statement", statement);
        }

        page = courseService.page(new Page<>(pageNow != null ? pageNow : 1, 10), wrapper);
        model.addAttribute("page", page);
        model.addAttribute("startTime", startTime);
        model.addAttribute("statement", statement);
        return "admin_con/course_list";
    }

    //用来删除单个消费记录
    @GetMapping("/del_course")
    public String del_course(Integer id) {
        System.out.println("进入到controller包下的CourseController类中的 del_course（）方法---------->");
        System.out.println(id);
        boolean bool = courseService.removeById(id);
        if (bool) {
            return "redirect:/course/course_list";
        }
        return null;
    }

    //添加课程
    @RequestMapping("/to_course_add")
    public String to_course_add() {
        return "admin_con/course_add";
    }

    @PostMapping("/course_add")
    @ResponseBody
    public String course_add(@RequestParam String courseName, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
                           @RequestParam String courseInfo, @RequestParam Double money) {
        if(!check_name(courseName)) {
            //存在该课程
            return "3";
        }
        if (startTime == null || money == null) {
            return "0";
        }
        Course course = new Course();
        course.setCourseName(courseName);
        course.setNowNum(0);
        course.setStartTime(startTime);
        course.setStatement("未结束");
        course.setCourseInfo(courseInfo);
        course.setMoney(money);
        course.setDeleted(0);

        boolean bool = courseService.save(course);
        if (bool) {
            return "1";
        }
        return "0";
    }
    @RequestMapping("/to_course_update")
    public String to_course_update(Model model, @RequestParam Long id) {
        System.out.println("进入到controller包下的AdminController类中的 to_course_update（）方法---------->");
        Course course = courseService.getById(id);
        model.addAttribute("course", course);
        return "admin_con/course_update";
    }

    @PostMapping("/course_update")
    @ResponseBody
    public String user_update(@RequestParam Long id, @RequestParam String courseName,
             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime, @RequestParam String courseInfo, @RequestParam Double money) {
        System.out.println("进入到controller包下的CourseController类中的 user_update（）方法---------->");
        if (id == null || courseName == null || startTime == null || courseInfo == null || money == null) {
            return "0";
        }
        Course course = courseService.getById(id);
        course.setCourseName(courseName);
        course.setStartTime(startTime);
        course.setCourseInfo(courseInfo);
        course.setMoney(money);
        boolean bool = courseService.updateById(course);
        if (bool) {
            return "1";
        }
        return "0";
    }

    @GetMapping("/change_state")
    @ResponseBody
    public void change_state(@RequestParam Integer id, @RequestParam String statement){
        if(id == null || statement == null);
        Course course = courseService.getById(id);
        if(course!= null){
            course.setStatement(statement);
            courseService.updateById(course);
        }
    }

    public Boolean check_name(String courseName){
        if(courseName == null)  return false;
        QueryWrapper<Course> query = new QueryWrapper<>();
        query.eq("course_name", courseName);
        if(courseService.getOne(query) != null) {
            //存在该课程
            return false;
        }
        return true;
    }
}
