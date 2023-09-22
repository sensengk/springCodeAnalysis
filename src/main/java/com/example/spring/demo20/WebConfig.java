package com.example.spring.demo20;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties({WebMvcProperties.class, ServerProperties.class})
public class WebConfig {
    // 内嵌web容器工厂
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(ServerProperties serverProperties){
        return new TomcatServletWebServerFactory(serverProperties.getPort());
    }

    // 创建 Dispatcher Servlet
    @Bean
    public DispatcherServlet dispatcherServlet(){
        return new DispatcherServlet();
    }
    // 注册 DispatcherServlet Spring mvc的入口
    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean( DispatcherServlet dispatcherServlet,WebMvcProperties webMvcProperties){
        DispatcherServletRegistrationBean registrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        // 不懒加载DispatcherServlet, 立即进行加载， 数字小的，初始化优先级高。多个DispatcherServlet加载顺序
        registrationBean.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());
        return registrationBean;
    }

    // 如果用DispatcherServlet 初始化时默认添加的组件，并不会作为Bean,给测试带来困扰
    // 加入RequestMappingHandlerMapping 就不会使用默认的了
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping(){
        return new RequestMappingHandlerMapping();
    }

    //加入RequestMappingHandlerAdapter, 替换掉DispatcherServlet默认的4个HandlerAdapter
    @Bean
    public MyRequestMappingHandlerAdapter requestMappingHandlerAdapter(){
        TokenArgumentResolver tokenArgumentResolver = new TokenArgumentResolver();
        YmlReturnValueHandler ymlReturnValueHandler = new YmlReturnValueHandler();
        MyRequestMappingHandlerAdapter handlerAdapter = new MyRequestMappingHandlerAdapter();
        handlerAdapter.setCustomArgumentResolvers(List.of(tokenArgumentResolver));
        handlerAdapter.setReturnValueHandlers(List.of(ymlReturnValueHandler));
        return handlerAdapter;
    }


}
