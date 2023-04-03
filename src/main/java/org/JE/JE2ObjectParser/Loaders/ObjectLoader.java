package org.JE.JE2ObjectParser.Loaders;

import org.JE.JE2ObjectParser.Annotations.PersistentName;
import org.JE.JE2ObjectParser.Tokenization.JField;
import org.JE.JE2ObjectParser.Tokenization.JObject;
import org.JE.JE2ObjectParser.Tokenization.ResolveToken;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ObjectLoader<T> {

    public boolean logFailures = false;

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
        JObject object = new JObject(inputObject, inputObject.getClass().getDeclaredFields(),null,null);
        for (int i = 0; i < lines.length; i+=3) {
            String path = lines[i].replace("Field:","");
            String type = lines[i+1].replace("Type:","");
            String value = lines[i+2].replace("Value:","");
            if(!fieldResolver(new ResolveToken(type,value,path), object) && logFailures){
                System.out.println("Unable to resolve field: " + path);
            }
        }
        return inputObject;
    }

    public T parseString(T inputObject, String[] data){
        StringBuilder sb = new StringBuilder();
        for (String s : data) {
            sb.append(s).append("\n");
        }
        return parseString(inputObject, sb.toString());
    }

    public boolean fieldResolver(ResolveToken token, JObject root){
        int depth = token.depth;
        String[] path = token.path.split("\\.");
        JObject current = resolve(depth, path, root);

        if(current == null)
        {
            return false;
        }

        return trySetField(token, path, current);
    }

    private boolean trySetField(ResolveToken token, String[] path, JObject current) {
        /*System.out.println("Trying to set field");
        System.out.println(Arrays.toString(path));
        System.out.println(depth);*/

        for (JField field : current.fields) {
            //System.out.println(field.field.getName());
            if(field.field.getName().equals(path[path.length-1])){
                try {
                    field.field.set(current.object, getNativeValue(token.type, token.value));
                    //System.out.println("Set " + field.field.getName() + " to " + field.field.get(current.object));
                    return true;
                } catch (IllegalAccessException e) {
                    if(logFailures)
                        e.printStackTrace();
                }
                break;
            }
        }
        return false;
    }


    private JObject resolve(int depth, String[] path, JObject current) {
        JObject parent = current;
        for (int i = 0; i < depth+1; i++) {
            boolean found = false;
            // We want to resolve by annotation before we resolve by name
            for (JField field : parent.fields) {
                // Check if any fields have the @PersistentName annotation and use that instead of the field name if it matches
                if(field.field.isAnnotationPresent(PersistentName.class)){
                    PersistentName annotation = field.field.getAnnotation(PersistentName.class);
                    if(annotation.name().equals(path[i])){
                        // set path to the name of the field
                        path[i] = field.field.getName();
                        parent = field.getParentOrChild();
                        found = true;
                        break;
                    }
                }
            }
            if(!found){
                for (JField field : parent.fields) {
                    if(field.field.getName().equals(path[i])){
                        parent = field.getParentOrChild();
                        found = true;
                        break;
                    }
                }
            }
            if(!found)
                return null;
        }
        //System.out.println("Resolved: " + Arrays.toString(path) + "!");
        return parent;
    }

    private Object getNativeValue(String type, String input){
        return switch (type) {
            case "int" -> Integer.parseInt(input);
            case "double" -> Double.parseDouble(input);
            case "float" -> Float.parseFloat(input);
            case "long" -> Long.parseLong(input);
            case "short" -> Short.parseShort(input);
            case "byte" -> Byte.parseByte(input);
            case "boolean" -> Boolean.parseBoolean(input);
            case "char" -> input.charAt(0);
            case "string" -> input;
            default -> null;
        };
    }

    private boolean isPrimitive(Object input){
        return input instanceof Integer || input instanceof Double || input instanceof Float || input instanceof Long || input instanceof Short || input instanceof Byte || input instanceof Boolean || input instanceof Character || input instanceof String;
    }
}
