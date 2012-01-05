package factory;

import org.junit.Test;


class ClassWithoutSetupDefined {
}

public class InstantiationWithoutSetupTest {

    @Test(expected = SetupNotDefinedException.class)
    public void shouldThrowExceptionIfSetupIsNotDefined() {
        Factory.create(ClassWithoutSetupDefined.class);
    }
}
