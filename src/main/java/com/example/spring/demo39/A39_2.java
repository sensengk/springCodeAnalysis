package com.example.spring.demo39;

import org.springframework.boot.DefaultBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class A39_2 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SpringApplication app = new SpringApplication();
        app.addListeners(event -> {
            System.out.println(event.getClass());
        });

        // 获取事件发布器实现的类名
        List<String> names = SpringFactoriesLoader.loadFactoryNames(SpringApplicationRunListener.class, A39_2.class.getClassLoader());

        for (String name : names) {
            System.out.println(name);
            Class<?> clazz = Class.forName(name);
            Constructor<?> constructor = clazz.getConstructor(SpringApplication.class, String[].class);
            SpringApplicationRunListener publisher = (SpringApplicationRunListener) constructor.newInstance(app, args);

            // 发送事件
            DefaultBootstrapContext bootstrapContext = new DefaultBootstrapContext();
            publisher.starting(bootstrapContext); // spring boot 开始启动
            publisher.environmentPrepared(bootstrapContext,new StandardEnvironment()); // 环境信息准备完成
            GenericApplicationContext context = new GenericApplicationContext();
            publisher.contextPrepared(context); // 上下文准备完成,调用初始化器之后，发送此事件
            publisher.contextLoaded(context); //  上下文加载完成 所有的bean definition已经加载完成，但是还没有初始化
            context.refresh();
            publisher.failed(context, new Exception("failed")); // 运行过程中出错

        }
    }
}
