package com.example.spring.demo45;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Bean1 {

    protected Bean2 bean2;
    protected boolean initialized;

    public Bean2 getBean2() {
        System.out.println("bean2 get");
        return bean2;
    }

    @Autowired
    public void setBean2(Bean2 bean2) {
        System.out.println("bean2 set");
        this.bean2 = bean2;
    }

    @PostConstruct
    public void init() {
        System.out.println(" init");
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }
}
