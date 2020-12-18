//package com.tree.f2st.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.CacheControl;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//public class MvcConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/resources/**")
//                .addResourceLocations("classpath:/templates/","classpath:/static/")
//                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/webfist/mapJs").setViewName("mapJs");
//    }
//}
//
////Spring Boot HTML 파일 templates에서도 읽도록 설정하기
////controller에서 html 파일을 찾지 못해 config 파일 추가
////참고 : https://bottom-to-top.tistory.com/38?category=807633