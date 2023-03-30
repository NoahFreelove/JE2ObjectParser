package org.JE.JE2ObjectParser;

import org.JE.JE2ObjectParser.TestClasses.TestObject;

public class Main {
    public static void main(String[] args) {
        try {
            ObjectLoader<TestObject> objectLoader = new ObjectLoader<>();
            TestObject loaded = objectLoader.parseFromFile(new TestObject(),"Saved.txt");
            System.out.println(loaded.sub.str);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        /*try {
            ObjectSaver<TestObject> objectSaver = new ObjectSaver<>();
            objectSaver.saveToFile(new TestObject(), "Saved.txt");

        }catch (Exception e){
            System.out.println(e.getMessage());
        }*/
    }
}