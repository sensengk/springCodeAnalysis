package com.example.spring.demo47;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Configuration
public class A47_3 {
    public static void main(String[] args) throws NoSuchFieldException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A47_3.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();

        System.out.println("1、测试primary");
        testPrimary(beanFactory);

        System.out.println("2、测试Default");
        testDefault(beanFactory);


    }

    private static void testDefault(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor dd2 = new DependencyDescriptor(Target2.class.getDeclaredField("service3"), true);

        Class<?> type = dd2.getDependencyType();
        String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, type);

        for (String name : names) {
            if (name.equals(dd2.getDependencyName())) {
                System.out.println(name);
            }
        }

    }
    private static void  testPrimary(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor dd1 = new DependencyDescriptor(Target1.class.getDeclaredField("service"), true);

        Class<?> type = dd1.getDependencyType();
        String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, type);

        for (String name : names) {
            if (beanFactory.getMergedBeanDefinition(name).isPrimary()) {
                System.out.println(name);
            }
        }
    }
    static class Target1{
        @Autowired
        private Service service;
    }
    static class Target2{
        @Autowired
        private Service service3;
    }


    interface Service{

    }
    @Component("service1")
    static class Service1 implements Service {

    }
    @Component("service2")
    @Primary
    static class Service2 implements Service {

    }
    @Component("service3")
    static class Service3 implements Service {

    }

}
