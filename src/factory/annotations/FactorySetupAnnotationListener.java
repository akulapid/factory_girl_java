package factory.annotations;

import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;
import factory.DuplicateSetupException;
import factory.FactorySetup;

import java.util.Map;

import static java.lang.String.format;

public class FactorySetupAnnotationListener implements ClassAnnotationDiscoveryListener {

    private Map<String, Class> factorySetups;

    public FactorySetupAnnotationListener(Map<String, Class> factorySetups) {
        this.factorySetups = factorySetups;
    }

    public String[] supportedAnnotations() {
        return new String[] {
            FactorySetup.class.getName()
        };
    }

    public void discovered(String setupClass, String annotationName) {
        try {
            Class factorySetupClass = Class.forName(setupClass);
            FactorySetup factorySetup = (FactorySetup) factorySetupClass.getAnnotation(Class.forName(annotationName));
            String key = getKey(factorySetup);
            if (factorySetups.containsKey(key))
                throw new DuplicateSetupException(format("Duplicate setup names between (%s) and (%s)", factorySetupClass.getCanonicalName(), factorySetups.get(key).getCanonicalName()));
            factorySetups.put(key, factorySetupClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getKey(FactorySetup factorySetup) {
        String key = factorySetup.name();
        if (key.isEmpty())
            key = factorySetup.value().getSimpleName();
        return key;
    }
}
