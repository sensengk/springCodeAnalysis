package com.example.spring.demo13;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class A13Application {
    public static void main(String[] args) {
        Target target = new Target();

        Proxy proxy = new Proxy();
        proxy.setMethodInterceptor(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("before");
//                Object invoke = method.invoke(target, objects); // 反射调用
//                Object invoke = methodProxy.invoke(target, objects); // 内部无反射
                Object o1 = methodProxy.invokeSuper(o, objects); // 内部无反射，结合代理使用
                return o1;
            }
        });
        proxy.save();
        proxy.save(1);
        proxy.save(13L);

    }
}
