package com.example.spring.demo5;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.spring.demo5.component.Bean2;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.spring.demo5.component")
public class Config {

    public Bean2 bean2(){
        return new Bean2();
    }

    @Bean
    public Bean1 bean1(){
        return new Bean1();
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean( DruidDataSource dataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean(initMethod = "init")
    public DruidDataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/guonenglocal");
        dataSource.setUsername("root");
        dataSource.setPassword("123456ws");
        return dataSource;
    }



}
