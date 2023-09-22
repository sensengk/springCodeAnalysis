package com.example.spring.demo11;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyDemo {
    interface Foo{
        void foo();
    }

    static class Target implements Foo{
        @Override
        public void foo() {
            System.out.println("target demo");
        }
    }

    public static void main(String[] args) {
        ClassLoader classLoader = JdkProxyDemo.class.getClassLoader();
        Target target = new Target();
        Foo foo = (Foo)Proxy.newProxyInstance(classLoader, new Class[]{Foo.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                System.out.println("before");
                Object invoke = method.invoke(target, args);
                System.out.println("after");

                return invoke;


            }
        });

        foo.foo();

    }

}

