package com.example.spring.demo1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Component1 {


    @Autowired
    ApplicationEventPublisher context;

    public void register(){
        context.publishEvent(new UserRegisteredEvent(this));
    }
}
