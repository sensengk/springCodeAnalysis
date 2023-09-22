package com.example.spring.demo8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Singleton,prototype,request,session,application
//如果 jdk > 8, 运行时请添加 --add-opens java.base/java.lang=ALL-UNNAMED
@SpringBootApplication
public class A08Application {
    public static void main(String[] args) {
         SpringApplication.run(A08Application.class, args);



    }
}

