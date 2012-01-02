package factory;

import org.junit.Test;

import static junit.framework.Assert.fail;

class ClassWithStringField {
    String foo;
}

public class StringInstantiationTest {

    @Test
    public void shouldNotThrowExceptionWhenInstantiatingStringField() {
        try {
            Factory.create(ClassWithStringField.class);
        } catch (Exception e) {
            fail();
        }
    }
}
