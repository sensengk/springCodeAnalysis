package com.example.spring.demo4;

//AutowiredAnnotationBeanPostProcessor 运行解析

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DigInAutowired {
    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerSingleton("bean2",new Bean2());
        beanFactory.registerSingleton("bean3",new Bean3());

        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver()); //解析value
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders); // ${}的解析器

        // 查找哪些属性，方法加了@Autowired,这称之为InjectionMetaData
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
//        System.out.println(bean1);
//        processor.postProcessProperties(null, bean1,"bean1"); 这一步相当于下面的两步
//        System.out.println(bean1);

//      1、用反射的方式  查找哪些属性，方法加了@Autowired,这称之为InjectionMetaData
        Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);
        InjectionMetadata metadata = (InjectionMetadata)findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);
        System.out.println(metadata);

//        2、调用 InjectionMetaData来进行依赖注入，注入时按类型查找值
        metadata.inject(bean1,"bean1",null);
        System.out.println(bean1);

//        3、如何按照类型查找 ，内部会按照类型来进行查找 内部如何查找类型的方式，其实是封装成 dependencyDescriptor
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        // 进行构造
        DependencyDescriptor dd1 = new DependencyDescriptor(bean3, false);
        Object o = beanFactory.doResolveDependency(dd1, null, null, null);
        System.out.println(o);

        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor dd2 = new DependencyDescriptor(new MethodParameter(setBean2, 0), false);
        Object o2 = beanFactory.doResolveDependency(dd2, null, null, null);

        System.out.println(o2);

        Method setPort = Bean1.class.getDeclaredMethod("setPort", String.class);
        DependencyDescriptor dd3 = new DependencyDescriptor(new MethodParameter(setPort, 0), false);
        Object o3 = beanFactory.doResolveDependency(dd3, null, null, null);

        System.out.println(o3);

    }
}
