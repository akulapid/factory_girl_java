package factory;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class SetupFinder {

    private Map<String, Class> factorySetups = new HashMap<String, Class>();

    public SetupFinder() {
        Discoverer discoverer = new ClasspathDiscoverer();
        discoverer.addAnnotationListener(new FactorySetupAnnotationListener());
        discoverer.discover();
    }

    public Class setupClassFor(Class clazz) {
        return setupClassFor(clazz, null);
    }

    public Class setupClassFor(Class clazz, String setupName) {
        if (setupName == null || setupName.isEmpty())
            setupName = clazz.getSimpleName();
        return factorySetups.get(setupName);
    }

    public class FactorySetupAnnotationListener implements ClassAnnotationDiscoveryListener {

        public String[] supportedAnnotations() {
            return new String[] {
                FactorySetup.class.getName()
            };
        }

        public void discovered(String clazz, String annotationName) {
            try {
                Class factorySetupClass = Class.forName(clazz);
                Annotation annotation = factorySetupClass.getAnnotation(Class.forName(annotationName));
                if (annotation instanceof FactorySetup) {
                    FactorySetup factorySetup = (FactorySetup) annotation;
                    String key = factorySetup.name();
                    if (key.isEmpty())
                        key = factorySetup.value().getSimpleName();
                    factorySetups.put(key, factorySetupClass);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
