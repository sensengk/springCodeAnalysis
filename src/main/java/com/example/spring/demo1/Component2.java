package com.example.spring.demo1;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Component2 {
    @EventListener
    public void acceptEvent(UserRegisteredEvent userRegisteredEvent){
        System.out.println(userRegisteredEvent);
    }
}
