package com.example.spring.demo43;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class Bean1PostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if(beanName.equals("bean1") && bean instanceof Bean1 ) {
            System.out.println("Bean1PostProcessor.postProcessBeforeInitialization()");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)  {
        if(beanName.equals("bean1") && bean instanceof Bean1 ) {
            System.out.println("Bean1PostProcessor.postProcessAfterInitialization()");
        }
        return bean;
    }


}
