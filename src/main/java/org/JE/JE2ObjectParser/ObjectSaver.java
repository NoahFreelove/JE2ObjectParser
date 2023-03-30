package org.JE.JE2ObjectParser;

import org.JE.JE2ObjectParser.Tokenization.JField;
import org.JE.JE2ObjectParser.Tokenization.JObject;

import java.io.FileWriter;
import java.util.ArrayList;

public class ObjectSaver<T> {

    public ObjectSaver(){}

    public void saveToFile(T object, String path){
        String[] lines = saveObject(object);
        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter(path);
            for (String line :
                    lines) {
                fileWriter.write(line + "\n");
            }
            fileWriter.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

        public String[] saveObject (T inputObject){
        ArrayList<String> lines = new ArrayList<>();
        // Go through every public field in the object including the fields of the fields and so on until you reach a primitive
        // Save the field name and the value of the field

        JObject object = new JObject(inputObject, inputObject.getClass().getDeclaredFields(),null,null);
        ArrayList<JField> allFields = object.getChildFields();
        System.out.println("Before primitive filter: " + allFields.size());
        allFields.removeIf(field -> !field.isPrimitive);
        System.out.println("After primitive filter: " + allFields.size());

        allFields.forEach(jField -> {
            try {
                Object primValue = jField.field.get(jField.getParentOrChild().object).toString();
                // If value is a string, add quotes around it
                String value;
                if(jField.field.getType() == String.class){
                    value = "Value:\"" + primValue + "\"";
                }
                else{
                    value = "Value:" + primValue;
                }

                String field = "Field:" + jField.getPath();
                String type = "Type:" + jField.field.getType().getSimpleName().toLowerCase();
                System.out.println(field);
                lines.add(field);
                lines.add(type);
                lines.add(value);
            }catch (Exception err){
                System.out.println("err: " + err.getMessage());

            }
        });
        return lines.toArray(new String[0]);
    }
}
