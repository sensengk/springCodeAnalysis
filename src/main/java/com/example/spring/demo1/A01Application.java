package com.example.spring.demo1;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

@SpringBootApplication
public class A01Application {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        ConfigurableApplicationContext context = SpringApplication.run(A01Application.class, args);

        // 通过debug的方式可以查看context里面的内容
        System.out.println(context);

        // 通过反射的方式获取到存储所有单例的字段
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjects.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        // 通过反射获取到对应的单例字段
        Map<String,Object> map = (Map<String,Object>)singletonObjects.get(beanFactory);
        map.entrySet().stream().filter(e->e.getKey().startsWith("component")).forEach(e ->{
            System.out.println(e.getKey() + "====" + e.getValue());
        });


        // 国际化的功能
        System.out.println(context.getMessage("hello", null, Locale.CHINA));
        System.out.println(context.getMessage("hello", null, Locale.ENGLISH));

        // 处理资源的能力
        // classpath: 类路径通配符 classpath*:可以去jar包里main寻找资源
        Resource[] resources = context.getResources("classpath:application.properties");
        Resource[] resources1 = context.getResources("classpath*:/META-INF/spring.factories");

        Arrays.stream(resources).forEach(System.out::println);
        Arrays.stream(resources1).forEach(System.out::println);

        // 获取环境资源和配置文件数据的能力
         System.out.println(context.getEnvironment().getProperty("server.port"));

         // 发布事件
        context.publishEvent(new UserRegisteredEvent(context));
        context.getBean(Component1.class).register();
    }
}
