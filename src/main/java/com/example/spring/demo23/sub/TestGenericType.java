package com.example.spring.demo23.sub;

import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class TestGenericType {
    public static void main(String[] args) {
        // 1、jdk
        Type type = StudentDao.class.getGenericSuperclass();
        System.out.println(type);
        if(type instanceof ParameterizedType ){
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            System.out.println(actualTypeArguments[0]);
        }

        // 2、spring API
        Class<?> aClass = GenericTypeResolver.resolveTypeArgument(StudentDao.class, BaseDao.class);
        System.out.println(aClass);

    }
}
