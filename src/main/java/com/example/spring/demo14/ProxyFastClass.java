package com.example.spring.demo14;

import org.springframework.cglib.core.Signature;


public class ProxyFastClass {

    static Signature s0 = new Signature("saveSuper","()V");
    static Signature s1 = new Signature("saveSuper","(I)V");
    static Signature s2 = new Signature("saveSuper","(J)V");

//    获取目标方法的编号
//     Proxy
//        save()     0
//        save(int)  1
//        save(long) 2
//    signature包括方法名字，参数返回值
    public int getIndex(Signature signature){
        if(s0.equals(signature)){
            return 0;
        }else if(s1.equals(signature)){
            return 1;
        }else if(s2.equals(signature)){
            return 2;
        }
        return -1;
    }

    // 根据方法编号，正常调用目标对象方法
    public Object invoke(int index, Object Proxy, Object[] args){
        if(index == 0){
            ((Proxy)Proxy).saveSuper();
            return null;
        } else if(index == 1) {
            ((Proxy)Proxy).saveSuper(((int) args[0]));
            return null;
        }else if(index == 2) {
            ((Proxy)Proxy).saveSuper(((long) args[0]));
            return null;
        } else {
            throw new RuntimeException("无此方法");
        }
    }

    public static void main(String[] args) {
        ProxyFastClass fastClass = new ProxyFastClass();
        int save = fastClass.getIndex(new Signature("saveSuper", "(J)V"));
        System.out.println(save);
        fastClass.invoke(save,new Proxy(),new Object[]{1L});
    }

}
