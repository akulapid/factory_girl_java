package factory;

import data.*;
import org.junit.Test;

class Stub {
}

@FactorySetup(type = Stub.class)
class StubSetup {

    public int foo(int bar) {
        return 0;
    }

    public void bar() {
    }
}

public class SetupDefinitionTest {

    @Test(expected = FactorySetupException.class)
    public void shouldThrowExceptionIfSetupClassHasAPublicMethodThatTakesAnArgument() {
        Factory.create(Stub.class);
    }

    @Test(expected = FactorySetupException.class)
    public void shouldThrowExceptionIfSetupClassHasAPublicMethodThatReturnsVoid() {
        Factory.create(Stub.class);
    }
}
