package com.example.spring.demo4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

public class Bean1 {
    private Bean2 bean2;

    @Autowired
    public void setBean2(Bean2 bean2){
        System.out.println("@Autowired 生效" + bean2);
        this.bean2 = bean2;
    }

    @Autowired
    private Bean3 bean3;

    @Resource
    public void setBean3(Bean3 bean3){
        System.out.println("@Resource 生效" + bean3);
        this.bean3 = bean3;
    }

    private String port;

    @Autowired
    public void setPort(@Value("${JAVA_HOME}") String port){
        System.out.println("@Value生效" + port);
        this.port = port;
    }

    @PostConstruct
    public void init(){
        System.out.println("@PostConstruct 生效");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("@PreDestroy 生效");
    }

    @Override
    public String toString() {
        return "Bean1{" +
                "bean2=" + bean2 +
                ", bean3=" + bean3 +
                ", port='" + port + '\'' +
                '}';
    }
}
