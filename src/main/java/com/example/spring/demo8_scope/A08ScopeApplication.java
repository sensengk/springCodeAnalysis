package com.example.spring.demo8_scope;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.example.spring.demo8_scope")
public class A08ScopeApplication {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A08ScopeApplication.class);
        E e = context.getBean(E.class);
//        scope失效解释，因为初始化的时候生成了单例bean,每次获取到同一个引用，添加@lazy相当于做了一层代理
        // 代理解决
        System.out.println(e.getF1().getClass()); // 是一个增强的类
        System.out.println(e.getF1());
        System.out.println(e.getF1());

        //代理解决
        System.out.println(e.getF2());
        System.out.println(e.getF2());
        System.out.println(e.getF2());

        // 工厂方法
        System.out.println(e.getF3());
        System.out.println(e.getF3());
        System.out.println(e.getF3());

        // 根据context获取f4
        System.out.println(e.getF4());
        System.out.println(e.getF4());
        System.out.println(e.getF4());


    }
}

