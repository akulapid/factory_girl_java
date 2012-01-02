package factory;

import org.junit.Test;

class ClassWithEnumField {
    enum Something{
    };
    Something anEnum;
}

public class EnumInstantiationTest {

    @Test
    public void shouldNotInstantiateEnumField() {
        Factory.create(ClassWithEnumField.class);
    }
}
