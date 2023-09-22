package com.example.spring.demo23;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import java.util.Date;

public class TestDataBinder {
    public static void main(String[] args) {
        // 利用反射原理，为bean的属性赋值
        MyBean target = new MyBean();
        DataBinder dataBinder = new DataBinder(target);
        dataBinder.initDirectFieldAccess(); // 默认是假，执行方法改为真。
        // 数据来源
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.add("a","10");
        pvs.add("b","hello");
        pvs.add("c","1999/03/10");
        dataBinder.bind(pvs);

        System.out.println(target);

    }

    static class MyBean{
        private int a;
        private String b;
        private Date c;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public String getB() {
            return b;
        }

        public void setB(String b) {
            this.b = b;
        }

        public Date getC() {
            return c;
        }

        public void setC(Date c) {
            this.c = c;
        }

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
