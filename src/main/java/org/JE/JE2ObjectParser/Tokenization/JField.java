package org.JE.JE2ObjectParser.Tokenization;

import java.lang.reflect.Field;

public class JField {
    public JObject parent;
    public Field field;
    public JObject child;

    public JField(JObject parent, Field field){
        this.parent = parent;
        this.field = field;

        System.out.println(field.getName());
        if(field.getType().isPrimitive()){
            child = null;
        }else{
            Field[] declaredFields = field.getType().getDeclaredFields();

            if(declaredFields.length== 0){
                child = null;
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
            child = new JObject(fieldObject, declaredFields);
        }
    }
}
