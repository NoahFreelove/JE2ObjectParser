package org.JE.JE2ObjectParser.Tokenization;

import org.JE.JE2.Annotations.ActPublic;
import org.JE.JE2ObjectParser.Annotations.ForceParserVisible;

import java.lang.annotation.Annotation;
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
        ArrayList<JField> jFields = new ArrayList<>();
        for(Field field : fields){
            if(!Modifier.isStatic(field.getModifiers())
            && !Modifier.isTransient(field.getModifiers())){
                if(Modifier.isPrivate(field.getModifiers()) ||
                        Modifier.isProtected(field.getModifiers()) ||
                        Modifier.isFinal(field.getModifiers())){
                    Annotation[] annotations = field.getAnnotations();
                    for (Annotation ann : annotations) {
                        if (ann instanceof ForceParserVisible || ann instanceof ActPublic){
                            field.setAccessible(true);
                            try {
                                jFields.add(new JField(this,field));
                            }
                            catch (Exception e){
                                System.out.println("err: " + e.getMessage());
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
                else {
                    try {
                        jFields.add(new JField(this,field));
                    }
                    catch (Exception e){
                        System.out.println("err: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

            }
        }
        this.fields = jFields.toArray(new JField[0]);
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
