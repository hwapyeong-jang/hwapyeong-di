package com.macaront.app;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Stack;

public class ReflectionTests {

    @org.junit.jupiter.api.Test
    public void test() throws Exception {

        Class<Test> clazz = Test.class;
        Constructor<Test> testConstructor =  clazz.getDeclaredConstructor();
        testConstructor.setAccessible(true);

        Field[] fields = clazz.getDeclaredFields();
        for (Field field: fields) {
            System.out.println(field.getName());
            field.setAccessible(true);
        }

        Field nameField = clazz.getDeclaredField("testName");
        nameField.setAccessible(true);

        Method method = clazz.getDeclaredMethod("func");
        method.setAccessible(true);

        Method getNameMethod = clazz.getDeclaredMethod("getName");
        getNameMethod.setAccessible(true);

        getNameMethod.invoke(clazz);

        Test test = testConstructor.newInstance();
        method.invoke(clazz);

        nameField.set(test, "name is");
        method.invoke(test);



    }

}

class Test {

    private Test() {}

    private String testName;
    private static final String testName2 = "TestName2";

    private void func() {
        System.out.println(this.testName);
        System.out.println("func");
    }

    private static String getName() {
        return "asd";
    }

}
