package factory;

import org.junit.Test;

class ClassWithArrayField {
    Object[] anArray;
}

public class ArrayInstantiationTest {

    @Test
    public void shouldNotTryToInstantiateArrayField() {
        Factory.create(ClassWithArrayField.class);
    }
}
