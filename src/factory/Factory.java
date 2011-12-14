package factory;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;
import com.sun.corba.se.pept.transport.InboundConnectionCache;
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Factory {

    private static DefaultsFinder defaultsFinder = new DefaultsFinder();

    public static <T> T create(Class<T> clazz) {
        try {
            T object = clazz.newInstance();
            instantiateFields(object, clazz);
            setDefaults(object);
            return object;
        } catch (InstantiationException e) {
            throw new FactoryInstantiationException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> void setDefaults(T object) throws FactoryDefaultsInstantiationException {
        Class defaultsClazz = defaultsFinder.defaultsClassFor(object.getClass());
        if (defaultsClazz == null)
            return;
        try {
            Object defaults = defaultsClazz.newInstance();
            Method[] methods = defaultsClazz.getDeclaredMethods();
            for (Method method : methods) {
                Method targetMethod = object.getClass().getMethod(getTargetMethodNameFor(method.getName()), method.getReturnType());
                targetMethod.invoke(object, method.invoke(defaults));
            }
        } catch (InstantiationException e) {
            throw new FactoryDefaultsInstantiationException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static String getTargetMethodNameFor(String defaultsMethodName) {
        return "set" + WordUtils.capitalize(defaultsMethodName);
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
