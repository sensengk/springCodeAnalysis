package com.example.spring.demo28;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class A28 {
    public static void main(String[] args) throws IOException, NoSuchMethodException, HttpMediaTypeNotAcceptableException {
        test1();
        test2();
        test3();
        test4();
    }
    private static void test4() throws NoSuchMethodException, HttpMediaTypeNotAcceptableException, IOException {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletWebRequest webRequest = new ServletWebRequest(request,response);

        request.addHeader("Accept","application/xml");
        response.setContentType("application/json");
        // 参数和返回值处理器都能使用
        RequestResponseBodyMethodProcessor processor = new RequestResponseBodyMethodProcessor(
                List.of(
                        new MappingJackson2HttpMessageConverter(),new MappingJackson2XmlHttpMessageConverter()
                )
        );

        processor.handleReturnValue(
                new User("张三",40),
                new MethodParameter(A28.class.getMethod("user"),-1),
                new ModelAndViewContainer(),
                webRequest
        );

        System.out.println( new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    @ResponseBody
    public User user(){
        return null;
    }


    public static void test3() throws IOException {
        MockHttpInputMessage message = new MockHttpInputMessage("""
                    {
                        "name" : "李四",
                        "age" : 20
                    }
                """.getBytes(StandardCharsets.UTF_8));
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if(converter.canRead(User.class, MediaType.APPLICATION_JSON)){
            Object read = converter.read(User.class, message);
            System.out.println(read);
        }

    }
    public static void test2() throws IOException {
        MockHttpOutputMessage message = new MockHttpOutputMessage();
        MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter();
        if(converter.canWrite(User.class, MediaType.APPLICATION_XML)){
            converter.write(new User("李四",20),MediaType.APPLICATION_XML,message);
            System.out.println(message.getBodyAsString());
        }
    }
    public static void test1() throws IOException {
        MockHttpOutputMessage message = new MockHttpOutputMessage();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if(converter.canWrite(User.class, MediaType.APPLICATION_JSON)){
            converter.write(new User("张三",18),MediaType.APPLICATION_JSON,message);
            System.out.println(message.getBodyAsString());
        }

    }
    public static class User{
        private String name;
        private int age;

        @JsonCreator
        public User(@JsonProperty("name") String name,@JsonProperty("age") int age) {
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
