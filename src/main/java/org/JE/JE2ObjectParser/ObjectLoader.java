package org.JE.JE2ObjectParser;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ObjectLoader<T> {

    private int level;
    private Class<?> currentClass;
    private ArrayList<Object> hierarchy = new ArrayList<>();
    private Object activeObject;
    private Field activeField;

    public ObjectLoader() {

    }

    public T parseFromFile(T inputObject, String path) throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine()).append("\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return parseString(inputObject, sb.toString());
    }

    public T parseString(T inputObject, String inputData) throws Exception {
        String[] lines = inputData.split("\n");
        hierarchy.add(inputObject);

        System.out.println(Arrays.toString(lines));

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.startsWith("Field:")) {
                String fieldName = lines[i].replace("Field:", "");
                activeField = hierarchy.get(level).getClass().getField(fieldName);
                if (lines[i + 1].startsWith("Level:")) {
                    int newLevel = Integer.parseInt(lines[i + 1].replace("Level:", ""));
                    if (newLevel < level) {
                        // resize arraylist remove all objects after level
                        hierarchy.subList(newLevel, hierarchy.size()).clear();
                    } else {
                        hierarchy.add(activeField.get(hierarchy.get(level)));
                    }

                    Class<?> newClass = Class.forName(lines[i + 2].replace("Class:", ""));


                    activeObject = hierarchy.get(level).getClass().getField(objectField).get(hierarchy.get(level));
                    if (hierarchy.size() == level + 1) {
                        hierarchy.trimToSize();
                        hierarchy.add(activeObject);
                    } else {
                        hierarchy.set(level, objectField);
                    }
                    System.out.println(newLevel);
                    System.out.println(newClass.getSimpleName());
                    //System.out.println(objectField);
                    System.out.println(activeObject);
                    i += 2;
                }
            } else if (line.startsWith("Value:")) {
                boolean prim = isPrim(currentClass);
                if(prim){
                    activeField.set(hierarchy.get(level),);
                }
            }
        }
        return inputObject;
    }

    private static <E extends Object> E getNativeValue(Class<E> clazz, String input){
        if(E instanceof Integer){

        }
    }
    private static boolean isPrim(Class<?> input)
    {
        // return true if string, int, long, double, float, bool, char, byte
        if(input.isPrimitive())
            return true;
        if(input == Integer.class)
            return true;
        if(input == String.class)
            return true;
        if(input == Long.class)
            return true;
        if(input == Double.class)
            return true;
        if(input == Float.class)
            return true;
        if(input == Boolean.class)
            return true;
        if(input == Character.class)
            return true;
        if(input == Byte.class)
            return true;
        return false;
    }
}
