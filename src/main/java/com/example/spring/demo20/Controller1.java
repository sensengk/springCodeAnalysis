package com.example.spring.demo20;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.Yaml;

@Controller
public class Controller1 {
    @GetMapping("/test1")
    public ModelAndView test1() throws Exception{
        System.out.println("test1(");
        return null;
    }
    @PostMapping("/test2")
    public ModelAndView test2(@RequestParam("name") String name) throws Exception{
        System.out.println("test2" + name);
        return null;
    }
    @PutMapping("/test3")
    public ModelAndView test3(@Token String token){
        System.out.println("token" + token);
        return null;
    }
    @RequestMapping("/test4")
    @Yml
    public User test4(){
        System.out.println("test4");
        return new User("zhangsan", 20.22);
    }

    public static class User{
        private String name;
        private Double age;

        public User(String name, Double age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getAge() {
            return age;
        }

        public void setAge(Double age) {
            this.age = age;
        }
    }

    public static void main(String[] args) {
        String str = new Yaml().dump(new User("zhangsan", 20.22));
        System.out.println(str);


    }
}
