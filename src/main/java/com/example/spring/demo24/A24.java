package com.example.spring.demo24;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.method.ControllerAdviceBean;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class A24 {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
    /*    @InitBinder 来源有两个
        1、@ControllerAdvice 中的@InitBinder 由RequestmappingHandlerAdapter 在初始化时解析并记录
        2、@Controller 中的@InitBinder 由RequestmappingHandlerAdapter 在控制器方法首次执行时解析并记录*/
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);

        RequestMappingHandlerAdapter handlerAdapter = new RequestMappingHandlerAdapter();
        handlerAdapter.setApplicationContext(context);
        handlerAdapter.afterPropertiesSet();

        System.out.println("刚开始");
        showBindMethods(handlerAdapter);

        Method getDataBinderFactory = RequestMappingHandlerAdapter.class.getDeclaredMethod("getDataBinderFactory", HandlerMethod.class);
        getDataBinderFactory.setAccessible(true);
        System.out.println("模拟调用Controller1的foo方法");

        getDataBinderFactory.invoke(handlerAdapter,new HandlerMethod(new WebConfig.Controller1(), WebConfig.Controller1.class.getMethod("foo")));
        showBindMethods(handlerAdapter);

        System.out.println("模拟调用Controller2的bar方法");

        getDataBinderFactory.invoke(handlerAdapter,new HandlerMethod(new WebConfig.Controller2(), WebConfig.Controller2.class.getMethod("bar")));
        showBindMethods(handlerAdapter);


        context.close();

    }
    public static void showBindMethods(RequestMappingHandlerAdapter handlerAdapter) throws NoSuchFieldException, IllegalAccessException {
        Field initBinderAdviceCache = RequestMappingHandlerAdapter.class.getDeclaredField("initBinderAdviceCache");
        initBinderAdviceCache.setAccessible(true);
        Map<ControllerAdviceBean, Set<Method>> globalMap = (Map<ControllerAdviceBean, Set<Method>>)initBinderAdviceCache.get(handlerAdapter);
        System.out.println("全局的@InitBinder方法," + globalMap.values().stream().
                flatMap(ms -> ms.stream().map(m -> m.getName())).collect(Collectors.toList())
        );

        Field initBinderCache = RequestMappingHandlerAdapter.class.getDeclaredField("initBinderCache");

        initBinderCache.setAccessible(true);
        Map<Class<?>, Set<Method>> controllerMap = (Map<Class<?>, Set<Method>>)initBinderCache.get(handlerAdapter);
        System.out.println("局部的@InitBinder方法，" + controllerMap.entrySet().stream().flatMap(
                e -> e.getValue().stream().map(v -> e.getKey().getSimpleName() + "." + v.getName())).collect(Collectors.toList())
        );


    }
}
