package com.example.spring.demo8_scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class E {

    @Lazy // 生成代理对象 ，每次获取F的时候，重新生成新的F
    @Autowired
    private F1 f1;


    @Autowired
    private F2 f2;


    @Autowired
    private ObjectFactory<F3> f3;

    @Autowired
    private ApplicationContext context;

    public F1 getF1() {
        return f1;
    }


    public F2 getF2() {
        return f2;
    }

    public F3 getF3() {
        return f3.getObject();
    }

    public F4 getF4() {
        return context.getBean(F4.class);
    }




    public void setF(F1 f) {
        this.f1 = f;
        System.out.println("setF(F f) {}" + f.getClass());
    }

}
