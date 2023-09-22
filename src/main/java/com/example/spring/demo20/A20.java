package com.example.spring.demo20;

import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

public class A20 {
    public static void main(String[] args) throws Exception {
        AnnotationConfigServletWebServerApplicationContext context =
                new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

        // 解析 @RequestMapping 以及派生注解 生成路径与控制器方法的映射关系，在初始化时就生成了
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        handlerMethods.forEach((k,v) -> {
            System.out.println(k + "====" + v);
        });
        // 解析完毕之后，请求来了，获取相关的控制器方法
        MockHttpServletRequest request = new MockHttpServletRequest("PUT", "/test4");
        request.setParameter("name","zhangsan");
        request.addHeader("token","某个值");
        MockHttpServletResponse response = new MockHttpServletResponse();

        HandlerExecutionChain chain = handlerMapping.getHandler(request);

        // 包含控制器链， 包含拦截器
        System.out.println(chain);

        System.out.println(">>>>>>>>>>>>>>>>>>>>");
        // handlerAdapter 调用控制器方法
        MyRequestMappingHandlerAdapter handlerAdapter = context.getBean(MyRequestMappingHandlerAdapter.class);
        handlerAdapter.invokeHandlerMethod(request,response,(HandlerMethod) chain.getHandler());

        // 检查响应
        System.out.println(response.getContentAsString());

        System.out.println(">>>>>>>>>>>>>> 参数解析器");
        for (HandlerMethodArgumentResolver resolver : handlerAdapter.getArgumentResolvers()) {
            System.out.println(resolver);
        }

        System.out.println(">>>>>>>>>>>>> 返回值解析器");
        for (HandlerMethodReturnValueHandler handler : handlerAdapter.getReturnValueHandlers()) {
            System.out.println(handler);
        }


    }
}
