package com.example.spring.demo2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Arrays;

public class TestBeanFactory {
    public static void main(String[] args) {

//        用beanfactory管理下面的bean
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 定义beanDefinition
        AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();

        beanFactory.registerBeanDefinition("config",beanDefinition);

        // 增加一些后处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        // 获取处理器执行处理器
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values().stream().forEach(beanFactoryPostProcessor->{
            // 这些后处理才能解析config,并且把config里面的bean添加到beanfactory
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        });

        // bean处理器，针对bean的生命周期的一些阶段进行拓展
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream()
                // 根据order, 进行排序
                .sorted(beanFactory.getDependencyComparator())
                .forEach(beanPostProcessor -> {
            // beanPostProcessor的添加顺序会影响解析的顺序，谁先添加谁奏效。
            System.out.println("beanPostProcessor-------->" + beanPostProcessor);
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        });

        // 预先实例化单例bean
        beanFactory.preInstantiateSingletons();

        // 输出
        Arrays.stream(beanFactory.getBeanDefinitionNames()).forEach(System.out::println);

        System.out.println(beanFactory.getBean(Bean1.class).getBean2());
        System.out.println(beanFactory.getBean(Bean1.class).getInter());


    }

    @Configuration
    static class Config{
        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(){
            return new Bean2();
        }

        @Bean
        public Bean3 bean3(){
            return new Bean3();
        }

        @Bean
        public Bean4 bean4(){
            return new Bean4();
        }

    }

    interface Inter{

    }

    static class Bean3 implements Inter{
        public Bean3(){
            System.out.println("构造Bean3()");
        }
    }
    static class Bean4 implements Inter{
        public Bean4(){
            System.out.println("构造Bean4()");
        }
    }


    static class Bean1{
        public Bean1(){
            System.out.println("构造Bean1()");
        }
        @Autowired
        private Bean2 bean2;
        public Bean2 getBean2(){
            return bean2;
        }

        @Autowired
        @Resource(name = "bean4")
        private Inter bean3;

        public Inter getInter() {
            return bean3;
        }
    }

    static class Bean2{
        public Bean2(){
            System.out.println("构造Bean2()");
        }
    }
}
