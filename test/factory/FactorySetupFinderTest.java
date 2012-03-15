package factory;

import factory.annotations.Annotations;
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

@Setup(ClassWithSetupDefined.class)
class ClassWithSetupDefinedSetup {

    public int capacity() {
        return 1000;
    }
}

@Setup(value = ClassWithSetupDefined.class, name = "AliasClass")
class ClassWithSetupDefinedSetupAlias {

    public int capacity() {
        return 50;
    }
}

public class FactorySetupFinderTest {

    private Annotations annotations = new Annotations();

    @Test
    public void shouldFindSetupForClassWithSetupDefined() {
        assertEquals(ClassWithSetupDefinedSetup.class, annotations.setupClassFor(ClassWithSetupDefined.class));
    }

    @Test
    public void shouldReturnNullForClassWithoutSetupDefined() {
        assertEquals(null, annotations.setupClassFor(ClassWithoutSetupDefined.class));
    }

    @Test
    public void shouldFindAliasSetupForClassWithMultipleSetupsDefined() {
        assertEquals(ClassWithSetupDefinedSetupAlias.class, annotations.setupClassFor(ClassWithSetupDefined.class, "AliasClass"));
    }
}
