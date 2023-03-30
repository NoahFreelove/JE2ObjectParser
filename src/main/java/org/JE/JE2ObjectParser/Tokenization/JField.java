package org.JE.JE2ObjectParser.Tokenization;

import java.lang.reflect.Field;

public class JField {
    public JObject parent;
    public Field field;
    public boolean isPrimitive = false;
    public JObject child;

    public JField(JObject parent, Field field){
        this.parent = parent;
        this.field = field;

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
            child = new JObject(fieldObject, declaredFields, field);
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
}
