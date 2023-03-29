package org.JE.JE2ObjectParser.Tokenization;

public class ResolveToken {
    public int depth;
    public String type;
    public String value;
    public String path;

    public ResolveToken(String type, String value, String path){
        this.depth = path.split("\\.").length-1;
        this.type = type;
        this.value = value;
        this.path = path;
    }
}
