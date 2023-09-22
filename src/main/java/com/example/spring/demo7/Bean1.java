package com.example.spring.demo7;

import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

public class Bean1 implements InitializingBean {

    @PostConstruct
    public void init1(){
        System.out.println("初始化1");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化2");
    }

    public void init3(){
        System.out.println("初始化3");
    }
}
