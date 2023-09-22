//package com.example.spring.demo19;
package org.springframework.aop.framework.autoproxy;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Field;
import java.util.List;

public class A19 {

    @Aspect
    static class MyAspect{

        @Before("execution(* foo(..))") // 静态通知调用，不带参数绑定  执行时不需要切点
        public void before1(){
            System.out.println("before1");
        }

        @Before("execution(* foo(..)) && args(x)") // 动态通知调用，需要参数绑定 执行时需要切点
        public void before2(int x){
//            InterceptorAndDynamicMethodMatcher 底层实现的类
            System.out.printf("before2(%d)%n",x);
        }
    }

    static class Target {
        public void foo(int x){
            System.out.printf("target foo(%d)%n",x);
        }
    }

    @Configuration
    static class MyConfig{
            @Bean
            AnnotationAwareAspectJAutoProxyCreator proxyCreator(){
                return new AnnotationAwareAspectJAutoProxyCreator();
            }

            @Bean
            public MyAspect myAspect(){
                return new MyAspect();
            }

    }

    public static void main(String[] args) throws Throwable {

        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean(MyConfig.class);
        context.refresh();

        AnnotationAwareAspectJAutoProxyCreator creator = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        List<Advisor> list = creator.findEligibleAdvisors(Target.class, "target");

        Target target = new Target();
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(target);
        factory.addAdvisors(list);
        Object proxy = factory.getProxy(); // 获取代理
        List<Object> interceptList = factory.getInterceptorsAndDynamicInterceptionAdvice(Target.class.getMethod("foo", int.class), Target.class);

        for (Object o : interceptList) {
//            System.out.println(o);
            showDetail(o);
        }

//        受保护的类后面加一个大括号相当于创建子类
        ReflectiveMethodInvocation invocation = new ReflectiveMethodInvocation(
                proxy,target,Target.class.getMethod("foo", int.class),new Object[]{100},Target.class,interceptList
        ){};

       invocation.proceed();



    }

    public static void showDetail(Object o){
        try {
            Class<?> clazz = Class.forName("org.springframework.aop.framework.InterceptorAndDynamicMethodMatcher");
            if(clazz.isInstance(o)){
                Field methodMatcher = clazz.getDeclaredField("methodMatcher");
                methodMatcher.setAccessible(true);
                Field interceptor = clazz.getDeclaredField("interceptor");
                interceptor.setAccessible(true);
                System.out.println("环绕通知和切点：" + o);
                System.out.println("切点为：" + methodMatcher.get(o));
                System.out.println("通知为：" + interceptor.get(o));
            } else {
                System.out.println("普通环绕通知为：" + o);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
