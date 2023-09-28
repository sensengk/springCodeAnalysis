package com.example.spring.demo43;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component("bean1")
public class Bean1FactoryBean implements FactoryBean<Bean1> {

    @Override
    public Bean1 getObject() throws Exception {
        System.out.println("create bean1");
        return new Bean1();
    }

    @Override
    public Class<?> getObjectType() {
        return Bean1.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
