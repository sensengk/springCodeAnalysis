package com.example.spring.demo15;


import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class A15Application {
    public static void main(String[] args) {

        // 1、备好切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");

        // 2、 备好通知
        MethodInterceptor advice = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("before....");
                Object proceed = invocation.proceed();
                System.out.println("after.....");
                return proceed;
            }
        };

        // 3 切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut,advice);

        // 4、创建代理
        Target1 target1 = new Target1();
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(target1);
        factory.addAdvisor(advisor);
        factory.setInterfaces(target1.getClass().getInterfaces()); // 设置了接口，告诉代理实现了接口，这样就会使用jdk的方式创建代理
        factory.setProxyTargetClass(true); // 如果是true的话，总会一直用cglib实现
        I1 proxy = (I1)factory.getProxy();
        System.out.println(proxy.getClass());
        proxy.foo();
        proxy.bar();


    }

    interface I1{
        void foo();
        void bar();
    }
    static class Target1 implements I1{

        @Override
        public void foo(){
            System.out.println("target1 foo");
        }

        @Override
        public void bar() {
            System.out.println("target1 bar");
        }
    }
}
