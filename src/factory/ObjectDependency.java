package factory;

import java.util.ArrayList;
import java.util.List;

public class ObjectDependency {

    private Object object;
    private String fieldName;
    private List<ObjectDependency> dependencies = new ArrayList<ObjectDependency>();

    public ObjectDependency() {
    }

    public ObjectDependency(Object object, String fieldName) {
        this.object = object;
        this.fieldName = fieldName;
    }

    public ObjectDependency add(Object object, String fieldName) {
        ObjectDependency dependency = new ObjectDependency(object, fieldName);
        dependencies.add(dependency);
        return dependency;
    }

    public void remove(String fieldName) {
        for (ObjectDependency dependency : dependencies)
            if (fieldName.equals(dependency.fieldName)) {
                dependencies.remove(dependency);
                break;
            }
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<ObjectDependency> getDependencies() {
        return dependencies;
    }
}
