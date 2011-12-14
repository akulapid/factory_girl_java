package factory;

import data.ClassWithDefaultsDefined;
import data.ClassWithoutDefaultsDefined;
import data.DefaultsForAClass;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class DefaultsFinderTest {

    @Test
    public void shouldFindDefaultsForClassWithDefaultsDefined() {
        assertEquals(DefaultsForAClass.class, new DefaultsFinder().defaultsClassFor(ClassWithDefaultsDefined.class));
    }

    @Test
    public void shouldReturnNullForClassWithoutDefaultsDefined() {
        assertEquals(null, new DefaultsFinder().defaultsClassFor(ClassWithoutDefaultsDefined.class));
    }
}
