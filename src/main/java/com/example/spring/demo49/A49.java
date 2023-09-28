package com.example.spring.demo49;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.GenericApplicationListener;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.ResolvableType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.function.Predicate;


@Configuration
public class A49 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A49.class);

        context.getBean(MyService.class).doBusiness();
        context.close();
    }

    static class MyEvent extends ApplicationEvent{

        public MyEvent(Object source) {
            super(source);
        }
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
    static class EmailApplicationListener implements ApplicationListener<MyEvent>  {
        @Override
        public void onApplicationEvent(MyEvent event) {
            System.out.println("收到短信");
        }
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
    public ApplicationEventMulticaster applicationEventMulticaster(ConfigurableApplicationContext context, ThreadPoolTaskExecutor executor){

        return new AbstractApplicationEventMulticaster(){

            private ArrayList<GenericApplicationListener> listeners = new ArrayList<>();

            // 收集监听器
            @Override
            public void addApplicationListenerBean(String listenerBeanName) {

                System.out.println(listenerBeanName + ">>>>>>>>>>>");
                ApplicationListener listener = context.getBean(listenerBeanName, ApplicationListener.class);
                ResolvableType type = ResolvableType.forClass(listener.getClass()).getInterfaces()[0].getGeneric();

                GenericApplicationListener genericApplicationListener = new GenericApplicationListener(){

                    @Override
                    public void onApplicationEvent(ApplicationEvent event) {
                        listener.onApplicationEvent(event);
                    }

                    @Override
                    public boolean supportsEventType(ResolvableType eventType) {
                        return type.isAssignableFrom(eventType);
                    }
                };
                System.out.println(type);
                listeners.add(genericApplicationListener);

            }

            // 发布事件
            @Override
            public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {
                for (GenericApplicationListener listener : listeners) {
                    if(listener.supportsEventType(ResolvableType.forClass(event.getClass()))){
                        executor.submit(() ->{
                            listener.onApplicationEvent(event);
                        });
                    }
                }
            }
        };
    }

     abstract static class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster {


         @Override
         public void addApplicationListener(ApplicationListener<?> listener) {

         }

         @Override
         public void addApplicationListenerBean(String listenerBeanName) {

         }

         @Override
         public void removeApplicationListener(ApplicationListener<?> listener) {

         }

         @Override
         public void removeApplicationListenerBean(String listenerBeanName) {

         }

         @Override
         public void removeApplicationListeners(Predicate<ApplicationListener<?>> predicate) {

         }

         @Override
         public void removeApplicationListenerBeans(Predicate<String> predicate) {

         }

         @Override
         public void removeAllListeners() {

         }

         @Override
         public void multicastEvent(ApplicationEvent event) {

         }

         @Override
         public void multicastEvent(ApplicationEvent event, ResolvableType eventType) {

         }
     }





}
