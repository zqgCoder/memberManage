package com.gem.config;

import com.gem.interceptor.MyAdminInterceptor;
import com.gem.interceptor.MyCustomerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    //实现拦截器 要拦截的路径以及不拦截的路径
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用于排除拦截

        //本拦截器用来实现对管理员的拦截
        registry.addInterceptor(new MyAdminInterceptor())
                .addPathPatterns("/myAdmin/**")
                .excludePathPatterns("/","/login.html","/cus/**","/customer/**","/myAdmin/login","/myAdmin/check");

        //用户拦截器
        registry.addInterceptor(new MyCustomerInterceptor())
                .addPathPatterns("/customer/**")
                .excludePathPatterns("/","/login.html","/cus/**","/myAdmin/**");
    }
}