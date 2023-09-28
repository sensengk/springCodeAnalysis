package com.example.spring.demo41;

import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

public class A41 {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        // 默认先加载外部的bean,后加载本项目的bean, 如何bean名字冲突，后面的会把前面的覆盖掉
//        context.getDefaultListableBeanFactory().setAllowBeanDefinitionOverriding(false);  // 不允许覆盖
        context.registerBean("config",Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();
        String[] names = context.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }

    @Configuration // 本项目配置类
    @Import(MyImportSelector.class)
    static class Config{

    }
    // DeferredImportSelector 推迟导入
    static class MyImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            List<String> names = SpringFactoriesLoader.loadFactoryNames(MyImportSelector.class, null);
            return names.toArray(new String[0]);
        }
    }


    @Configuration // 第三方配置类
    static class AutoConfiguration1{
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }
    }

    static class Bean1{}
    static class Bean2{}

    @Configuration // 第三分配置类
    static class AutoConfiguration2{
        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }
    }

}
