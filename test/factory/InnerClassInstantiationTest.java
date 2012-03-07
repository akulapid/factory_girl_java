package factory;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

@FactorySetup(type = InnerClassInstantiationTest.SampleClass.class)
class SampleClassSetup {
}

public class InnerClassInstantiationTest {

    static class SampleClass {
    }

    @Test
    public void shouldInstantiateAnInnerClass() {
        assertTrue(Instantiator.create(SampleClass.class) instanceof SampleClass);
    }
}
