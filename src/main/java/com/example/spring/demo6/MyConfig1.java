package com.example.spring.demo6;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyConfig1 {

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext){
        System.out.println("注入applicationContext");
    }

    @PostConstruct
    public void init(){
        System.out.println("初始化");
    }

    @Bean
    public BeanFactoryPostProcessor processor1(){
        return beanFactory -> {
            System.out.println("执行processor1");
        };
    }
}
