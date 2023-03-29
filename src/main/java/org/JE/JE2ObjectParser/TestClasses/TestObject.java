package org.JE.JE2ObjectParser.TestClasses;

import org.JE.JE2ObjectParser.Annotations.PersistentName;

public class TestObject {
    @PersistentName(name = "stringField")
    public String stringField = "This is some text!";
    public SubObject sub = new SubObject();
    public int num = 0;
    public TestObject(){
    }
}
