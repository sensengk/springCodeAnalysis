package com.example.spring.demo8;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Scope("session")
@Component
public class BeanForSession {
    @PreDestroy
    public void destroy(){
        System.out.println("destroy");
    }


}
