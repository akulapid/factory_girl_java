package factory.annotations;

import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;
import factory.DuplicatePersistentException;
import factory.FactoryPersistenceHandler;

import java.util.concurrent.atomic.AtomicReference;

import static java.lang.String.format;

public class FactoryPersistentAnnotationListener implements ClassAnnotationDiscoveryListener {

    private AtomicReference<Class> factoryPersistent;

    public FactoryPersistentAnnotationListener(AtomicReference<Class> factoryPersistent) {
        this.factoryPersistent = factoryPersistent;
    }

    public String[] supportedAnnotations() {
        return new String[] {
            FactoryPersistenceHandler.class.getName()
        };
    }

    public void discovered(String persistentClass, String annotationName) {
        try {
            Class factoryPersistentClass = Class.forName(persistentClass);
            FactoryPersistenceHandler factoryPersistenceHandlerAnnotation = (FactoryPersistenceHandler) factoryPersistentClass.getAnnotation(Class.forName(annotationName));
            if (factoryPersistent.get() != null)
                throw new DuplicatePersistentException(format("Duplicate persistent listeners between (%s) and (%s). There can be only one defined.", factoryPersistentClass.getCanonicalName(), factoryPersistenceHandlerAnnotation.annotationType().getCanonicalName()));
            factoryPersistent.set(factoryPersistentClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
