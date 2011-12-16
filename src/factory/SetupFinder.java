package factory;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;

import java.util.HashMap;
import java.util.Map;

public class SetupFinder {

    private Map<Class, Class> factorySetups = new HashMap<Class, Class>();

    public SetupFinder() {
        Discoverer discoverer = new ClasspathDiscoverer();
        discoverer.addAnnotationListener(new FactorySetupAnnotationListener());
        discoverer.discover();
    }

    public Class setupClassFor(Class clazz) {
        return factorySetups.get(clazz);
    }

    public class FactorySetupAnnotationListener implements ClassAnnotationDiscoveryListener {

        public String[] supportedAnnotations() {
            return new String[]{FactorySetup.class.getName()};
        }

        public void discovered(String clazz, String annotation) {
            try {
                Class factorySetupClass = Class.forName(clazz);
                FactorySetup factorySetup = (FactorySetup) factorySetupClass.getAnnotation(FactorySetup.class);
                factorySetups.put(factorySetup.type(), factorySetupClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
