package com.example.spring.demo23;

import org.springframework.beans.SimpleTypeConverter;

import java.util.Date;

public class TestSimpleConverter {
    public static void main(String[] args) {
        SimpleTypeConverter typeConverter = new SimpleTypeConverter();
        Integer integer = typeConverter.convertIfNecessary("13", int.class);
        Date date = typeConverter.convertIfNecessary("1999/03/04", Date.class);
        System.out.println(date);
        System.out.println(integer);
    }
}
