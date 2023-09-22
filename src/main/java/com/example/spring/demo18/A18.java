//package com.example.spring.demo18;
package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.*;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class A18 {
    static class Aspect{
        @Before("execution(* foo())")
        public void before1(){
            System.out.println("before1");
        }

        @Before("execution(* foo())")
        public void before2(){
            System.out.println("before2");
        }

        public void after(){
            System.out.println("after");
        }

        @AfterReturning("execution(* foo())")
        public void afterReturning(){
            System.out.println("afterReturning");
        }

        @AfterThrowing("execution(* foo())")
        public void afterThrowing(){
            System.out.println("afterThrowing");
        }

        @Around("execution(* foo())")
        public Object around(ProceedingJoinPoint pjp) throws Throwable{
            return null;
        }
    }

    static class Target{
        public void foo(){
            System.out.println("target foo...");
        }
    }

    public static void main(String[] args) throws Throwable {
        // 高级切面如何转换为低级切面
        SingletonAspectInstanceFactory singletonAspectInstanceFactory = new SingletonAspectInstanceFactory(new Aspect());
        List<Advisor> list = new ArrayList<>();
        for (Method method : Aspect.class.getDeclaredMethods()) {
            if(method.isAnnotationPresent(Before.class))
            {
                // 切点表达式的值
                String expression = method.getAnnotation(Before.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);

                // 通知类
                AspectJMethodBeforeAdvice advice = new AspectJMethodBeforeAdvice(method,pointcut,singletonAspectInstanceFactory);

                // 切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut,advice);
                // 添加切面
                list.add(advisor);
            } else if(method.isAnnotationPresent(AfterReturning.class)){
                // 切点表达式的值
                String expression = method.getAnnotation(AfterReturning.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);

                // 通知类
                AspectJAfterReturningAdvice advice = new AspectJAfterReturningAdvice(method,pointcut,singletonAspectInstanceFactory);

                // 切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut,advice);
                // 添加切面
                list.add(advisor);
            }else if(method.isAnnotationPresent(AfterThrowing.class)){
                // 切点表达式的值
                String expression = method.getAnnotation(AfterThrowing.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);

                // 通知类
                AspectJAfterThrowingAdvice advice = new AspectJAfterThrowingAdvice(method,pointcut,singletonAspectInstanceFactory);

                // 切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut,advice);
                // 添加切面
                list.add(advisor);
            }
        }
        for (Advisor advisor : list) {
            System.out.println(advisor);
        }

        // 统一转换为环绕通知 实现methodInterceptor

//        有一个线程，把调用链维护起来
//         调用链维护所有的通知

        Target target = new Target();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        // 准备把MethodInvocation放入当前线程
        proxyFactory.addAdvice(ExposeInvocationInterceptor.INSTANCE);
        proxyFactory.addAdvisors(list);

        System.out.println(">>>>>>>>>>>>");
        // 会把通知转换成环绕通知
        List<Object> methodInterceptorList = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(Target.class.getMethod("foo"), Target.class);

        for (Object o : methodInterceptorList) {
            System.out.println(o);
        }
        // 创建并执行调用链（环绕通知信息和目标信息）
        MethodInvocation methodInvocation = new ReflectiveMethodInvocation(
                null,target,Target.class.getMethod("foo"), new Object[0],Target.class,methodInterceptorList
        );
        methodInvocation.proceed();



    }


}
