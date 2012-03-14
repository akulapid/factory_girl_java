package factory;

import factory.annotations.Annotations;
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Instantiator {

    private static Annotations annotations = new Annotations();

    public static <T> T create(Class<T> clazz) {
        return create(clazz, null);
    }

    public static <T> T create(Class<T> clazz, String setupName) {
        try {
            Class setupClass = annotations.setupClassFor(clazz, setupName);
            if (setupClass == null)
                throw new SetupNotDefinedException(format("No FactorySetup found for %s", clazz.getName()));

            T object = instantiate(clazz, setupClass);
            instantiateFields(object, clazz);
            setup(object, setupClass);
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

    public static <T> T createProxy(Class<T> proxyClass, String setupName) {
        try {
            Constructor<FactoryPersistenceHandler> persistenceHandlerConstructor = annotations.persistentClass().getConstructor(String.class);
            FactoryPersistenceHandler persistentHandler = persistenceHandlerConstructor.newInstance("");

            Constructor<T> proxyConstructor = proxyClass.getConstructor(FactoryPersistenceHandler.class);
            T proxy = proxyConstructor.newInstance(persistentHandler);

            Class setupClass = annotations.setupClassFor(proxyClass.getSuperclass(), setupName);
            setup(proxy, setupClass);
            return proxy;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T instantiate(Class<T> clazz, Class setupClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Method factoryConstructorMethod = getFactoryConstructorMethod(setupClass);
        if (factoryConstructorMethod == null)
            return clazz.newInstance();
        return (T) factoryConstructorMethod.invoke(setupClass.newInstance());
    }

    private static Method getFactoryConstructorMethod(Class setupClazz) {
        try {
            return setupClazz.getDeclaredMethod("constructor");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static <T> void setup(T object, Class setupClass) throws FactorySetupException {
        try {
            Object setup = setupClass.newInstance();
            List<Method> applicableSetters = getApplicableSetters(setupClass);
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
            field.setAccessible(true);
            if (field.getType().equals(String.class))
                field.set(object, new String(""));
            else if (!field.getType().isPrimitive() && !field.getType().isArray() && !field.getType().isEnum() && !field.getType().isInterface())
                field.set(object, create(field.getType()));
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
