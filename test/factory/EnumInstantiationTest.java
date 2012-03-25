package factory;

import org.junit.Test;

class ClassWithEnumField {
    enum Something{
    };
    Something anEnum;
}

@Factory(ClassWithEnumField.class)
class ClassWithEnumFieldSetup {
}

public class EnumInstantiationTest {

    @Test
    public void shouldNotInstantiateEnumField() {
        Instantiator.create(ClassWithEnumField.class);
    }
}
