package factory;

import org.junit.Test;

class ClassWithArrayField {
    Object[] anArray;
}

@Setup(ClassWithArrayField.class)
class ClassWithArrayFieldSetup {
}

public class ArrayInstantiationTest {

    @Test
    public void shouldNotTryToInstantiateArrayField() {
        Instantiator.create(ClassWithArrayField.class);
    }
}
