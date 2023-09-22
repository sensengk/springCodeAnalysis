package com.example.spring.demo27;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class WebConfig {
    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer(){

        return null;
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver(FreeMarkerConfigurer configurer){
        return null;
    }
}
