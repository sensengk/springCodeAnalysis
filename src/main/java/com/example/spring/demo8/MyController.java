package com.example.spring.demo8;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class MyController {

    @Lazy
    @Autowired
    private BeanForSession beanForSession;

    @Lazy
    @Autowired
    private BeanForApplication beanForApplication;

    @Lazy
    @Autowired
    private BeanForRequest beanForRequest;

    @GetMapping(value="/test", produces = "text/html")
    public String test(HttpServletRequest request, HttpSession session){
        ServletContext servletContext = request.getServletContext();
        String sb = "<ul> " +
                "<li> request Scope:" + beanForRequest +"</li>" +
                "<li> session Scope:" + beanForSession +"</li>" +
                "<li> application Scope:" + beanForApplication +"</li>" +
                 " </ul>";
        return sb;

    }


}
