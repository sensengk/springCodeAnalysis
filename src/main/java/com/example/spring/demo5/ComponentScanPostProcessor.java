package com.example.spring.demo5;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class ComponentScanPostProcessor implements BeanFactoryPostProcessor {

    @Override // context.refresh的时候进行调用
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        // 去哪个类上，寻找哪些注解
        try {
            ComponentScan annotation = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
            if(annotation != null) {
                for (String s : annotation.basePackages()) {
    //                转变成类路径的形式
    //                com.example.spring.demo5.component -> classpath*:com/example/spring/demo5/component/**/*.class
                    String path = "classpath*:" + s.replace(".","/") + "/**/*.class";
                    CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
                    // 用来获取资源
                    Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
                    AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();

                    for (Resource resource : resources) {
                        MetadataReader reader = factory.getMetadataReader(resource);
                        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
                        if(annotationMetadata.hasAnnotation(Component.class.getName()) ||
                                annotationMetadata.hasMetaAnnotation(Component.class.getName())){

                            AbstractBeanDefinition bd = BeanDefinitionBuilder.genericBeanDefinition(reader.getClassMetadata().getClassName()).getBeanDefinition();
                            if(configurableListableBeanFactory instanceof DefaultListableBeanFactory ){
                                DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableListableBeanFactory;
                                String name = generator.generateBeanName(bd, beanFactory );
                                beanFactory.registerBeanDefinition(name,bd);
                            }

                        }

                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
