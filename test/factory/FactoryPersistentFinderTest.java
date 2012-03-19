package factory;

import factory.annotations.Annotations;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

@PersistenceHandler
class DealPersistent extends AbstractPersistenceHandler {

    public DealPersistent(String resourceName) {
        super(resourceName);
    }

    public void persist(Object object) {
    }
}

public class FactoryPersistentFinderTest {

    private Annotations annotations = new Annotations();

    @Test
    public void shouldFindPersistentClass() {
        assertEquals(DealPersistent.class, annotations.persistentClass());
    }
}
