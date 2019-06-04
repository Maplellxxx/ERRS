package com.waibao.project.config;

import com.waibao.project.component.AdminHandlerInterceptor;
import com.waibao.project.component.LoginHandlerInterceptor;
import com.waibao.project.component.SystemHandlerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//可以拓展SpringMVC的功能

//@EnableWebMvc全面接管MVC
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry){
//        registry.addViewController("/").setViewName("login");
//        registry.addViewController("/main.html").setViewName("index");
//    }

    @Bean//将组件注册在容器中
    public WebMvcConfigurerAdapter webMvcConfigurerAdapter(){
        WebMvcConfigurerAdapter adapter = new WebMvcConfigurerAdapter(){
            @Override
            public void addViewControllers(ViewControllerRegistry registry){
                registry.addViewController("/").setViewName("login");
                registry.addViewController("/main.html").setViewName("index");
                registry.addViewController("/system/").setViewName("system/index");
                registry.addViewController("/system").setViewName("system/index");
            }

            //注册拦截器
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
                        .excludePathPatterns("/","/login","/register","/admin/**","/system/**");
                registry.addInterceptor(new AdminHandlerInterceptor()).addPathPatterns("/admin/**");
                registry.addInterceptor(new SystemHandlerInterceptor()).addPathPatterns("/system/**")
                        .excludePathPatterns("/system/login","/system/register");
            }
        };
        return adapter;

    }

//    @Bean
//    public LocaleResolver localeResolver(){
//        return new MyLocaleResolver();
//    }
}

