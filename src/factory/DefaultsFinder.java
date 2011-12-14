package factory;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;

import java.util.HashMap;
import java.util.Map;

public class DefaultsFinder {

    private Map<Class, Class> factoryDefaults = new HashMap<Class, Class>();

    public DefaultsFinder() {
        Discoverer discoverer = new ClasspathDiscoverer();
        discoverer.addAnnotationListener(new FactoryDefaultAnnotationListener());
        discoverer.discover();
    }

    public Class defaultsClassFor(Class clazz) {
        return factoryDefaults.get(clazz);
    }

    public class FactoryDefaultAnnotationListener implements ClassAnnotationDiscoveryListener {

        public String[] supportedAnnotations() {
            return new String[]{FactoryDefault.class.getName()};
        }

        public void discovered(String clazz, String annotation) {
            try {
                Class factoryDefaultClass = Class.forName(clazz);
                FactoryDefault factoryDefault = (FactoryDefault) factoryDefaultClass.getAnnotation(FactoryDefault.class);
                factoryDefaults.put(factoryDefault.type(), factoryDefaultClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
