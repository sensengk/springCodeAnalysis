package com.example.spring.demo5;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

public class A05Application {
    public static void main(String[] args) throws IOException {
        // 干净容器
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("config",Config.class);
//        bean工厂处理器
//        context.registerBean(ConfigurationClassPostProcessor.class); // 解析ComponentScan @Bean @Import
//        context.registerBean(MapperScannerConfigurer.class,beanDefinition -> {
////            添加扫描范围， mybatis底层是用这个处理器进行解析mapper
//            beanDefinition.getPropertyValues().add("basePackage","com.example.spring.demo5.mapper");
//        });
//        等于 自定义的几个处理器

//        context.registerBean(ComponentScanPostProcessor.class);

        context.registerBean(AtBeanPostProcessor.class);
        context.registerBean(MapperPostProcessor.class);



        context.refresh();

        // 判断有多少beanDefinition
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(">>>>>>>>>" + beanDefinitionName);
        }
        context.close();

    }
}
