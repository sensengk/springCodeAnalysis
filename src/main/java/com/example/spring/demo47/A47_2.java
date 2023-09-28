package com.example.spring.demo47;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class A47_2 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(A47_2.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();

        System.out.println("1、测试数组");
        testArray(beanFactory);

        System.out.println("2、测试List");
        testList(beanFactory);

        System.out.println("3、测试ApplicationContext");
        testApplicationContext(beanFactory);

        System.out.println("4、测试Generic");
        testGeneric(beanFactory);

        System.out.println("5、测试Qualifier");
        testQualifier(beanFactory);


    }

    private static void testQualifier(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException, IllegalAccessException {
        DependencyDescriptor dd5 = new DependencyDescriptor(Target.class.getDeclaredField("service"), true);
        Class<?> type = dd5.getDependencyType();
        String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, type);
        ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(beanFactory);

        for (String name : names) {
            BeanDefinition bd = beanFactory.getMergedBeanDefinition(name);
            // 内部解析了qualifier注解,并且进行了匹配
            if (resolver.isAutowireCandidate(new BeanDefinitionHolder(bd, name), dd5)) {
                System.out.println(name);
                System.out.println(dd5.resolveCandidate(name,type,beanFactory));
            }
        }


    }

    private static void testGeneric(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException, IllegalAccessException {
        DependencyDescriptor dd4 = new DependencyDescriptor(Target.class.getDeclaredField("teacherDao"), true);
        Class<?> type = dd4.getDependencyType();
        ContextAnnotationAutowireCandidateResolver resolver = new ContextAnnotationAutowireCandidateResolver();
        resolver.setBeanFactory(beanFactory);
        String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, type);
        for (String name : names) {
            BeanDefinition bd = beanFactory.getMergedBeanDefinition(name);
            if(resolver.isAutowireCandidate(new BeanDefinitionHolder(bd,name), dd4)){
                System.out.println(name);
                System.out.println(dd4.resolveCandidate(name,type,beanFactory));
            }

        }


    }



    private static void testApplicationContext(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException, IllegalAccessException {
        DependencyDescriptor dd3 = new DependencyDescriptor(Target.class.getDeclaredField("applicationContext"), true);
        Field resolvableDependencies = DefaultListableBeanFactory.class.getDeclaredField("resolvableDependencies");
        resolvableDependencies.setAccessible(true);
        Map<Class<?>,Object> dependencies = (Map<Class<?>,Object>)resolvableDependencies.get(beanFactory);
        for (Map.Entry<Class<?>, Object> entry : dependencies.entrySet()) {
            if(entry.getKey().isAssignableFrom(dd3.getDependencyType())){
                System.out.println(entry.getValue());
                break;
            }

        }
    }
    private static void testList(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor dd2 = new DependencyDescriptor(Target.class.getDeclaredField("serviceList"), true);
        if(dd2.getDependencyType() == List.class){
            Class<?> resolve = dd2.getResolvableType().getGeneric().resolve();
            System.out.println(resolve);
            String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, resolve);
            List<Object> list = new ArrayList<>();
            for (String name : names) {
                System.out.println(name);
                Object bean = dd2.resolveCandidate(name, resolve, beanFactory);
                list.add(bean);
            }
            System.out.println(list);
        }
    }
    private static void testArray(DefaultListableBeanFactory beanFactory) throws NoSuchFieldException {
        DependencyDescriptor dd1 = new DependencyDescriptor(Target.class.getDeclaredField("serviceArray"), true);
        if(dd1.getDependencyType().isArray()){
            Class<?> componentType = dd1.getDependencyType().getComponentType();
            System.out.println(componentType);
            String[] names = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, componentType);
            List<Object> list = new ArrayList<>();
            for (String name : names) {
                System.out.println(name);
                Object bean = dd1.resolveCandidate(name, componentType, beanFactory);
                list.add(bean);
            }
            Object array = beanFactory.getTypeConverter().convertIfNecessary(list, dd1.getDependencyType());
            System.out.println(array);
        }
    }

    static class Target{
        @Autowired
        private Service[] serviceArray;
        @Autowired
        private List<Service> serviceList;
        @Autowired
        private ConfigurableApplicationContext applicationContext;
        @Autowired
        private Dao<Teacher> teacherDao;
        @Autowired
        @Qualifier("service2")
        private Service service;

    }
    interface Service{

    }
    @Component("service1")
    static class Service1 implements Service{

    }
    @Component("service2")
    static class Service2 implements Service{

    }
    @Component("service3")
    static class Service3 implements Service{

    }
    interface Dao<T>{}

    static class Student{}

    static class Teacher{

    }
    @Component("dao1")
    static class Dao1 implements Dao<Student>{

    }
    @Component("dao2")
    static class Dao2 implements Dao<Teacher>{

    }



}
