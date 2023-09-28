package com.example.spring.demo40;

import com.example.spring.demo20.MyRequestMappingHandlerAdapter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11Nio2Protocol;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestTomcat {
    public static void main(String[] args) throws IOException, LifecycleException {
        // 创建Tomcat对象
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("tomcat");

        // 创建项目文件夹，即docBase文件夹
        File docBase = Files.createTempDirectory("boot.").toFile();
        docBase.deleteOnExit();

        // 创建tomcat项目，在tomcat中称为上下文
        Context context = tomcat.addContext("", docBase.getAbsolutePath());

        WebApplicationContext springContext = getApplicationContext();

        // 编程添加servlet
        context.addServletContainerInitializer(new ServletContainerInitializer() {
            @Override
            public void onStartup(Set<Class<?>> set, ServletContext ctx) throws ServletException {
                ctx.addServlet("aaa", new HelloServlet()).addMapping("/hello");
//                DispatcherServlet bean = springContext.getBean(DispatcherServlet.class);
//                ctx.addServlet("bbb", bean).addMapping("/");
                for (ServletRegistrationBean registrationBean : springContext.getBeansOfType(ServletRegistrationBean.class).values()) {
                    registrationBean.onStartup(ctx);
                }


            }
        }, Collections.emptySet());
        // 启动tomcat
        tomcat.start();

        // 创建链接器
        Connector connector = new Connector(new Http11Nio2Protocol());
        connector.setPort(8080);
        tomcat.setConnector(connector);


    }
    public static WebApplicationContext getApplicationContext(){
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(Config.class);
        context.refresh();
        return context;
    }

    @Configuration
    static class Config{
        @Bean
        public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet){
            DispatcherServletRegistrationBean registrationBean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
            // 不懒加载DispatcherServlet, 立即进行加载， 数字小的，初始化优先级高。多个DispatcherServlet加载顺序
            return registrationBean;
        }

        // 创建 Dispatcher Servlet
        @Bean
        public DispatcherServlet dispatcherServlet(WebApplicationContext  context){
            return new DispatcherServlet(context);
        }

        //加入RequestMappingHandlerAdapter, 替换掉DispatcherServlet默认的4个HandlerAdapter
        @Bean
        public MyRequestMappingHandlerAdapter requestMappingHandlerAdapter(){
            MyRequestMappingHandlerAdapter handlerAdapter = new MyRequestMappingHandlerAdapter();
            handlerAdapter.setMessageConverters(List.of(new MappingJackson2HttpMessageConverter()));
            return handlerAdapter;
        }
        @RestController
        static class TestController {
            @GetMapping("/test")
            public String test(){
                return "test";
            }
        }
    }
}
