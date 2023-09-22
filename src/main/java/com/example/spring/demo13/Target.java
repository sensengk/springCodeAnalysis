package com.example.spring.demo13;

public class Target {
    public void save(){
        System.out.println("保存");
    }

    public void save(int i){
        System.out.println(i + "保存int");
    }

    public void save(long j){
        System.out.println(j + "保存long");
    }
}
