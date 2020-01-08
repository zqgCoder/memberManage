package com.gem.interceptor;

import com.gem.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录验证拦截器
 */
public class MyCustomerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        System.out.println(">>>MyCustomerInterceptor>>>>>>>在请求处理之前进行调用（Controller方法调用之前）");
        //判断是否登录
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return true;
        }
        response.sendRedirect("/crm/user/index?info=1");
        return false;
    }
}
