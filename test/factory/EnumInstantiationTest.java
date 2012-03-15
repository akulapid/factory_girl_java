package factory;

import org.junit.Test;

class ClassWithEnumField {
    enum Something{
    };
    Something anEnum;
}

@Setup(ClassWithEnumField.class)
class ClassWithEnumFieldSetup {
}

public class EnumInstantiationTest {

    @Test
    public void shouldNotInstantiateEnumField() {
        Instantiator.create(ClassWithEnumField.class);
    }
}
