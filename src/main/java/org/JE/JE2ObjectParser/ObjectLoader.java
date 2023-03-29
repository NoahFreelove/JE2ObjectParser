package org.JE.JE2ObjectParser;

import org.JE.JE2ObjectParser.Tokenization.JField;
import org.JE.JE2ObjectParser.Tokenization.JObject;
import org.JE.JE2ObjectParser.Tokenization.ResolveToken;

import java.io.File;
import java.util.Scanner;

public class ObjectLoader<T> {


    public ObjectLoader() {

    }

    public T parseFromFile(T inputObject, String path) {
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine()).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println(e.getMessage());
        }
        return parseString(inputObject, sb.toString());
    }

    public T parseString(T inputObject, String inputData) {
        String[] lines = inputData.split("\n");
        JObject object = new JObject(inputObject, inputObject.getClass().getDeclaredFields());
        for (int i = 0; i < lines.length; i+=3) {
            String path = lines[i].replace("Field:","");
            String type = lines[i+1].replace("Type:","");
            String value = lines[i+2].replace("Value:","");
            fieldResolver(new ResolveToken(type,value,path), object);
        }
        return inputObject;
    }

    public void fieldResolver(ResolveToken token, JObject root){
        int depth = token.depth;
        String[] path = token.path.split("\\.");
        JObject current = root;
        for (int i = 0; i < depth; i++) {
            for (JField field : current.fields) {
                if(field.field.getName().equals(path[i])){
                    current = field.child;
                    break;
                }
            }
        }
        for (JField field : current.fields) {
            if(field.field.getName().equals(path[depth])){
                try {
                    field.field.set(current.object, getNativeValue(token.type, token.value));
                    //System.out.println("Set " + field.field.getName() + " to " + field.field.get(current.object));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private static Object getNativeValue(String type, String input){
        return switch (type) {
            case "int" -> Integer.parseInt(input);
            case "double" -> Double.parseDouble(input);
            case "float" -> Float.parseFloat(input);
            case "long" -> Long.parseLong(input);
            case "short" -> Short.parseShort(input);
            case "byte" -> Byte.parseByte(input);
            case "boolean" -> Boolean.parseBoolean(input);
            case "char" -> input.charAt(0);
            case "String" -> input;
            default -> null;
        };
    }
}
