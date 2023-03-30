package org.JE.JE2ObjectParser;

import org.JE.JE2ObjectParser.TestClasses.TestObject;

public class Main {
    public static void main(String[] args) {
        try {
            ObjectLoader<TestObject> objectLoader = new ObjectLoader<>();
            TestObject loaded = objectLoader.parseFromFile(new TestObject(),"Object.txt");
            System.out.println(loaded.stringField2);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}