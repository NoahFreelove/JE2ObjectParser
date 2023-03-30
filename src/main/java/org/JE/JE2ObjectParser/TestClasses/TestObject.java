package org.JE.JE2ObjectParser.TestClasses;

import org.JE.JE2ObjectParser.Annotations.PersistentName;

public class TestObject {
    @PersistentName(name = "coolField!")
    public String stringField2 = "This is some text!";
    public SubObject sub = new SubObject();
    public int num = 0;
    public TestObject(){
    }
}
