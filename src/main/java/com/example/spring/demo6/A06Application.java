package com.example.spring.demo6;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

public class A06Application {
    public static void main(String[] args) {
//        Aware接口用于注入一些与容器相关的信息

        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("myBean",MyBean.class);
//        context.registerBean("myConfig1",MyConfig1.class);

//        context.registerBean("myConfig2",MyConfig2.class);

        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
        context.registerBean(ConfigurationClassPostProcessor.class);

        context.refresh(); // 1、beanFactory 后处理器 2、添加Bean后处理器 3、初始化单例
        context.close();
    }
}
