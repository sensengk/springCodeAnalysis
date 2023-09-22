package com.example.spring.demo12;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class A12 {
    interface Foo{
        void foo();
        int bar();
    }
    static class Target implements Foo{

        @Override
        public void foo() {
            System.out.println("target foo");
        }

        @Override
        public int bar() {
            System.out.println("target bar");
            return 100;
        }
    }
    public interface InvocationHandler{
        Object invoke(Object proxy, Method method, Object[] args) throws Exception;
    }

    public static void main(String[] args) {

        Foo proxy = new $Proxy0(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {

                System.out.println("before");
                Object invoke = method.invoke(new Target(), args);
                System.out.println("after");
                return invoke;
            }
        });

        proxy.bar();
        proxy.foo();

    }
}

