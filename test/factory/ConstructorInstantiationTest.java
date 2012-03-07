package factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

class ClassWithoutNullaryConstructor {

    private int foo;

    public ClassWithoutNullaryConstructor(int foo) {
        this.foo = foo;
    }

    public int getFoo() {
        return foo;
    }
}

@FactorySetup(type = ClassWithoutNullaryConstructor.class)
class ClassWithoutNullaryConstructorSetup {

    public ClassWithoutNullaryConstructor constructor() {
        return new ClassWithoutNullaryConstructor(4);
    }
}

public class ConstructorInstantiationTest {

    @Test
    public void shouldInstantiateThroughConstructor() {
        assertEquals(4, Instantiator.create(ClassWithoutNullaryConstructor.class).getFoo());
    }

    @Test
    public void shouldNotUseFactoryConstructorForSetup() {
        try {
            Instantiator.create(ClassWithoutNullaryConstructor.class);
        } catch (Exception e) {
            fail("should not use factory constructor for setup");
        }
    }
}
