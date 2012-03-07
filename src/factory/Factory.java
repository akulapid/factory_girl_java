package factory;

import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Factory {

    private static SetupFinder setupFinder = new SetupFinder();

    public static <T> T create(Class<T> clazz) {
        try {
            T object = instantiate(clazz);
            instantiateFields(object, clazz);
            setup(object);
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
        Class setupClazz = setupFinder.setupClassFor(clazz);
        if (setupClazz == null)
            throw new SetupNotDefinedException(format("No FactorySetup found for %s", clazz.getName()));
        Method factoryConstructorMethod = getFactoryConstructorMethod(setupClazz);
        if (factoryConstructorMethod == null)
            return clazz.newInstance();
        return (T) factoryConstructorMethod.invoke(setupClazz.newInstance());
    }

    private static Method getFactoryConstructorMethod(Class setupClazz) {
        try {
            return setupClazz.getDeclaredMethod("constructor");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static <T> void setup(T object) throws FactorySetupException {
        Class setupClazz = setupFinder.setupClassFor(object.getClass());
        if (setupClazz == null)
            return;
        try {
            Object setup = setupClazz.newInstance();
            List<Method> applicableSetters = getApplicableSetters(setupClazz);
            assertMethodsSignature(applicableSetters);
            for (Method method : applicableSetters) {
                Method targetMethod = object.getClass().getMethod(getTargetMethodNameFor(method.getName()), method.getReturnType());
                targetMethod.invoke(object, method.invoke(setup));
            }
        } catch (InstantiationException e) {
            throw new FactorySetupException("", e);
        } catch (InvalidSignatureException e) {
            throw new FactorySetupException("", e);
        } catch (NoSuchMethodException e) {
            throw new FactorySetupException("", e);
        } catch (InvocationTargetException e) {
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

    private static List<Method> getApplicableSetters(Class setupClazz) {
        List<Method> publicMethods = new ArrayList<Method>();
        for (Method method : setupClazz.getDeclaredMethods())
            if (Modifier.isPublic(method.getModifiers()) && !method.getName().equals("constructor"))
                publicMethods.add(method);
        return publicMethods;
    }

    private static String getTargetMethodNameFor(String setupSetter) {
        return "set" + WordUtils.capitalize(setupSetter);
    }

    private static <T> void instantiateThisFields(Class<T> clazz, T object) throws InstantiationException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(String.class))
                field.set(object, new String(""));
            else if (!field.getType().isPrimitive() && !field.getType().isArray() && !field.getType().isEnum() && !field.getType().isInterface()) {
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
