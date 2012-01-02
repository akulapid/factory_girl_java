package factory;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class InnerClassInstantiationTest {

    static class SampleClass {
    }

    @Test
    public void shouldInstantiateAnInnerClass() {
        assertTrue(Factory.create(SampleClass.class) instanceof SampleClass);
    }
}
