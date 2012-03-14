package factory.annotations;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.String.format;

public class Annotations {

    private Map<String, Class> factorySetups = new HashMap<String, Class>();
    private AtomicReference<Class> factoryPersistent = new AtomicReference<Class>();

    public Annotations() {
        Discoverer discoverer = new ClasspathDiscoverer();
        discoverer.addAnnotationListener(new FactorySetupAnnotationListener(factorySetups));
        discoverer.addAnnotationListener(new FactoryPersistentAnnotationListener(factoryPersistent));
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

    public Class persistentClass() {
        return factoryPersistent.get();
    }
}

