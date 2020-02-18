package com.gem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.entity.Customer;
import com.gem.entity.User;
import com.gem.mapper.CustomerMapper;
import com.gem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/*
 * 本页面主要用来作为用户的一些个人信息和消费记录的增删改查功能。
 * 与UserController结合使用，可以完成用户的全部与数据库有关的行为。
 * */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerMapper customerMapper;

    //返回到用户的个人中心
    @GetMapping("/user_center")
    public String user_center(Model model) {
        return "user_con/index";
    }

    @GetMapping("/user_consume")
    public String user_consume(Model model, HttpSession session, Integer pageNow, Long courseid) {
        System.out.println("进入到controller包下的CustomerController类中的 user_consume（）方法---------->");

        User user = (User) session.getAttribute("user");

        Page<Customer> page = null;
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();

        wrapper.eq("username", user.getUsername());

        if (courseid != null) {
            wrapper.eq("courseid", courseid);
        }
        page = customerService.page(new Page<>(pageNow != null ? pageNow : 1, 3), wrapper);
        model.addAttribute("page", page);
        model.addAttribute("courseid", courseid);
        return "user_con/user_list";
    }

    @GetMapping("/list")
    public String list(Model model, Integer pageNow, String condition) {
        Page<Customer> page = null;
        if (condition != null) {
            QueryWrapper<Customer> wrapper = new QueryWrapper<>();
            wrapper.like("name", condition);
            page = customerService.page(new Page<>(pageNow != null ? pageNow : 1, 3), wrapper);
        } else {
            page = customerService.page(new Page<>(pageNow != null ? pageNow : 1, 3));
        }
        model.addAttribute("page", page);
        model.addAttribute("sc", condition);

        return "list";
    }

    /*
    @PostMapping("/edit")
    public String edit(Long id, String name, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthday) {
        Customer customer = customerService.getById(id);
        customer.setName(name);
        customer.setBirthday(birthday);
        boolean bool = customerService.updateById(customer);
        if (bool) {
            return "redirect:/customer/list";
        }
        return null;
    }

*/
    @PostMapping("/search")
    public String search(Model model, String sname) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.like("name", sname);
        Page<Customer> page = customerService.page(new Page<>(1, 3), wrapper);
        model.addAttribute("page", page);
        model.addAttribute("sc", sname);
        return "list";
    }

}
