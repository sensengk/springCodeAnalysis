package com.example.spring.demo3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class A03Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A03Application.class, args);

        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);

        // 可以展示容器的销毁操作
        context.close();
    }
}
