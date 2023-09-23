package com.example.spring.demo29;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Configuration
public class WebConfig {

    @ControllerAdvice
    static class MyControllerAdvice implements ResponseBodyAdvice<Object>{
        // 满足条件才转换
        public boolean supports(MethodParameter returnType, Class converterType) {
            if(returnType.getMethodAnnotation(ResponseBody.class) != null ||
                    returnType.getContainingClass().isAnnotationPresent(ResponseBody.class)
                    || AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResponseBody.class) != null){
                return  true;
            }
            return false;
        }

        // 将User或者其他类型转换成Result类型
        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

            System.out.println(body);
            if (body instanceof Result) {
                return body;
            }
            return Result.ok(body);

        }
    }

    @Controller
//    @ResponseBody
//    @RestController
    public static class MyController{
        @ResponseBody
        public User user(){
            return  new User("zhangsan",20);
        }

    }
   public static class User{
       private  String name;
       private int age;

       public User(String name, int age) {
           this.name = name;
           this.age = age;
       }

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       public int getAge() {
           return age;
       }

       public void setAge(int age) {
           this.age = age;
       }

       @Override
       public String toString() {
           return "User{" +
                   "name='" + name + '\'' +
                   ", age=" + age +
                   '}';
       }
   }
}
