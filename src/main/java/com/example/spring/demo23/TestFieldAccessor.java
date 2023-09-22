package com.example.spring.demo23;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.DirectFieldAccessor;

import java.util.Date;

public class TestFieldAccessor {
    public static void main(String[] args) {
        // 利用反射原理，为bean的属性赋值
        MyBean target = new MyBean();
        DirectFieldAccessor wrapper = new DirectFieldAccessor(target);
        wrapper.setPropertyValue("a","10");
        wrapper.setPropertyValue("b","hello");
        wrapper.setPropertyValue("c","1999/03/10");

        System.out.println(target);



    }

    static class MyBean{
        private int a;
        private String b;
        private Date c;


        @Override
        public String toString() {
            return "MyBean{" +
                    "a=" + a +
                    ", b='" + b + '\'' +
                    ", c=" + c +
                    '}';
        }
    }
}
