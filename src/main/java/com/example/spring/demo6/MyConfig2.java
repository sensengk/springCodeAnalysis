package com.example.spring.demo6;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfig2 implements InitializingBean, ApplicationContextAware {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("注入 ApplicationContext");
    }

    @Bean
    public BeanFactoryPostProcessor processor2(){
        return beanFactory -> {
            System.out.println("执行processor2");
        };
    }
}
