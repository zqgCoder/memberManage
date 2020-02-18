package com.gem.controller;

import java.io.*;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gem.entity.User;
import com.gem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * 本页面主要用来作为用户的登录注册的控制器
 * */
@Controller
@RequestMapping("/user")

public class UserController {

    @Autowired
    UserService userService;

    //用来实现在拦截未登录用户后，返回登录页面
    @GetMapping("/index")
    public String toHome() {
        System.out.println("进入到controller包下的UserController类中的toHome（）方法---------->");
        return "user_con/login";
    }

    //查看用户名是否存在，可以用来作为判断注册时用户名唯一的方法
    public String check(String username) {
        System.out.println("进入到controller包下的UserController类中的checkUsername（）方法---------->");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userService.getOne(queryWrapper);
        if (user != null) {
            System.out.println("用户存在,注册失败");
            return "1";
        }
        return "0";
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(User user) {
        if (check(user.getUsername()) == "1") {
            System.out.println("用户登陆成功");
            return "3";
        }
        //开始注册
        boolean bool = userService.save(user);
        System.out.println("注册成功，用户信息---------->" + user.toString());
        if (bool) {
            return "1"; //注册成功
        } else {
            return "0";
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(HttpSession session, String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).eq("password", password);
        User user = userService.getOne(wrapper);
        if (user != null) {
            //把登录成功的用户添加到session
            session.setAttribute("user", user);
            System.out.println("找到了用户");
            return "1";
        } else {
            return "0";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session, Integer id) {
        System.out.println("进入到controller包下的UserController类中的 logout（）方法---------->");
        //销毁用户
        String role = (String) session.getAttribute("role");
        if (role == "customer") {
            System.out.println("用户退出成功,用户的ID为：" + id);
            session.invalidate();
        }
        //跳转页面
        return "user_con/login";
    }

    public static String getMd5(String str) throws Exception {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //用户信息修改
    @PostMapping("/update")
    @ResponseBody
    public String update(HttpSession session, String username, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthday, String gender) {
        User user = (User) session.getAttribute("user");
        //判断是不是同一个用户
        if (user.getUsername().equals(username)) {
            System.out.println("是同一个用户");
            user.setBirthday(birthday);
            user.setGender(gender);
            boolean bool = userService.updateById(user);
            if (bool) {
                return "1";
            }
            return "0";
        }
        System.out.println("不是同一个用户");

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        User user2 = userService.getOne(queryWrapper);
        if (user2 != null) {
            System.out.println("用户名一样了，失败");
            return "3";     //用户名相同
        } else {
            //不是同一个用户,用户名不同
            user.setUsername(username);
            user.setBirthday(birthday);
            user.setGender(gender);
            boolean bool = userService.updateById(user);
            if (bool) {
                return "1";
            }
            return "0";
        }
    }

    //跳转到用户修改信息页面
    @GetMapping("/to_user_update")
    public String to_user_update() {
        return "user_con/user_update";
    }

    @GetMapping("/update_pwd")
    public String update_pwd() {
        return "user_con/user_pwd";
    }

    //修改用户密码
    @PostMapping("/update_password")
    @ResponseBody
    public String update_password(HttpSession session, @RequestParam String pwd1, @RequestParam String pwd2) {
        System.out.println("进入到controller包下的UserController类中的 update_password（）方法---------->");
        User user = (User) session.getAttribute("user");
        System.out.println(user.getPassword() + "------------" + pwd1 + "--------------" + pwd2);
        if (pwd2 == "" || pwd2 == null) return "0";
        user.setPassword(pwd2);
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("password", pwd2);
        boolean bool = userService.updateById(user);
        if (bool)
            return "1";
        return "0";
    }

    //上传图片
    @RequestMapping("/upload")
    @ResponseBody
    public Map<String, Object> upload(HttpSession session, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {

        String picName = file.getOriginalFilename();
        String filePath = "D:\\image\\" + picName;
        File dest = new File(filePath);
        file.transferTo(dest);
        User user = (User) session.getAttribute("user");
        user.setHead(picName);
        boolean bool = userService.updateById(user);
        Map map = new HashMap<String, Object>();
        if (bool) {
            map.put("msg", "ok");
            map.put("code", 200);
        } else {
            map.put("msg", "error");
            map.put("code", 0);
        }
        return map;
    }

    //下载图片
    @GetMapping("/downImage")
    public String downImage(@RequestParam String imageName, HttpServletRequest request, HttpServletResponse response) {
        String fileUrl = "D:\\image\\" + imageName;
        System.out.println("the imageName is : " + fileUrl);
        if (fileUrl != null) {
            File file = new File(fileUrl);
            if (file.exists()) {
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
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
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