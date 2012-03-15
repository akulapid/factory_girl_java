package factory.annotations;

import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;
import factory.DuplicateSetupException;
import factory.Setup;

import java.util.Map;

import static java.lang.String.format;

public class FactorySetupAnnotationListener implements ClassAnnotationDiscoveryListener {

    private Map<String, Class> factorySetups;

    public FactorySetupAnnotationListener(Map<String, Class> factorySetups) {
        this.factorySetups = factorySetups;
    }

    public String[] supportedAnnotations() {
        return new String[] {
            Setup.class.getName()
        };
    }

    public void discovered(String setupClass, String annotationName) {
        try {
            Class factorySetupClass = Class.forName(setupClass);
            Setup setup = (Setup) factorySetupClass.getAnnotation(Class.forName(annotationName));
            String key = getKey(setup);
            if (factorySetups.containsKey(key))
                throw new DuplicateSetupException(format("Duplicate setup names between (%s) and (%s)", factorySetupClass.getCanonicalName(), factorySetups.get(key).getCanonicalName()));
            factorySetups.put(key, factorySetupClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getKey(Setup setup) {
        String key = setup.name();
        if (key.isEmpty())
            key = setup.value().getSimpleName();
        return key;
    }
}
