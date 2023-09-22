package com.example.spring.demo5.component;

import org.springframework.stereotype.Component;

@Component
public class Bean2 {
    public Bean2(){
        System.out.println("我被spring管理了");
    }
}
