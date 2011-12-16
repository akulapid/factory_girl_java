package factory;

import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Factory {

    private static DefaultsFinder defaultsFinder = new DefaultsFinder();

    public static <T> T create(Class<T> clazz) {
        try {
            T object = instantiate(clazz);
            instantiateFields(object, clazz);
            setDefaults(object);
            return object;
        } catch (InstantiationException e) {
            throw new FactoryInstantiationException(e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T instantiate(Class<T> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Class defaultsClazz = defaultsFinder.defaultsClassFor(clazz);
        if (defaultsClazz == null)
            return clazz.newInstance();
        Method factoryConstructorMethod = getFactoryConstructorMethod(defaultsClazz);
        if (factoryConstructorMethod == null)
            return clazz.newInstance();
        return (T) factoryConstructorMethod.invoke(defaultsClazz.newInstance());
    }

    private static Method getFactoryConstructorMethod(Class defaultsClazz) {
        try {
            return defaultsClazz.getDeclaredMethod("constructor");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static <T> void setDefaults(T object) throws FactoryDefaultsInstantiationException {
        Class defaultsClazz = defaultsFinder.defaultsClassFor(object.getClass());
        if (defaultsClazz == null)
            return;
        try {
            Object defaults = defaultsClazz.newInstance();
            List<Method> methods = Arrays.asList(defaultsClazz.getDeclaredMethods());
            List<Method> applicableSetters = getApplicableSetters(defaultsClazz);
            assertMethodsSignature(applicableSetters);
            for (Method method : applicableSetters) {
                Method targetMethod = object.getClass().getMethod(getTargetMethodNameFor(method.getName()), method.getReturnType());
                targetMethod.invoke(object, method.invoke(defaults));
            }
        } catch (InstantiationException e) {
            throw new FactoryDefaultsInstantiationException(e);
        } catch (InvalidSignatureException e) {
            throw new FactoryDefaultsInstantiationException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void assertMethodsSignature(List<Method> methods) throws InvalidSignatureException {
        for (Method method : methods) {
            if (method.getParameterTypes().length > 0)
                throw new InvalidSignatureException();
            if (method.getReturnType().equals(Void.TYPE))
                throw new InvalidSignatureException();
        }
    }

    private static List<Method> getApplicableSetters(Class defaultsClazz) {
        List<Method> publicMethods = new ArrayList<Method>();
        for (Method method : defaultsClazz.getDeclaredMethods())
            if (Modifier.isPublic(method.getModifiers()) && !method.getName().equals("constructor"))
                publicMethods.add(method);
        return publicMethods;
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
