package com.example.spring.demo6;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

public class MyBean implements BeanNameAware, ApplicationContextAware, InitializingBean {
    @Override
    public void setBeanName(String s) {
        System.out.println("当前的bean " + this + "当前的名字" + s);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("当前的bean " + this + "当前的容器" + applicationContext);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("当前的bean " + this + "初始化");

    }

    @Autowired
    public void getApplicationContext(ApplicationContext applicationContext){
        System.out.println("注入applicationContext");
    }

    @PostConstruct
    public void init(){
        System.out.println("初始化");
    }
}
