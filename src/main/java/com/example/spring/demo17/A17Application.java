//package com.example.spring.demo17;
// 用aop的包就能调用框架里面的方法了。
package org.springframework.aop.framework.autoproxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Method;
import java.util.List;

public class A17Application {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("aspect1",Aspect1.class);
        context.registerBean("config",Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        // 实现了BeanPostProcessor， 在bean注入以及初始化的时候使用，可以获取所有的切面，把高级切面转变成低级切main
        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);

        context.refresh();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {

            System.out.println(beanDefinitionName);
        }

        // 获取匹配器，
        AnnotationAwareAspectJAutoProxyCreator creator = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);

        // 找到所有有资格的 advisors
        // 低级切面是否和Target匹配，返回匹配的低级切面
//        List<Advisor> advisors = creator.findEligibleAdvisors(Target1.class, "target1");
//
//        for (Advisor advisor : advisors) {
//            System.out.println(advisor);
//        }

//        wrapIfNecessary 内部调用了findEligibleAdvisors,只要集合不为空，表示需要创建代理
        Object o1 = creator.wrapIfNecessary(new Target1(), "target1","target1");
        Object o2 = creator.wrapIfNecessary(new Target2(), "target2","target2");



        System.out.println(o1.getClass());
        System.out.println(o2.getClass());

        ((Target1) o1).foo();

        context.close();

    }
    static class Target1{
        public void foo(){
            System.out.println("target1 foo");
        }
        public void bar(){
            System.out.println("target2 bar");
        }
    }
    static class Target2{
        public void bar(){
            System.out.println("target2 bar");
        }
    }

    @Aspect // 高级切面类
    static class Aspect1{
        @Before("execution(* foo())")
        public void before(){
            System.out.println("aspect1 before...");
        }

        @After("execution(* foo())")
        public void after(){
            System.out.println("aspect1 after....");
        }

//        @Before("execution(* bar())")
//        public void before1(){
//            System.out.println("aspect1 after....");
//        }
    }

    @Configuration
    static class Config{
        @Bean
        public Advisor advisor3(MethodInterceptor advice3){
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            return new DefaultPointcutAdvisor(pointcut, advice3 );
        }

        @Bean
        public MethodInterceptor advice3(){
            return new MethodInterceptor() {
                @Override
                public Object invoke(MethodInvocation invocation) throws Throwable {
                    System.out.println("heiha");
                    Object proceed = invocation.proceed(); // 调用目标方法
                    System.out.println("后面");
                    return proceed;
                }
            };
        }


    }

}
