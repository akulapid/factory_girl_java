package factory;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

class ClassWithPrimitiveField {
    int aPrimitiveField;
}

@FactorySetup(ClassWithPrimitiveField.class)
class ClassWithPrimitiveFieldSetup {
}

public class PrimitiveTypeInstantiationTest {

    @Test
    public void shouldNotTryToInstantiatePrimitiveField() {
        Instantiator.create(ClassWithPrimitiveField.class);
    }
}
