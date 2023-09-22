package com.example.spring.demo3;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;

@Component
public class MyBeanPostProcessor implements InstantiationAwareBeanPostProcessor, DestructionAwareBeanPostProcessor {
    @Override
    public void postProcessBeforeDestruction(Object Bean, String beanName) throws BeansException {
        // 只有lifeCycleBean才会打印输出
        if(beanName.equals("lifeCycleBean")){
            System.out.println("<<<<<<<销毁之前执行");
        }
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return DestructionAwareBeanPostProcessor.super.requiresDestruction(bean);
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if(beanName.equals("lifeCycleBean")){
            System.out.println("<<<<<<<实例化之前执行");
        }
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if(beanName.equals("lifeCycleBean")){
            System.out.println("<<<<<<<实例化之后执行,如果返回false,跳过依赖注入阶段");
        }
        //
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if(beanName.equals("lifeCycleBean")){
            System.out.println("<<<<<<<依赖注入阶段执行,如@Autowired,@Value,@Resource");
        }
        return pvs;
    }



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("lifeCycleBean")){
            System.out.println("<<<<<<<初始化之前执行,这里返回的对象会替换原来的bean");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(beanName.equals("lifeCycleBean")){
            System.out.println("<<<<<<<初始化之后执行,这里返回的对象会替换原来的bean，如代理增强");
        }
        return bean;
    }


}
