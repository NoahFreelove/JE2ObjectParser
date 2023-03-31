package org.JE.JE2ObjectParser.TestClasses;

import org.JE.JE2ObjectParser.Annotations.PersistentName;

public class SubObject {

    @PersistentName(name = "strr")
    public String betterField = "betterField";

    @PersistentName(name = "betterField")
    public String str = null;


    public SubSubClass subSub = new SubSubClass();
}
