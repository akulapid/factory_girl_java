package factory;

import factory.annotations.Annotations;
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class Instantiator {

    private static Annotations annotations = new Annotations();

    public static <T> T createProxy(Class<T> proxyClass, String setupName) {
        try {
            Class setupClass = annotations.setupClassFor(proxyClass.getSuperclass(), setupName);
            Persistent persistent = (Persistent) setupClass.getAnnotation(Persistent.class);
            String databaseName = persistent != null? persistent.databaseName() : null;

            Class persistenceHandlerClass = annotations.persistentClass();
            AbstractPersistenceHandler persistenceHandler = null;
            if (persistenceHandlerClass != null) {
                Constructor<AbstractPersistenceHandler> persistenceHandlerConstructor = persistenceHandlerClass.getConstructor(String.class);
                persistenceHandler = persistenceHandlerConstructor.newInstance(databaseName);
            }

            ObjectDependency dependency = new ObjectDependency();
            Constructor<T> proxyConstructor = proxyClass.getConstructor(PersistenceHandlerProxy.class, ObjectDependency.class);
            T proxy = proxyConstructor.newInstance(new PersistenceHandlerProxy(persistenceHandler), dependency);

            instantiateAndSetupFields(proxy, proxyClass.getSuperclass(), setupClass, dependency);
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

    public static <T> T create(Class<T> clazz, ObjectDependency dependency, String setupName) {
        try {
            Class setupClass = annotations.setupClassFor(clazz, setupName);
            if (setupClass == null)
                throw new SetupNotDefinedException(format("No FactorySetup found for %s", clazz.getName()));

            T object = instantiate(clazz, setupClass);

            if (dependency == null)
                dependency = new ObjectDependency(object);
            else
                dependency = dependency.add(object);

            instantiateAndSetupFields(object, clazz, setupClass, dependency);
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

    public static <T> T create(Class<T> clazz, ObjectDependency dependency) {
        return create(clazz, dependency, null);
    }

    public static <T> T create(Class<T> clazz, String setupName) {
        return create(clazz, null, setupName);
    }

    public static <T> T create(Class<T> clazz) {
        return create(clazz, null, null);
    }

    private static <T> T instantiate(Class<T> clazz, Class setupClass) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Method factoryConstructorMethod = getFactoryConstructorMethod(setupClass);
        if (factoryConstructorMethod == null)
            return clazz.newInstance();
        return (T) factoryConstructorMethod.invoke(setupClass.newInstance());
    }

    private static <T> void instantiateAndSetupFields(T object, Class<? super T> clazz, Class setupClass, ObjectDependency dependency) throws InstantiationException, IllegalAccessException {
        instantiateSuperFields(clazz, object, setupClass, dependency);
        instantiateThisFields(clazz, object, dependency);
        setup(object, setupClass, dependency);
    }

    private static <T> void instantiateThisFields(Class<T> clazz, T object, ObjectDependency dependency) throws InstantiationException, IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getType().equals(String.class))
                field.set(object, new String(""));
            else if (!field.getType().isPrimitive() && !field.getType().isArray() && !field.getType().isEnum() && !field.getType().isInterface())
                field.set(object, create(field.getType(), dependency));
        }
    }

    private static <T> void instantiateSuperFields(Class<T> clazz, T object, Class setupClass, ObjectDependency dependency) throws InstantiationException, IllegalAccessException {
        if (clazz.getSuperclass() != null) {
            Class<? super T> superClazz = clazz.getSuperclass();
            if (superClazz != Object.class)
                instantiateAndSetupFields(object, superClazz, annotations.setupClassFor(superClazz), dependency);
        }
    }

    private static Method getFactoryConstructorMethod(Class setupClazz) {
        try {
            return setupClazz.getDeclaredMethod("constructor");
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static <T> void setup(T object, Class setupClass, ObjectDependency dependency) throws FactorySetupException {
        try {
            Object setup = setupClass.newInstance();
            List<Method> applicableSetters = getApplicableSetters(setupClass);
            assertMethodsSignature(applicableSetters);
            for (Method method : applicableSetters) {
                Method targetMethod = object.getClass().getMethod(getTargetMethodNameFor(method.getName()), method.getReturnType());
                targetMethod.invoke(object, method.invoke(setup));
            }
            List<Method> applicableAssociateMethods = getApplicableAssociations(setupClass);
            assertMethodsSignature(applicableAssociateMethods);
            for (Method method : applicableAssociateMethods) {
                Object association = create(method.getParameterTypes()[0], dependency);
                Method targetMethod = object.getClass().getMethod(getTargetMethodNameFor(method.getName()), method.getReturnType());
                targetMethod.invoke(object, method.invoke(setup, association));
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
            if (method.getParameterTypes().length > 1)
                throw new InvalidSignatureException();
            if (method.getReturnType().equals(Void.TYPE))
                throw new InvalidSignatureException();
        }
    }

    private static List<Method> getApplicableSetters(Class setupClazz) {
        List<Method> publicMethods = new ArrayList<Method>();
        for (Method method : setupClazz.getDeclaredMethods())
            if (isFactorySetter(method))
                publicMethods.add(method);
        return publicMethods;
    }

    private static List<Method> getApplicableAssociations(Class setupClazz) {
        List<Method> publicMethods = new ArrayList<Method>();
        for (Method method : setupClazz.getDeclaredMethods())
            if (isFactoryAssociation(method))
                publicMethods.add(method);
        return publicMethods;
    }

    private static boolean isFactorySetter(Method method) {
        return Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0 && !method.getName().equals("constructor");
    }

    private static boolean isFactoryAssociation(Method method) {
        return Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 1 && !method.getName().equals("constructor");
    }

    private static String getTargetMethodNameFor(String setupSetter) {
        return "set" + WordUtils.capitalize(setupSetter);
    }
}
