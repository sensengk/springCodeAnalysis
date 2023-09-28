package com.example.spring.demo48;

import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@Configuration
public class A48_1 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A48_1.class);


        context.getBean(MyService.class).doBusiness();
        context.close();
    }

    static class MyEvent extends ApplicationEvent{

        public MyEvent(Object source) {
            super(source);
        }
    }

    // 所有的单例bean初始化完成之后，会回调这个方法
    @Bean
    public SmartInitializingSingleton smartInitializingSingleton (ConfigurableApplicationContext context){
        return new SmartInitializingSingleton(){
            @Override
            public void afterSingletonsInstantiated() {
                String[] names = context.getBeanDefinitionNames();
                for (String name : names) {
                    Object bean = context.getBean(name);
                    Method[] methods = bean.getClass().getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(MyListener.class)) {
                            ApplicationListener<ApplicationEvent> listener = new ApplicationListener<>(){

                                @Override
                                public void onApplicationEvent(ApplicationEvent event) {
                                    System.out.println(event);
                                    Class<?> parameterType = method.getParameterTypes()[0];// 获取的参数类型
                                    if(parameterType.isAssignableFrom(event.getClass())){
                                        try {
                                            method.invoke(bean, event);
                                        } catch (InvocationTargetException e) {
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            };
                            context.addApplicationListener(listener);
                        }
                    }

                }
            }
        };
    }

    @Component
    static class MyService {

        @Autowired
        private ApplicationEventPublisher publisher;
        public void doBusiness(){
            System.out.println("主线业务");

            publisher.publishEvent(new MyEvent("发送短信"));
        }
    }

    @Component
    static class SmsApplicationListener implements ApplicationListener<MyEvent> {

        @Override
        public void onApplicationEvent(MyEvent event) {
            System.out.println("收到短信");
        }
    }

    @Component
    static class EmailApplicationListener  {


        @EventListener
        public void listener(MyEvent event) {
            System.out.println("收到邮件");
        }
    }

    @Component
    static class PhoneApplicationListener  {

        @MyListener
        public void listener(MyEvent event) {
            System.out.println("收到电话");
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface MyListener {

    }

    @Bean
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(100);
        executor.setQueueCapacity(50);
        return executor;
    }

    @Bean
    public SimpleApplicationEventMulticaster applicationEventMulticaster(ThreadPoolTaskExecutor executor){
        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        multicaster.setTaskExecutor(executor);
        return multicaster;
    }





}
