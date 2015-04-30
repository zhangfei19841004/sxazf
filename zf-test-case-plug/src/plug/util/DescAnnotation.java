package plug.util;

import java.lang.reflect.Field;

public class DescAnnotation {
	
	public static <T> String getDescription(Class<T> klass, String str) {
        Field[] f = klass.getFields();
        for (Field field : f) {
            String v = null;
            try {
                v = field.get(klass).toString();
            } catch (IllegalArgumentException e) {             
                e.printStackTrace();
            } catch (IllegalAccessException e) {               
                e.printStackTrace();
            }          
            if (field.isAnnotationPresent(Description.class) && str.equals(v)) {
            	Description desc = field.getAnnotation(Description.class);
                String d = desc.description();
                return d;
            }
        }
        return null;
    }
}
