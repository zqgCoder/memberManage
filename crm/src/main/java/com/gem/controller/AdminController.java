package com.gem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gem.entity.Admin;
import com.gem.entity.Customer;
import com.gem.entity.User;
import com.gem.mapper.CustomerMapper;
import com.gem.service.AdminService;
import com.gem.service.CustomerService;
import com.gem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/myAdmin")
public class AdminController {

    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerMapper customerMapper;

    //用户信息
    @GetMapping("/user_list")
    public String user_list(Model model, Integer pageNow, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthday, String gender) {
        System.out.println("进入到controller包下的AdminController类中的 user_list（）方法---------->");
        Page<User> page = null;
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (birthday != null) {
            wrapper.eq("birthday", birthday);
        }
        if(gender != null && gender != ""){
            wrapper.eq("gender", gender);
        }

        page = userService.page(new Page<>(pageNow != null ? pageNow : 1, 3), wrapper);
        model.addAttribute("page", page);
        model.addAttribute("birthday", birthday);
        model.addAttribute("gender",gender);
        return "admin_con/user_list";
    }

    //用户消费记录
    @GetMapping("/customer_list")
    public String customer_list(Model model, Integer pageNow, Integer userid, Integer courseid) {
        System.out.println("进入到controller包下的AdminController类中的 customer_list（）方法---------->");


        Page<Customer> page = null;
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        if(userid != null) {
            wrapper.eq("userid", userid);
        }
        if(courseid != null) {
            wrapper.eq("courseid", courseid);
        }
        page = customerService.page(new Page<>(pageNow != null ? pageNow : 1,3),wrapper);
        model.addAttribute("page" , page);
        model.addAttribute("userid", userid);
        model.addAttribute("courseid", courseid);

        return "admin_con/customer_list";
    }


    @GetMapping("/login")
    public String login() {
        System.out.println("进入到controller包下的AdminController类中的 login（）方法---------->");
        return "admin_con/login";
    }

