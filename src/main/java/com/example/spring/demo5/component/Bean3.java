package com.example.spring.demo5.component;

import org.springframework.stereotype.Component;

@Component
public class Bean3 {
    public Bean3(){
        System.out.println("我被spring管理了");
    }
}
