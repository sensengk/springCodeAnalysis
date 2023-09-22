package com.example.spring.demo3;

import org.apache.naming.factory.BeanFactory;

import java.util.ArrayList;
import java.util.List;

public class TestMethodTemplate {
    public static void main(String[] args) {
        // 模拟bean工厂的如何构造bean
        MyBeanFactory beanFactory = new MyBeanFactory();
        beanFactory.addBeanPostProcessor(bean -> System.out.println("解析autowired"));
        beanFactory.addBeanPostProcessor(bean -> System.out.println("解析resource"));
        beanFactory.getBean();
    }
    // 模版方法设计模式 如果构造bean
    static class MyBeanFactory{
        public Object getBean(){
            Object bean = new Object();
            System.out.println("构造Bean:" + bean);
            System.out.println("依赖注入：" + bean);
            // 对依赖注入进行拓展
            for (BeanPostProcessor processor : processors) {
                processor.inject(bean);
            }
            System.out.println("初始化：" + bean);
            return bean;
        }
        private List<BeanPostProcessor> processors = new ArrayList<>();
        public void addBeanPostProcessor(BeanPostProcessor processor){

            processors.add(processor);

        }


    }

    static interface BeanPostProcessor{
        public void inject(Object bean); // 对依赖注入阶段的拓展
    }




}
