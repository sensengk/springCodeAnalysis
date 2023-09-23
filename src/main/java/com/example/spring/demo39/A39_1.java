package com.example.spring.demo39;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class A39_1 {
    public static void main(String[] args) {

    }
    static class Bean1{

    }
    static class Bean2{}
    static class Bean3{}
    @Bean
    public Bean2 bean2(){
        return new Bean2();
    }
}