    //用来验证管理员登录
    @PostMapping("/check")
    @ResponseBody
    public String check(HttpSession session, String username, String password) {
        System.out.println("进入到controller包下的AdminController类中的 check（）方法---------->");

        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("password", password);
        Admin admin = adminService.getOne(wrapper);
        if (admin != null) {
            //把登录成功的管理员添加到session
            session.setAttribute("admin", admin);
            //设置管理员的角色
            session.setAttribute("role","administrator");
            System.out.println("管理员登陆成功");
            return "1";
        } else {
            return "0";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Long id){
        System.out.println("进入到controller包下的AdminController类中的 logout（）方法---------->");
        //销毁用户
        String role = (String)session.getAttribute("role");
        if(role == "administrator") {
            System.out.println("用户退出成功,用户的ID为：" + id);
            session.invalidate();
        }
        //跳转页面
        return "admin_con/login";
    }

    //返回主页面
    @GetMapping("/admin_center" )
    public String admin_center(Model model) {
        return "admin_con/index";
    }

    //用来删除单个消费记录
    @GetMapping("/del_customer")
    public String del_customer(Integer id) {
        System.out.println("进入到controller包下的AdminController类中的 del_customer（）方法---------->");
        System.out.println(id);
        boolean bool = customerService.removeById(id);
        if (bool) {
            return "redirect:/myAdmin/customer_list";
        }
        return null;
    }

    @GetMapping("/del_user" )
    public String del_user(@RequestParam Long id){
        System.out.println("进入到controller包下的AdminController类中的 del_user（）方法---------->");
        boolean bool = userService.removeById(id);
        if(bool) {
            return "redirect:/myAdmin/user_list";
        }
        return null;
    }

    //添加用户
    @RequestMapping("/to_user_add")
    public String to_user_add() {
        return "admin_con/user_add";
    }

    @PostMapping("/user_add")
    @ResponseBody
    public String user_add(@RequestParam String username, @RequestParam String password,
                  @RequestParam String gender, @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthday) {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username",username);

        if(userService.getOne(query) != null){
            //存在该用户
            return "3";
        }
        if(username == null || password == null){
            return "0";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(gender);
        user.setBirthday(birthday);
        user.setDeleted(0);

        System.out.println("---------------" + user.toString() + "---------------");
        boolean bool = userService.save(user);
        if(bool){
            return "1";
        }
        return "0";
    }

    //添加消费记录
    @RequestMapping("/to_customer_add")
    public String to_customer_add(){
        return "admin_con/customer_add";
    }

    @PostMapping("/customer_add")
    @ResponseBody
    public String customer_add(@RequestParam String username, @RequestParam Long courseid,
           @RequestParam Double money,@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate time){
        //填写的信息不全
        if(username == null || courseid == null || money == null || time == null){
            return "0";
        }
        //差找该用户是否存在
        QueryWrapper<User> query = new QueryWrapper<>();
        query.eq("username",username);
        User user = userService.getOne(query);
        if(user == null){
            //不存在该用户
            return "3";
        }
        Customer customer = new Customer();
        customer.setCourseid(courseid);
        customer.setUserid(user.getId());
        customer.setMoney(money);
        customer.setTime(time);
        customer.setDeleted(0);
        System.out.println("---------------" + customer.toString() + "---------------");
        //添加成功
        boolean bool = customerService.save(customer);
        if(bool) return "1";
        //添加失败
        return "0";
    }

    @RequestMapping("/to_user_update")
    public String to_user_update(Model model, @RequestParam Long id){
        System.out.println("进入到controller包下的AdminController类中的 to_user_update（）方法---------->");
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "admin_con/user_update";
    }

    @PostMapping("/user_update")
    @ResponseBody
    public String user_update(@RequestParam Long id, @RequestParam String username, @RequestParam String gender,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthday){
        System.out.println("进入到controller包下的AdminController类中的 user_update（）方法---------->");
        if(id == null || username == null || gender == null || birthday == null){
            return "0";
        }
        User user = userService.getById(id);
        user.setUsername(username);
        user.setGender(gender);
        user.setBirthday(birthday);
        boolean bool = userService.updateById(user);
        if(bool){
            return "1";
        }
        return "0";
    }

    @RequestMapping("/to_customer_update")
    public String to_customer_update(Model model, @RequestParam Long id){
        Customer customer = customerService.getById(id);
        model.addAttribute("customer", customer);
        return "admin_con/customer_update";
    }

    @PostMapping("/customer_update")
    @ResponseBody
    public String customer_update(@RequestParam Long id, @RequestParam Long userid, @RequestParam Long courseid,
          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate time, @RequestParam Double money){
        if(id == null || userid == null || courseid == null || time == null || money == null){
            return "0";
        }

        User user = userService.getById(userid);
        if(user == null){
            //该用户不存在
            return "3";
        }
        Customer customer = customerService.getById(id);
        customer.setUserid(userid);
        customer.setCourseid(courseid);
        customer.setTime(time);
        customer.setMoney(money);
        boolean bool = customerService.updateById(customer);
        if(bool){
            return "1";
        }
        return "0";
    }

    //上传图片
    @RequestMapping("/upload")
    @ResponseBody
    public Map<String,Object> upload(@RequestParam Long id, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        Map map = new HashMap<String,Object>();
        if(file == null){
            map.put("msg","error");
            map.put("code",0);
        }
        String picName = file.getOriginalFilename();
        String filePath = "D:\\image\\" + picName;
        File dest = new File(filePath);
        file.transferTo(dest);
        User user = userService.getById(id);
        user.setHead(picName);
        boolean bool = userService.updateById(user);
        if(bool){
            map.put("msg","ok");
            map.put("code",200);
        } else{
            map.put("msg","error");
            map.put("code",0);
        }
        return map;
    }

    //下载图片
    @GetMapping("/downImage")
    public String downImage(@RequestParam String imageName, HttpServletRequest request, HttpServletResponse response) {
        String fileUrl = "D:\\image\\" + imageName;
        System.out.println("the imageName is : " + fileUrl);
        if(fileUrl != null) {
            File file = new File(fileUrl);
            if(file.exists()) {
                response.setContentType("application/force-download");
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" + imageName);//设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("image was download success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if(bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(fis != null){
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }
}
