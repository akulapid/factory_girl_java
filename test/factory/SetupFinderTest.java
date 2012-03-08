package factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

class ClassWithSetupDefined {

    int capacity;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

@FactorySetup(ClassWithSetupDefined.class)
class ClassWithSetupDefinedSetup {

    public int capacity() {
        return 1000;
    }
}

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
