package com.example.spring.demo10.Service;


import org.springframework.stereotype.Service;

@Service
public class MyService {
    public void foo(){
        System.out.println("foo");
        bar();
    }
    private void bar(){
        System.out.println("bar");
    }
}
