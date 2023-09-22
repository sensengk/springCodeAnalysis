package com.example.spring.demo18;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

public class A18_1 {
    static class Target{
        public void foo(){
            System.out.println("Target.foo()");
        }
    }
    static class Advice1 implements MethodInterceptor{

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("Advice1.before()");
            Object proceed = invocation.proceed();// 调用下一个通知或者目标
            System.out.println("Advice1.after()");
            return proceed;
        }
    }
    static class Advice2 implements MethodInterceptor{

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("Advice2.before()");
            Object proceed = invocation.proceed();// 调用下一个通知或者目标
            System.out.println("Advice2.after()");
            return proceed;
        }
    }

    static class MyInvocation implements MethodInvocation{

        private Object target;
        private Method method;
        private Object[] args;
        private List<MethodInterceptor> methodInterceptorList;
        private int count = 1;

        public MyInvocation(Object target, Method method, Object[] args, List<MethodInterceptor> methodInterceptorList) {
            this.target = target;
            this.method = method;
            this.args = args;
            this.methodInterceptorList = methodInterceptorList;
        }


        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object[] getArguments() {
            return args;
        }

        @Override
        public Object proceed() throws Throwable {
            // 调用每一个环绕通知，调用目标
            if(count > methodInterceptorList.size()){
                // 调用目标，返回并结束接口
                Object invoke = method.invoke(target, args);
                return invoke;
            }
            MethodInterceptor interceptor = methodInterceptorList.get(count++ - 1);
            // 逐一调用
            return interceptor.invoke(this);
        }

        @Override
        public Object getThis() {
            return target;
        }

        @Override
        public AccessibleObject getStaticPart() {
            return method;
        }
    }

    public static void main(String[] args) throws Throwable {
        Target target = new Target();
        List<MethodInterceptor> methodInterceptors = List.of(new Advice1(), new Advice2());
        MyInvocation invocation = new MyInvocation(target, Target.class.getMethod("foo"), new Object[0], methodInterceptors);
        invocation.proceed();
    }
}
