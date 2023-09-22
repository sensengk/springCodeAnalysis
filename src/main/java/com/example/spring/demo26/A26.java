package com.example.spring.demo26;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.ModelFactory;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.lang.reflect.Method;
import java.util.List;

public class A26 {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);

        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        adapter.setApplicationContext(context);
        adapter.afterPropertiesSet();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name","zhangsan");

        ServletInvocableHandlerMethod handlerMethod = new ServletInvocableHandlerMethod(new WebConfig.Controller1(),
                WebConfig.Controller1.class.getMethod("foo", WebConfig.User.class));

        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,null);
        handlerMethod.setDataBinderFactory(factory);
        handlerMethod.setParameterNameDiscoverer(new DefaultParameterNameDiscoverer());
        handlerMethod.setHandlerMethodArgumentResolvers(getArgumentResolvers(context));

        ModelAndViewContainer container = new ModelAndViewContainer();

        Method getModelFactory = RequestMappingHandlerAdapter.class.getDeclaredMethod("getModelFactory", HandlerMethod.class, WebDataBinderFactory.class);
        getModelFactory.setAccessible(true);
        ModelFactory modelFactory = (ModelFactory)getModelFactory.invoke(adapter, handlerMethod,factory);

        modelFactory.initModel(new ServletWebRequest(request),container,handlerMethod);

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
