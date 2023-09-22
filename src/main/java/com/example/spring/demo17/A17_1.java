package com.example.spring.demo17;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;

public class A17_1 {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean( Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.refresh();
        context.close();

    }

    @Configuration
    static class Config{
        @Bean // 解析aspect ,并且产生代理
        public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator(){
            return new AnnotationAwareAspectJAutoProxyCreator();
        }
        @Bean
        public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor(){
            return new AutowiredAnnotationBeanPostProcessor();
        }
        @Bean
        public CommonAnnotationBeanPostProcessor commonAnnotationBeanPostProcessor(){
            return new CommonAnnotationBeanPostProcessor();
        }
        @Bean
        public Advisor advisor(MethodInterceptor advice){
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            return new DefaultPointcutAdvisor(advice);
        }
        @Bean
        public MethodInterceptor advice(){
            return  new MethodInterceptor() {
                @Override
                public Object invoke(MethodInvocation invocation) throws Throwable {
                    System.out.println("before...");
                    Object proceed = invocation.proceed();
                    System.out.println("after>>>");
                    return proceed;
                }
            };
        }
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }
    }
    static class Bean1{
        public void foo(){
            System.out.println("bean1中的foo执行了");
        }
        public Bean1(){
            System.out.println("bean1();");
        }
        @Autowired
        public void setBean1(Bean2 bean2){
            System.out.println("bean2注入了");
        }
        @PostConstruct
        public void init(){
            System.out.println("bean1 init");
        }


    }
    static class Bean2{
        public Bean2(){
            System.out.println("bean2()");
        }
        @Autowired
        public void setBean1(Bean1 bean1){
            System.out.println("bean1注入了");
        }
        @PostConstruct
        public void init(){
            System.out.println("initbean2");
        }
    }
}
