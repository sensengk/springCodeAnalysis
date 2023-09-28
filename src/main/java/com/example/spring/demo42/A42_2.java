package com.example.spring.demo42;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

public class A42_2 {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        // 默认先加载外部的bean,后加载本项目的bean, 如何bean名字冲突，后面的会把前面的覆盖掉
//        context.getDefaultListableBeanFactory().setAllowBeanDefinitionOverriding(false);  // 不允许覆盖
        context.registerBean("config", Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();
        String[] names = context.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }
    }

    @Configuration // 本项目配置类
//    @Import(AutoConfigurationImportSelector.class) // 这个报错，springboot内部做了检查，不能直接这么使用
    @Import(MyImportSelector.class)
    static class Config{

    }
    // DeferredImportSelector 推迟导入
    static class MyImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            return new String[]{AutoConfiguration1.class.getName(),AutoConfiguration2.class.getName()};
        }
    }

    static class MyCondition1 implements Condition{

        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Map<String, Object> attributes = metadata.getAnnotationAttributes(ConditionalOnClass.class.getName());
            String className = attributes.get("className").toString();
            boolean exists = (boolean)attributes.get("exists");
            boolean present = ClassUtils.isPresent(className, null);
            return exists ? present : !present;
        }
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD,ElementType.TYPE})
    @Conditional(MyCondition1.class)
    @interface ConditionalOnClass{
        boolean exists(); // true 判断存在， false 判断不存在
        String className(); // 要判断的类名

    }


    @Configuration // 第三方配置类
    @ConditionalOnClass(className = "com.alibaba.druid.pool.DruidDataSource",exists = true)
    static class AutoConfiguration1{
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }
    }

    static class Bean1{}
    static class Bean2{}

    @Configuration // 第三分配置类
    @ConditionalOnClass(className = "com.alibaba.druid.pool.DruidDataSource",exists = false)
    static class AutoConfiguration2{
        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }
    }

}
