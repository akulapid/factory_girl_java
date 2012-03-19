package factory;

import java.util.ArrayList;
import java.util.List;

public class ObjectDependency {

    Object object;
    List<ObjectDependency> dependencies = new ArrayList<ObjectDependency>();

    public ObjectDependency() {
    }

    public ObjectDependency(Object object) {
        this.object = object;
    }

    public ObjectDependency add(Object object) {
        ObjectDependency dependency = new ObjectDependency(object);
        dependencies.add(dependency);
        return dependency;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public List<ObjectDependency> getDependencies() {
        return dependencies;
    }
}
