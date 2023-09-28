package com.example.spring.demo45;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyAspect {

    @Before("execution(* com.example.spring.demo45.A45.*(..))")
    public void before(){
        System.out.println("before");
    }
}
