package com.example.spring.demo12;



import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestMethodInvoke {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        Method foo = TestMethodInvoke.class.getDeclaredMethod("foo", int.class);
        for (int i = 0; i < 17; i++) {
            // 在第17次的时候会生成实现类，进行类的调用，而不是反射，提高效率。
            show(i,foo);
            foo.invoke(null,i);
        }
        System.in.read();

    }

    public static void show(int i, Method foo){
        // 这块应该输出底层的MethodAccessor, 但是不知道应该怎么输出
        System.out.println(i+":" + foo);

    }
    public static void foo(int i){
        System.out.println(i + ":" + "foo");
    }
}
