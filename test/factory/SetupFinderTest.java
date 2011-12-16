package factory;

import data.ClassWithSetupDefined;
import data.ClassWithoutSetupDefined;
import data.ClassWithSetupDefinedSetup;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SetupFinderTest {

    @Test
    public void shouldFindSetupForClassWithSetupDefined() {
        assertEquals(ClassWithSetupDefinedSetup.class, new SetupFinder().setupClassFor(ClassWithSetupDefined.class));
    }

    @Test
    public void shouldReturnNullForClassWithoutSetupDefined() {
        assertEquals(null, new SetupFinder().setupClassFor(ClassWithoutSetupDefined.class));
    }
}
