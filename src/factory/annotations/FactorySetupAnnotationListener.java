package factory.annotations;

import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;
import factory.DuplicateSetupException;
import factory.Factory;

import java.util.Map;

import static java.lang.String.format;

public class FactorySetupAnnotationListener implements ClassAnnotationDiscoveryListener {

    private Map<String, Class> factorySetups;

    public FactorySetupAnnotationListener(Map<String, Class> factorySetups) {
        this.factorySetups = factorySetups;
    }

    public String[] supportedAnnotations() {
        return new String[] {
            Factory.class.getName()
        };
    }

    public void discovered(String setupClass, String annotationName) {
        try {
            Class factorySetupClass = Class.forName(setupClass);
            Factory factory = (Factory) factorySetupClass.getAnnotation(Class.forName(annotationName));
            String key = getKey(factory);
            if (factorySetups.containsKey(key))
                throw new DuplicateSetupException(format("Duplicate factory names between (%s) and (%s)", factorySetupClass.getCanonicalName(), factorySetups.get(key).getCanonicalName()));
            factorySetups.put(key, factorySetupClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getKey(Factory factory) {
        String key = factory.name();
        if (key.isEmpty())
            key = factory.value().getSimpleName();
        return key;
    }
}
