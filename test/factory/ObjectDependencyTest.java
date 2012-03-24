package factory;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class ObjectDependencyTest {
    
    @Test
    public void shouldAddRootObject() {
        Object root = new Object();
        ObjectDependency dependency = new ObjectDependency();
        dependency.add(root, null);
        assertEquals(root, dependency.getObject());
        assertEquals(0, dependency.getDependencies().size());
    }

    @Test
    public void shouldAddFirstLevelDependency() {
        ObjectDependency dependency = new ObjectDependency();
        Object root = new Object();
        dependency.add(root, null);

        Object firstLevel = new Object();
        dependency.add(firstLevel, null);
        List<ObjectDependency> dependencies = dependency.getDependencies();
        assertEquals(1, dependencies.size());
        assertEquals(firstLevel, dependencies.get(0).getObject());
    }

    @Test
    public void shouldRemoveAFirstLevelDependencyFromTheGraph() {
        ObjectDependency dependency = new ObjectDependency();
        Object root = new Object();
        dependency.add(root, null);

        Object firstLevel1 = new Object();
        dependency.add(firstLevel1, "foo");
        Object firstLevel2 = new Object();
        dependency.add(firstLevel2, "bar");

        dependency.remove("foo");
        assertEquals(1, dependency.getDependencies().size());
        assertEquals("bar", dependency.getDependencies().get(0).getFieldName());
    }
}
