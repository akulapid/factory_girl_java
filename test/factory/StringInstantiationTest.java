package factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

class ClassWithStringField {
    String foo;

    public String getFoo() {
        return foo;
    }

    public void setFoo(String foo) {
        this.foo = foo;
    }
}

@FactorySetup(type = ClassWithStringField.class)
class ClassWithStringFieldSetup {
    public String foo() {
        return "bar";
    }
}

public class StringInstantiationTest {

    @Test
    public void shouldNotThrowExceptionWhenInstantiatingStringField() {
        try {
            Instantiator.create(ClassWithStringField.class);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void shouldSetupStringField() {
        assertEquals("bar", Instantiator.create(ClassWithStringField.class).getFoo());
    }
}
