package org.JE.JE2ObjectParser;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) {
        try {
            ObjectLoader<TestObject> objectLoader = new ObjectLoader<>();
            TestObject loaded = objectLoader.parseFromFile(new TestObject(),"Object.txt");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}