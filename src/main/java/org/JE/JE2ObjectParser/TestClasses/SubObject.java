package org.JE.JE2ObjectParser.TestClasses;

import org.JE.JE2ObjectParser.Annotations.PersistentName;

public class SubObject {

    @PersistentName(name = "str")
    public String betterField = "betterField";

    @PersistentName(name = "betterField")
    public String str = "str";


    public SubSubClass subSub = new SubSubClass();
}
