package com.example.spring.demo23;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import java.util.Date;

public class TestServletDataBinder {
    public static void main(String[] args) {
       //  web环境下的数据绑定
        // 利用反射原理，为bean的属性赋值
        MyBean target = new MyBean();
        ServletRequestDataBinder dataBinder = new ServletRequestDataBinder(target);
        dataBinder.initDirectFieldAccess(); // 默认是假，执行方法改为真。

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("a","10");
        request.setParameter("b","hello");
        request.setParameter("c","1999/03/10");

        // 数据来源
        dataBinder.bind(new ServletRequestParameterPropertyValues(request));

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
