package com.example.spring.demo45;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

//@SpringBootApplication
public class A45 {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A45.class, args);
        Bean1 proxy = context.getBean(Bean1.class);

        context.close();

    }
    public static void showProxyAndTarget(Bean1 bean1) {

    }
}
