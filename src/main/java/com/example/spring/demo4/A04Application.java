package com.example.spring.demo4;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Arrays;

public class A04Application {
    public static void main(String[] args) {

        // 干净的appplicationContext 没有各种后处理器
        GenericApplicationContext context = new GenericApplicationContext();

//        用原始的方法注册三个bean

        context.registerBean("bean1",Bean1.class);
        context.registerBean("bean2",Bean2.class);
        context.registerBean("bean3",Bean3.class);
        context.registerBean("bean4",Bean4.class);

        // 可以帮助获取@value里面的值
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());

        context.registerBean(AutowiredAnnotationBeanPostProcessor.class); // 解析@Autowired 和@Value

        context.registerBean(CommonAnnotationBeanPostProcessor.class);// 解析@Resource, @PostConstruct @preDestory

        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory()); // 解析ConfigurationProperties



        // 初始化容器
        context.refresh();  // 执行beanFactory后处理器 添加bean后处理器 初始化所有单例

        System.out.println(context.getBean(Bean4.class));
        context.close(); // 销毁容器


    }
}
