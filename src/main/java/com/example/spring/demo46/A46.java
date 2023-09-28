package com.example.spring.demo46;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Configuration
public class A46 {
    public static void main(String[] args) throws NoSuchMethodException, NoSuchFieldException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A46.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        ContextAnnotationAutowireCandidateResolver
                resolver = new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(beanFactory);

        test1(context, resolver, Bean1.class.getDeclaredField("name"));
        test2(context, resolver, Bean1.class.getDeclaredField("age"));
        test3(context, resolver, Bean2.class.getDeclaredField("bean3"));
        test3(context, resolver, Bean4.class.getDeclaredField("value"));


    }
    private static void test3(AnnotationConfigApplicationContext context, ContextAnnotationAutowireCandidateResolver resolver, Field field) {
        DependencyDescriptor dd1 = new DependencyDescriptor(field, false);
        // 获取value的内容
        String s = resolver.getSuggestedValue(dd1).toString();
        System.out.println(s);
        // 解析${}
        ConfigurableEnvironment environment = context.getEnvironment();
        String s1 = environment.resolvePlaceholders(s);
        System.out.println(s1);
        System.out.println(s1.getClass());

        // 解析#{}
        Object bean3 = context.getBeanFactory().getBeanExpressionResolver().evaluate(s1, new BeanExpressionContext(context.getBeanFactory(), null));

        Object result = context.getBeanFactory().getTypeConverter()
                .convertIfNecessary(bean3, dd1.getDependencyType());
        System.out.println(result);
    }

    private static void test2(AnnotationConfigApplicationContext context, ContextAnnotationAutowireCandidateResolver resolver, Field field) {
        DependencyDescriptor dd1 = new DependencyDescriptor(field, false);
        // 获取value的内容
        String s = resolver.getSuggestedValue(dd1).toString();
        System.out.println(s);
        ConfigurableEnvironment environment = context.getEnvironment();
        String s1 = environment.resolvePlaceholders(s);
        System.out.println(s1);
        System.out.println(s1.getClass());
        Object o = context.getBeanFactory().getTypeConverter()
                .convertIfNecessary(s1, dd1.getDependencyType());
        System.out.println(o.getClass());
    }

    private static void test1(AnnotationConfigApplicationContext context, ContextAnnotationAutowireCandidateResolver resolver, Field field) {
        DependencyDescriptor dd1 = new DependencyDescriptor(field, false);
        // 获取value的内容
        String s = resolver.getSuggestedValue(dd1).toString();
        System.out.println(s);
        ConfigurableEnvironment environment = context.getEnvironment();
        String s1 = environment.resolvePlaceholders(s);
        System.out.println(s1);
    }

    public class Bean1{
        @Value("${server.port}")
        private String name;

        @Value("18")
        private int age;
    }
    public class Bean2{
        @Value("#{@bean3}")
        private Bean3 bean3;
    }
    @Component("bean3")
    public  class Bean3{

    }
    static class  Bean4{
        @Value("#{'hello' + '${JAVA_HOME}'}")
        private String value;
    }
}
