package factory;

import org.junit.Test;

class ClassWithArrayField {
    Object[] anArray;
}

@FactorySetup(type = ClassWithArrayField.class)
class ClassWithArrayFieldSetup {
}

public class ArrayInstantiationTest {

    @Test
    public void shouldNotTryToInstantiateArrayField() {
        Factory.create(ClassWithArrayField.class);
    }
}
