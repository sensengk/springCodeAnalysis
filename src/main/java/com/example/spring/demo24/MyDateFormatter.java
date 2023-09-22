package com.example.spring.demo24;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyDateFormatter implements Formatter<Date> {

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy|MM|dd");
        return sdf.parse(text);
    }

    @Override
    public String print(Date object, Locale locale) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy|MM|dd");

        return sdf.format(object);
    }
}
