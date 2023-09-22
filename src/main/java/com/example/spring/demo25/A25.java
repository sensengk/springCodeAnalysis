package com.example.spring.demo25;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.util.List;

public class A25 {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name","zhangsan");

        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(new WebConfig.Controller1(),
                WebConfig.Controller1.class.getMethod("foo", WebConfig.User.class));

        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,null);
        handlerMethod.setDataBinderFactory(factory);
        handlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
        handlerMethod.setHandlerMethodArgumentResolvers(getArgumentResolvers(context));

        ModelAndViewContainer container = new ModelAndViewContainer();

        handlerMethod.invokeAndHandle(new ServletWebRequest(request),container);

        System.out.println(container.getModel());

        context.close();
    }
    public static HandlerMethodArgumentResolverComposite getArgumentResolvers(AnnotationConfigApplicationContext context){
        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();

        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        composite.addResolvers(
                // false表示有 @RequestParam
                new RequestParamMethodArgumentResolver(beanFactory,false),
                new PathVariableMethodArgumentResolver(),
                new RequestHeaderMethodArgumentResolver(beanFactory),
                new ServletCookieValueMethodArgumentResolver(beanFactory),
                new ExpressionValueMethodArgumentResolver(beanFactory),
                new ServletRequestMethodArgumentResolver(),
                // false表示有 @ModelAttribute
                new ServletModelAttributeMethodProcessor(false), // 必须有ModelAttribute
                new RequestResponseBodyMethodProcessor(List.of(new MappingJackson2HttpMessageConverter())),
                new ServletModelAttributeMethodProcessor(true), // 省略了ModelAttribute
                new RequestParamMethodArgumentResolver(beanFactory,true) // 省略了RequestParam
        );
        return composite;
    }
}
