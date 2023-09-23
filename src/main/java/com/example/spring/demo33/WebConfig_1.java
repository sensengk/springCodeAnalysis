package com.example.spring.demo33;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.*;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class WebConfig_1 {
    @Bean
    public ServletWebServerFactory servletWebServerFactory(){
        return new TomcatServletWebServerFactory();
    }
    @Bean
    public DispatcherServlet dispatcherServlet(){
        return new DispatcherServlet();
    }
    @Bean
    public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet){
        return new DispatcherServletRegistrationBean(dispatcherServlet ,"/");
    }




    @Component
    static class MyHandlerMapping implements HandlerMapping {

        @Override
        public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
            String key = request.getRequestURI();
            Controller controller = collect.get(key);
            if(controller == null){
                return  null;
            }
            return new HandlerExecutionChain(controller);
        }
        @Autowired
        private ApplicationContext context;

        private Map<String, Controller> collect;

        @PostConstruct
        public void init(){
            collect = context.getBeansOfType(Controller.class).entrySet().stream()
                    .filter(entry -> entry.getKey().toString().startsWith("/"))
                    .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
            System.out.println(collect);
        }
    }

    @Component
    static class MyHandlerAdapter  implements HandlerAdapter {

        @Override
        public boolean supports(Object handler) {
            return handler instanceof Controller;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
           if(handler instanceof Controller) {
               ((Controller) handler).handleRequest(request, response);
           }
            return null;
        }

        @Override
        public long getLastModified(HttpServletRequest request, Object handler) {
            return 0;
        }
    }

    @Component("/c1")
    public static class  Controller1 implements Controller {
        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            System.out.println("c1");
            response.getWriter().write("this is c1");
            return null;
        }
    }

    @Component("c2")
    public static class  Controller2 implements Controller {
        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().write("this is c2");
            return null;

        }
    }

    @Bean("/c3")
    public Controller controller3(){
        return new Controller(){
            @Override
            public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.getWriter().write("this is c3");
                return null;
            }
        };
    }
}
