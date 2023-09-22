package com.example.spring.demo7;

import org.springframework.beans.factory.DisposableBean;

import javax.annotation.PreDestroy;

public class Bean2 implements DisposableBean {
    @PreDestroy
    public void destroy1(){
        System.out.println("销毁1");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("销毁2");
    }
    public void destroy3(){
        System.out.println("销毁3");
    }
}
