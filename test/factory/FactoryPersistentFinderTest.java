package factory;

import factory.annotations.Annotations;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

@FactoryPersistent
class DealPersistent {
}

public class FactoryPersistentFinderTest {

    private Annotations annotations = new Annotations();

    @Test
    public void shouldFindPersistentClass() {
        assertEquals(DealPersistent.class, annotations.persistentClass());
    }
}
