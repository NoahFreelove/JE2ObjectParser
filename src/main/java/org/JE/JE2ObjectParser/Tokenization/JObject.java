package org.JE.JE2ObjectParser.Tokenization;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class JObject {
    public JField[] fields;
    public Object object;

    public JObject(Object object, Field[] fields){
        this.object = object;
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
}
