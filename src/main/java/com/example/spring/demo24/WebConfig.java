package com.example.spring.demo24;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@Configuration
public class WebConfig {

    @ControllerAdvice
    static class MyControllerAdvice{

        @InitBinder
        public void binder3(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyDateFormatter());
        }
    }

    @Controller
    public static class Controller1{
        @InitBinder
        public void binder1(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyDateFormatter());
        }
        public void foo(){}
    }

    @Controller
    public static class Controller2{
        @InitBinder
        public void binder21(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyDateFormatter());
        }

        @InitBinder
        public void binder22(WebDataBinder webDataBinder){
            webDataBinder.addCustomFormatter(new MyDateFormatter());
        }

        public void bar(){

        }
    }
}
