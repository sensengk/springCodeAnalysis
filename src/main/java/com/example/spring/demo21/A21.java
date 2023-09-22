package com.example.spring.demo21;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockPart;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.DefaultDataBinderFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class A21 {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WebConfig.class);
        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
        // 准备测试Request
        HttpServletRequest request = mockRequest();

//        控制器方法封装成handlerMethod
        HandlerMethod handlerMethod = new HandlerMethod(new Controller(), Controller.class.getMethod("test", String.class, String.class, int.class, String.class, MultipartFile.class, int.class, String.class, String.class, String.class, HttpServletRequest.class, User.class, User.class, User.class));


//        准备对象绑定和类型转换
        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,null);


//        准备 ModelAndViewContainer 用来存储Model结果
        ModelAndViewContainer container = new ModelAndViewContainer();

//        解析每个参数值
        for (MethodParameter parameter : handlerMethod.getMethodParameters()) {


            HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();

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


            String annotations = Arrays.stream(parameter.getParameterAnnotations()).map(a -> a.annotationType().getSimpleName()).collect(Collectors.joining());
            parameter.initParameterNameDiscovery(new DefaultParameterNameDiscoverer());
            String str = annotations.length() > 0 ? "@" + annotations + " " : "";

            if(composite.supportsParameter(parameter)){

                // 支持此参数
                Object o = composite.resolveArgument(parameter, container, new ServletWebRequest(request), factory);
                System.out.println("[" + parameter.getParameterIndex() + "] " + str + parameter.getParameterType().getSimpleName() + "  " + parameter.getParameterName() + "--->" + o );
                System.out.println("模型数据为：" + container.getModel());

            } else {

                System.out.println("[" + parameter.getParameterIndex() + "] " + str + parameter.getParameterType().getSimpleName() + "  " + parameter.getParameterName() );
            }

        }



    }
    private static HttpServletRequest mockRequest() throws UnsupportedEncodingException {
        MockHttpServletRequest request = new MockHttpServletRequest("get", "/");
        request.setParameter("name1","zhangsan");
        request.setParameter("name2","lisi");
        request.addPart(new MockPart("file","abc","hello".getBytes(StandardCharsets.UTF_8)));
        Map<String, String> stringStringMap = new AntPathMatcher()
                .extractUriTemplateVariables("/test/{id}", "/test/123");
        request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE,stringStringMap);
        request.setContentType("application/json");
        request.setCookies(new Cookie("token","12313"));
        request.setParameter("name","张三");
        request.setParameter("age","18");

        request.setContent("""
                    {
                        "name" : "李四",
                         "age" : 20
                    }
                   """.getBytes(StandardCharsets.UTF_8));
        return new StandardServletMultipartResolver().resolveMultipart(request);
    }
    static class Controller{
        public void test(
                @RequestParam("name1") String name1,
                String name2,
                @RequestParam("age") int age,
                @RequestParam(name = "home",defaultValue = "${JAVA_HOME}") String home1,
                @RequestParam("file") MultipartFile file,
                @PathVariable("id") int id,
                @RequestHeader("Content-Type") String header,
                @CookieValue("token") String token,
                @Value("${JAVA_HOME}") String home2,
                HttpServletRequest request,
                @ModelAttribute("abc") User user1,  // name=zhangsan&age=18
                User user2,       // 省略modelAttribute
                @RequestBody User user3    // json
                ){

        }
    }
    static class User{
        private String name;
        private int age;

        public User() {
        }

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
