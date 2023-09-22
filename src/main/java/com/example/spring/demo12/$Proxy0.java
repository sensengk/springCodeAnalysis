package com.example.spring.demo12;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

public class $Proxy0 implements A12.Foo {
    private A12.InvocationHandler handler;

    public $Proxy0(A12.InvocationHandler invocationHandler){
        this.handler = invocationHandler;
    }

    static Method foo;
    static Method bar;
    static {
        try {
            foo = A12.Foo.class.getMethod("foo");
            bar = A12.Foo.class.getMethod("bar");
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }

    }

    @Override
    public void foo() {
        try {
            handler.invoke(this,foo, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int bar() {
        try {
            Object invoke = handler.invoke(this,  bar, new Object[0]);
            return (int)invoke;
        } catch (RuntimeException | Error e) {
           throw e;
        } catch (Throwable e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
