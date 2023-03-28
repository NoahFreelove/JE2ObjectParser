package org.JE.JE2ObjectParser;

public class TestObject {
    @PersistentName(name = "stringField")
    public String stringField = "This is some text!";
    public SubObject sub = new SubObject();
    public TestObject(){
        try {
            System.out.println(stringField.getClass().getFields()[0].get(stringField).getClass().getSimpleName());

        }catch (Exception ignore){}
    }
}
