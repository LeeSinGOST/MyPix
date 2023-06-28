package com.pixiv.controller.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginIntercepter interceptor1;
    @Autowired
    private changeIntercepter interceptor2;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加拦截器，配置拦截地址
//        registry.addInterceptor(interceptor3).addPathPatterns("/**")
//                .excludePathPatterns("/static/**","/css/**","/image/**","/js/**","/image/**");
//        registry.addInterceptor(interceptor2).addPathPatterns();
        registry.addInterceptor(interceptor1).addPathPatterns("/bookmark","/follow**","/recommend**","/update**","/id/**","/bookmark/**","/pic/**","/rank/**");

    }
}
