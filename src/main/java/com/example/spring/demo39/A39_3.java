package com.example.spring.demo39;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.*;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;

// 运行时请添加运行参数 --server.port=8080 debug
public class A39_3  {
    @SuppressWarnings("all") // 不显示警告
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication();
        app.addInitializers((context) -> System.out.println("初始化器增强"));

        System.out.println(">>>>>>>>>>>>>>>>2、封装启动args");
        DefaultApplicationArguments arguments = new DefaultApplicationArguments(args);


        System.out.println(">>>>>>>>>>>>>>>>8、创建容器");
        GenericApplicationContext context = createApplicationContext(WebApplicationType.SERVLET);

        System.out.println(">>>>>>>>>>>>>>>>9、准备容器");
        for (ApplicationContextInitializer initializer : app.getInitializers()) {
            initializer.initialize(context);
        }

        System.out.println(">>>>>>>>>>>>>>>>10、加载bean定义");
        DefaultListableBeanFactory beanfactory = context.getDefaultListableBeanFactory();
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(beanfactory);
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(beanfactory);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(beanfactory);

        reader.register(Config.class);
        xmlReader.loadBeanDefinitions(new ClassPathResource("B03.xml"));
        scanner.scan("com.example.spring.demo39.sub");


        System.out.println(">>>>>>>>>>>>>>>>11、刷新容器");
        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println("name" +  name + "来源：" + beanfactory.getBeanDefinition(name).getResourceDescription());
        }

        System.out.println(">>>>>>>>>>>>>>>>12、执行runner");
        // 回调，进行预加载一些数据
        for (CommandLineRunner runner : context.getBeansOfType(CommandLineRunner.class).values()) {
            runner.run(args);
        }
        for (ApplicationRunner runner : context.getBeansOfType(ApplicationRunner.class).values()) {
            runner.run(arguments);
        }






    }
    private static GenericApplicationContext createApplicationContext(WebApplicationType type) {
        GenericApplicationContext
                context = null;
        switch (type){
            case NONE ->
                context = new AnnotationConfigApplicationContext();
            case SERVLET ->
                context = new AnnotationConfigServletWebServerApplicationContext();
            case REACTIVE ->
                context = new AnnotationConfigReactiveWebServerApplicationContext();
        }
        return context;

    }
    static class Bean4{}
    static class Bean5{}
    static class Bean6{}
    static class Config{
        @Bean
        public Bean5 bean5(){
            return new Bean5();
        }

        @Bean
        public ServletWebServerFactory servletWebServerFactory(){
            return new TomcatServletWebServerFactory();
        }
        @Bean
        public CommandLineRunner commandLineRunner(){
            return new CommandLineRunner() {
                @Override
                public void run(String... args) throws Exception {
                    System.out.println(">>>>>>>>>>>>>>>> commandLineRunner" + Arrays.toString(args));
                }
            };
        }

        @Bean
        public ApplicationRunner applicationRunner(){
            return new ApplicationRunner() {
                @Override
                public void run(ApplicationArguments args) throws Exception {
                    System.out.println(">>>>>>>>>>>>>>>> ApplicationRunner" + Arrays.toString(args.getSourceArgs()));
                    System.out.println(args.getOptionNames());
                    System.out.println(args.getOptionValues("server.port"));
                    System.out.println(args.getNonOptionArgs());

                }
            };
        }

    }
}
