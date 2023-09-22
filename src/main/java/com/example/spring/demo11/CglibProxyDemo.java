package com.example.spring.demo11;


import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyDemo {

    static class Target{
        public void foo(){
            System.out.println("foo");
        }
    }

    public static void main(String[] args) {
        Target target = new Target();

        Target proxy = (Target)Enhancer.create(Target.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

                System.out.println("before");

//                Object invoke = method.invoke(target, args);
                // 内部不是通过反射调用的，需要目标类
//                Object invoke = methodProxy.invoke(target, args);
//                 不用调用外部的target，内部也没用反射，需要代理
                Object invoke = methodProxy.invokeSuper(o, args);
                System.out.println("after...");
                return invoke;
            }
        });
        proxy.foo();
    }

}
