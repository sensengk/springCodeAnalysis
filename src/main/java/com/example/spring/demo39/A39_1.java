package com.example.spring.demo39;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

@Configuration
public class A39_1 {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 1、演示获取Bean Definition源
        SpringApplication springApplication = new SpringApplication(A39_1.class);
        springApplication.setSources(Set.of("classpath:B01.xml"));
        // 2、演示推断应用类型

        Method deduceFromClasspath = WebApplicationType.class.getDeclaredMethod("deduceFromClasspath");
        deduceFromClasspath.setAccessible(true);
        WebApplicationType webApplicationType = (WebApplicationType) deduceFromClasspath.invoke(null);
        System.out.println("应用类型为 " + webApplicationType );

        // 3、演示 ApplicationContext 初始化器
        springApplication.addInitializers(new ApplicationContextInitializer<ConfigurableApplicationContext>(){

            @Override
            public void initialize(ConfigurableApplicationContext applicationContext) {
                // 尚未refresh的applicationContext
                if(applicationContext instanceof GenericApplicationContext){
                    // 通过初始化器，添加bean
                    ((GenericApplicationContext) applicationContext).registerBean("bean3",Bean3.class);
                }
            }
        });

        // 4、演示监听器与事件
        springApplication.addListeners(new ApplicationListener<ApplicationEvent>(){

           @Override
           public void onApplicationEvent(ApplicationEvent event) {

               System.out.println("监听到事件：" + event.getClass());
           }
        });

        // 5、演示主类推断
        Method deduceMainApplicationClass = SpringApplication.class.getDeclaredMethod("deduceMainApplicationClass");
        deduceMainApplicationClass.setAccessible(true);
        Class<?> mainApplicationClass = (Class<?>) deduceMainApplicationClass.invoke(springApplication);
        System.out.println("主类为：" + mainApplicationClass);


        // 上述是准备工作， 准备了初始话器，监听器和主类推断， 接下来就是正式的启动了


        ConfigurableApplicationContext context = springApplication.run(args); // 构建spring容器


        String[] names = context.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println("name:" + name + "来源：" + context.getBeanFactory().getBeanDefinition(name).getResourceDescription());
        }
//        context.close();


    }
    static class Bean1{

    }
    static class Bean2{}
    static class Bean3{}
    @Bean
    public Bean2 bean2(){
        return new Bean2();
    }

    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(){
        return new TomcatServletWebServerFactory();
    }
}
