package com.example.spring.demo16;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

public class A16Applicaiton {
    public static void main(String[] args) throws NoSuchMethodException {

        AspectJExpressionPointcut pt1 = new AspectJExpressionPointcut();
        pt1.setExpression("execution(* bar())");
        System.out.println(pt1.matches(T1.class.getMethod("foo"), T1.class));
        System.out.println(pt1.matches(T1.class.getMethod("bar"), T1.class));

        AspectJExpressionPointcut pt2 = new AspectJExpressionPointcut();
        pt2.setExpression("@annotation(org.springframework.transaction.annotation.Transactional)");
        System.out.println(pt2.matches(T1.class.getMethod("foo"), T1.class));
        System.out.println(pt2.matches(T1.class.getMethod("bar"), T1.class));

        // 上面两种有局限性，只能判断类里面的方法， 但实际中需要判断class, interface

        // 可以匹配类上以及方法上，如果类上匹配上，那么所有的方法都要增强
        StaticMethodMatcherPointcut pt3 = new StaticMethodMatcherPointcut(){

            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                // 检查方法上是否有 Transactional 注解
                // TYPE_HIERARCHY 从继承树上进行查找
                MergedAnnotations annotations = MergedAnnotations.from(method, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);
                if(annotations.isPresent(Transactional.class)){
                    return true;
                }

                // 检查类上是否有注解
                annotations = MergedAnnotations.from(targetClass);
                if(annotations.isPresent(Transactional.class)){
                    return true;
                }
                return false;
            }
        };

        System.out.println(pt3.matches(T1.class.getMethod("foo"), T1.class));
        System.out.println(pt3.matches(T1.class.getMethod("bar"), T1.class));
        System.out.println(pt3.matches(T2.class.getMethod("foo"), T2.class));
        System.out.println(pt3.matches(T3.class.getMethod("foo"), T2.class));




    }

    static class T1{

        @Transactional
        public void foo(){

        }

        public void bar(){

        }

    }

    @Transactional
    static class T2{
        public void foo(){}
    }
    @Transactional
    interface I3{
        void foo();
    }
    static class T3 implements I3{

        @Override
        public void foo() {

        }
    }
}
