package com.example.spring.demo41;

import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

public class TestAopAuto {
    public static void main(String[] args) {

        GenericApplicationContext context = new GenericApplicationContext();
        AnnotationConfigUtils.registerAnnotationConfigProcessors(context.getDefaultListableBeanFactory());

        context.registerBean(Config.class);

        context.refresh();

        String[] names = context.getBeanDefinitionNames();
        for (String name : names) {
            System.out.println(name);
        }

    }

    @Configuration // 本项目配置类
    @Import(MyImportSelector.class)
    static class Config{

    }
    // DeferredImportSelector 推迟导入
    static class MyImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            List<String> names = SpringFactoriesLoader.loadFactoryNames(A41.MyImportSelector.class, null);
            return new String[]{AopAutoConfiguration.class.getName()};
        }
    }
}
