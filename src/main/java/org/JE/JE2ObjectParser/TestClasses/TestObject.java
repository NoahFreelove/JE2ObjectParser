package org.JE.JE2ObjectParser.TestClasses;

import org.JE.JE2ObjectParser.Annotations.ForceParserVisible;
import org.JE.JE2ObjectParser.Annotations.PersistentName;

public class TestObject {
    @PersistentName(name = "coolField!")
    public String stringField2 = "This is some text!";
    public SubObject sub = new SubObject();

    @ForceParserVisible
    private int num = 0;
    public TestObject(){
    }

    public int getNum() {
        return num;
    }
}
