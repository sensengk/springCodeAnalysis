package com.example.spring.demo23;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.Date;
import java.util.List;

public class TestServletDataBinderFactory {
    public static void main(String[] args) throws Exception {

        // 参数值
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("birthday","1999|10|12");
        request.setParameter("address.name","西安");


        // 目标
        User target = new User();

        // 1、用工厂，无转换功能
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null,null);
//        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), target, "user");

        // 2、用@InitBinder 转换  propertyEditorRegistry PropertyEditor
//        InvocableHandlerMethod method = new InvocableHandlerMethod(new MyController(),MyController.class.getMethod("aaa", WebDataBinder.class));
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(List.of(method),null);

        // 3、用conversionService转换 ConversionService Formatter
//        FormattingConversionService service = new FormattingConversionService();
//        service.addFormatter(new MyDateFormatter());
//
//        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
//        initializer.setConversionService(service);

        // 4同时加了 @InitBinder 和 ConversionService
//        InvocableHandlerMethod method = new InvocableHandlerMethod(new MyController(),MyController.class.getMethod("aaa", WebDataBinder.class));
//        FormattingConversionService service = new FormattingConversionService();
//        service.addFormatter(new MyDateFormatter());
//
//        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
//        initializer.setConversionService(service);
//
//        // 谁优先级高，调用哪个， initBinder更高
//        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(List.of(method), initializer);

        // 5、 使用默认的ConversionService
        // springboot使用 ApplicationConversionService 这个注解
        DefaultFormattingConversionService service = new DefaultFormattingConversionService();
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(service);

        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, initializer);


        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), target, "user");
        // 进行参数值绑定
        dataBinder.bind(new ServletRequestParameterPropertyValues(request));


        System.out.println(target);

    }

    static class MyController{

        @InitBinder
        public void aaa(WebDataBinder dataBinder){
            // 扩展dataBinder功能
            dataBinder.addCustomFormatter(new MyDateFormatter());

        }
    }

   public static class User {
        // DefaultFormattingConversionService 这个service需要添加这个注解才能进行转换类型
        @DateTimeFormat(pattern = "yyyy|MM|dd")
        private Date birthday;
        private Address address;

       public Date getBirthday() {
           return birthday;
       }

       public void setBirthday(Date birthday) {
           this.birthday = birthday;
       }

       public Address getAddress() {
           return address;
       }

       public void setAddress(Address address) {
           this.address = address;
       }

       @Override
       public String toString() {
           return "User{" +
                   "birthday=" + birthday +
                   ", address=" + address +
                   '}';
       }
   }

   public static   class Address{

        private String name;

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }

       @Override
       public String toString() {
           return "Address{" +
                   "name='" + name + '\'' +
                   '}';
       }
   }

}
