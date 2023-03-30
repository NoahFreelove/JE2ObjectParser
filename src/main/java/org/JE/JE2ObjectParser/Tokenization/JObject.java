package org.JE.JE2ObjectParser.Tokenization;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class JObject {
    public JField[] fields;
    public Object object;
    public Field fieldOf;
    public JObject parent;

    public JObject(Object object, Field[] fields, Field fieldOf, JObject parent){
        this.parent = parent;
        this.object = object;
        this.fieldOf = fieldOf;
        if(object == null){
            this.fields = new JField[0];
            return;
        }
        int count = 0;
        for(Field field : fields){
            if(!Modifier.isPrivate(field.getModifiers())
                    && !Modifier.isFinal(field.getModifiers())
                    && !Modifier.isStatic(field.getModifiers())
                    && !Modifier.isTransient(field.getModifiers())){
                field.setAccessible(true);
                count++;
            }
        }
        this.fields = new JField[count];

        for(int i = 0; i < count; i++){
            try {
                this.fields[i] = new JField(this, fields[i]);
            }
            catch (Exception e){
                System.out.println("err: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    public ArrayList<JField> getChildFields(){
        ArrayList<JField> fields = new ArrayList<>();
        for(JField field : this.fields){
            fields.add(field);
            fields.addAll(field.getChildFieldsRecursive());
        }
        return fields;
    }

    public String getPathRecursive() {
        // get the parent path if parent is not null
        String path = "";
        if (parent != null) {
            path = parent.getPathRecursive();
        }
        // if this object is a field of another object, add the field name to the path
        if (fieldOf != null) {
            path += fieldOf.getName() + ".";
        }
        return path;
    }
}
