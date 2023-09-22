package com.example.spring.demo10.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class MyAspect {

    @Before("execution(* com.example.spring.demo9.Service.MyService.*())")
    public void before(){
        System.out.println("before");
    }

}
