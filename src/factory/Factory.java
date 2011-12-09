package factory;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Factory {

    private static Map<Class, Class> factoryDefaults = new HashMap<Class, Class>();

    static {
        findFactoryDefaultClasses();
    }

    public static class FactoryDefaultAnnotationListener implements ClassAnnotationDiscoveryListener {

        public String[] supportedAnnotations() {
            return new String[]{FactoryDefault.class.getName()};
        }

        public void discovered(String clazz, String annotation) {
            try {
                Class factoryDefaultType = Class.forName(clazz);
                FactoryDefault factoryDefault = (FactoryDefault)factoryDefaultType.getAnnotation(FactoryDefault.class);
                factoryDefaults.put(factoryDefault.type(), factoryDefaultType);
            } catch (Exception e) {
            }
        }
    }

    private static void findFactoryDefaultClasses() {
        Discoverer discoverer = new ClasspathDiscoverer();
        discoverer.addAnnotationListener(new FactoryDefaultAnnotationListener());
        discoverer.discover();
    }

    public static <T> T create(Class<T> clazz) throws InstantiationException {
        try {
            T object = clazz.newInstance();
            instantiateFields(object, clazz);
            setDefaults(object);
            return object;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> void setDefaults(T object) {
    }

    private static <T> void instantiateThisFields(Class<T> clazz, T object) throws InstantiationException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.getType().isPrimitive() && !field.getType().isArray() && !field.getType().isEnum()) {
                field.setAccessible(true);
                field.set(object, create(field.getType()));
            }
        }
    }

    private static <T> void instantiateSuperFields(Class<T> clazz, T object) throws InstantiationException, IllegalAccessException {
        if (clazz.getSuperclass() != null) {
            Class<? super T> superClazz = clazz.getSuperclass();
            instantiateFields(object, superClazz);
        }
    }

    private static <T> void instantiateFields(T object, Class<? super T> clazz) throws InstantiationException, IllegalAccessException {
        instantiateSuperFields(clazz, object);
        instantiateThisFields(clazz, object);
    }
}
