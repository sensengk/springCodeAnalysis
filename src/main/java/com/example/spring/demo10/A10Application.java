package com.example.spring.demo10;

import com.example.spring.demo10.Service.MyService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class A10Application {


    public static void main(String[] args) {



        ConfigurableApplicationContext context = SpringApplication.run(A10Application.class, args);
        MyService service = context.getBean(MyService.class);
        System.out.println("service,class:" + service.getClass());
        service.foo();

        context.close();


//        用aspectj插件才能进行增强 目前拉插件有问题，拉不下来 只能 通过beanFactory获取的才能代理，直接new处理的并没有代理
        new MyService().foo();


    }
}
