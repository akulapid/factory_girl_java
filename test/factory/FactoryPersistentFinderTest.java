package factory;

import factory.annotations.Annotations;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

@FactoryPersistenceHandler
class DealPersistent extends AbstractPersistenceHandler {

    public DealPersistent(String resourceName) {
        super(resourceName);
    }

    public void built(Object object) {
    }
}

public class FactoryPersistentFinderTest {

    private Annotations annotations = new Annotations();

    @Test
    public void shouldFindPersistentClass() {
        assertEquals(DealPersistent.class, annotations.persistentClass());
    }
}
