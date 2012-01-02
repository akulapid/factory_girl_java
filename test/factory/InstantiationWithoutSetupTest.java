package factory;

import org.junit.Test;

import static junit.framework.Assert.fail;


class ClassWithoutSetupDefined {
}

public class InstantiationWithoutSetupTest {

    @Test
    public void shouldNotThrowExceptionIfSetupIsNotDefined() {
        try {
            Factory.create(ClassWithoutSetupDefined.class);
        } catch (NullPointerException e) {
            fail("should not try to instantiate undefined setup class");
        }
    }
}
