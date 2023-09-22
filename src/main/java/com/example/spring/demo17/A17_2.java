package com.example.spring.demo17;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class A17_2 {
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

        public void afterReturning(){
            System.out.println("afterReturning");
        }
        public void afterThrowing(){
            System.out.println("afterThrowing");
        }
        public Object around(ProceedingJoinPoint pjp) throws Throwable{
            return null;
        }
    }

    static class Target{
        public void foo(){
            System.out.println("target foo...");
        }
    }

    public static void main(String[] args) {
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
            }
        }
        for (Advisor advisor : list) {
            System.out.println(advisor);
        }
    }


}
