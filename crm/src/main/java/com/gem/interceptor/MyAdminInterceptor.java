package com.gem.interceptor;

import com.gem.entity.Admin;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MyAdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        //System.out.println(">>>MyAdminInterceptor>>>>>>>在请求处理之前进行调用（Controller方法调用之前）");
        //判断是否为管理员登录
        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute("admin");
        String role = (String) session.getAttribute("role");

        if (admin != null && role == "administrator") {
            //System.out.println(admin.toString() + "------------" + role);
            return true;
        }
        //失败后返回到管理员登录页面
        response.sendRedirect("/crm/myAdmin/login");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println(">>>MyAdminInterceptor>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        System.out.println(">>>MyAdminInterceptor>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
    }
}
