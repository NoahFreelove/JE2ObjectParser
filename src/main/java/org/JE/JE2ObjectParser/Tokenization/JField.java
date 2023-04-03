package org.JE.JE2ObjectParser.Tokenization;

import org.JE.JE2ObjectParser.Annotations.PersistentName;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class JField {
    public JObject parent;
    public Field field;
    public boolean isPrimitive = false;
    public boolean isArray = false;
    public JObject child;

    public JField(JObject parent, Field field){
        this.parent = parent;
        this.field = field;
        this.field.setAccessible(true);
        if(field.getType().isArray()){
            isArray = true;
        }

        if(field.getType().isPrimitive() || field.getType() == String.class){
            child = null;
            isPrimitive = true;
        }else{
            Field[] declaredFields = field.getType().getDeclaredFields();

            if(declaredFields.length== 0){
                child = null;
                isPrimitive = true;
                return;
            }
            Object fieldObject = null;
            try {
                if(parent.object !=null){
                    fieldObject = field.get(parent.object);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            child = new JObject(fieldObject, declaredFields, field, parent);
        }
        if(child == null)
            isPrimitive = true;
    }

    public JObject getParentOrChild(){
        if(child == null)
            return parent;
        return child;
        //return parent;
    }
    public ArrayList<JField> getChildFieldsRecursive(){
        ArrayList<JField> fields = new ArrayList<>();
        if(child == null)
            return fields;
        for(JField field : child.fields){
            {
                fields.add(field);
                fields.addAll(field.getChildFieldsRecursive());
            }
        }
        return fields;
    }

    public String getPath(){
        if(parent.fieldOf == null)
            return field.getName();
        String name = field.getName();
        // return annotation name if it has it, otherwise return field name
        if(field.isAnnotationPresent(PersistentName.class)){
            PersistentName annotation = field.getAnnotation(PersistentName.class);
            name = annotation.name();
        }
        return parent.getPathRecursive()  + name;
    }
}
