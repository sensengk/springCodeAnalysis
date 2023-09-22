package com.example.spring.demo27;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class A27 {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        Method method = Controller.class.getMethod("test1");
        Controller controller = new Controller();
        Object returnValue = method.invoke(controller);

        HandlerMethod handlerMethod = new HandlerMethod(controller, method);
        ModelAndViewContainer container = new ModelAndViewContainer();

        // 1、测试返回值为ModelAndView
        HandlerMethodReturnValueHandlerComposite composite = getReturnValueHandler();
        if(composite.supportsReturnType(handlerMethod.getReturnType())){ // 是否支持此类型
            ServletWebRequest webRequest = new ServletWebRequest(new MockHttpServletRequest(),new MockHttpServletResponse());
            composite.handleReturnValue(returnValue,handlerMethod.getReturnType(),container, webRequest);
            System.out.println(container.getModel());
            System.out.println(container.getViewName());
            renderView(context,container,webRequest);
        }




    }

    public static HandlerMethodReturnValueHandlerComposite getReturnValueHandler(){
        HandlerMethodReturnValueHandlerComposite composite = new HandlerMethodReturnValueHandlerComposite();
        composite.addHandler(new ModelAndViewMethodReturnValueHandler());
        composite.addHandler(new ViewNameMethodReturnValueHandler());
        composite.addHandler(new ServletModelAttributeMethodProcessor(false));
        composite.addHandler(new HttpEntityMethodProcessor(List.of(new MappingJackson2HttpMessageConverter())));
        composite.addHandler(new HttpHeadersReturnValueHandler());
        composite.addHandler(new RequestResponseBodyMethodProcessor(List.of(new MappingJackson2HttpMessageConverter())));
        composite.addHandler(new ServletModelAttributeMethodProcessor(true));
        return composite;
    }

    public static void renderView(AnnotationConfigApplicationContext context, ModelAndViewContainer container, ServletWebRequest servletWebRequest){

    }



    static class Controller{
        public ModelAndView test1(){
            System.out.println("test1");
            ModelAndView mav = new ModelAndView("view1");
            mav.addObject("name","张三");
            return mav;
        }
        public String test2(){
            System.out.println("test2()");
            return "view2";
        }

        @ModelAttribute
        public User test3(){
            System.out.println("test3");
            return new User("李四", 20);
        }

        public User test4(){
            System.out.println("test4");
            return new User("王五", 30);
        }

        public HttpEntity<User> test5(){
            System.out.println("test5");
            return new HttpEntity<>(new User("赵六",40));
        }
        public HttpHeaders test6(){
            System.out.println("test6");
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Content-Type","text/html");
            return httpHeaders;
        }
        @ResponseBody
        public User test7(){
            System.out.println("test7");
            return  new User("钱7", 30);
        }

    }
    public static class User{
        private String name;
        private Integer age;

        public User(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String  toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }
}
