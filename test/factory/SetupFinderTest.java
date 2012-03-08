package factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

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

@FactorySetup(value = ClassWithSetupDefined.class, alias = "AliasClass")
class ClassWithSetupDefinedSetupAlias {

    public int capacity() {
        return 50;
    }
}

public class SetupFinderTest {

    private SetupFinder setupFinder = new SetupFinder();

    @Test
    public void shouldFindSetupForClassWithSetupDefined() {
        setupFinder = new SetupFinder();
        assertEquals(ClassWithSetupDefinedSetup.class, setupFinder.setupClassFor(ClassWithSetupDefined.class));
    }

    @Test
    public void shouldReturnNullForClassWithoutSetupDefined() {
        assertEquals(null, setupFinder.setupClassFor(ClassWithoutSetupDefined.class));
    }

    @Test
    public void shouldFindAliasSetupForClassWithSetupDefined() {
        assertEquals(ClassWithSetupDefinedSetupAlias.class, setupFinder.setupClassFor(ClassWithSetupDefined.class, "AliasClass"));
    }
}
