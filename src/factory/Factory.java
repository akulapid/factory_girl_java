package factory;

import java.lang.reflect.Field;

public class Factory {

    public static <T> T create(Class<T> clazz) throws InstantiationException {
        try {
            T object = clazz.newInstance();
            initializeFields(object, clazz);
            return object;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> void initializeThisFields(Class<T> clazz, T object) throws InstantiationException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.getType().isPrimitive() && !field.getType().isArray() && !field.getType().isEnum()) {
                field.setAccessible(true);
                field.set(object, create(field.getType()));
            }
        }
    }

    private static <T> void initializeSuperFields(Class<T> clazz, T object) throws InstantiationException, IllegalAccessException {
        if (clazz.getSuperclass() != null) {
            Class<? super T> superClazz = clazz.getSuperclass();
            initializeFields(object, superClazz);
        }
    }

    private static <T> void initializeFields(T object, Class<? super T> clazz) throws InstantiationException, IllegalAccessException {
        initializeSuperFields(clazz, object);
        initializeThisFields(clazz, object);
    }
}
