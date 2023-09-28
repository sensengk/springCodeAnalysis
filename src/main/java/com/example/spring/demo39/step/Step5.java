package org.springframework.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.context.event.EventPublishingRunListener;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessorApplicationListener;
import org.springframework.boot.env.RandomValuePropertySourceEnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLog;
import org.springframework.boot.logging.DeferredLogs;
import org.springframework.core.io.support.SpringFactoriesLoader;

public class Step5 {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication();
//        spring实际实现方式
        // 添加事件监听器
        app.addListeners(new EnvironmentPostProcessorApplicationListener());
        SpringFactoriesLoader.loadFactoryNames(EnvironmentPostProcessor.class, Step5.class.getClassLoader()).forEach(System.out::println);

        EventPublishingRunListener publisher = new EventPublishingRunListener(app, args);
        // 发事件的时候，进行增强，相当于从factory中获取所有的EnvironmentPostProsessor，然后进行增强
        ApplicationEnvironment env = new ApplicationEnvironment();

        System.out.println(">>>>>>>>>>增强前");
        env.getPropertySources().forEach(System.out::println);
        publisher.environmentPrepared(new DefaultBootstrapContext(), env);
        System.out.println(">>>>>>>>>>增强后");
        env.getPropertySources().forEach(System.out::println);

    }
    private static void test1(){
        SpringApplication app =  new SpringApplication();
        ApplicationEnvironment env = new ApplicationEnvironment();
        System.out.println(">>>>>>>>>>增强前");
        env.getPropertySources().forEach(System.out::println);
        ConfigDataEnvironmentPostProcessor postProcessor1 = new ConfigDataEnvironmentPostProcessor(new DeferredLogs(), new DefaultBootstrapContext());
        postProcessor1.postProcessEnvironment(env, app);

        System.out.println(">>>>>>>>>>增强后");
        env.getPropertySources().forEach(System.out::println);


        RandomValuePropertySourceEnvironmentPostProcessor postProcessor2 = new RandomValuePropertySourceEnvironmentPostProcessor(new DeferredLog());
        postProcessor2.postProcessEnvironment(env, app);

        System.out.println(">>>>>>>>>>增强后");
        env.getPropertySources().forEach(System.out::println);

        System.out.println(env.getProperty("server.port"));
        System.out.println(env.getProperty("random.int"));
        System.out.println(env.getProperty("random.int"));
        System.out.println(env.getProperty("random.int"));
        System.out.println(env.getProperty("random.uuid"));
    }
}
