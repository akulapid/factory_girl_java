package factory;

import org.junit.Test;

class Stub {
}

class ClassWithInvalidSetupDefinition {
}

@FactorySetup(type = Stub.class)
class StubSetup {

    public int foo(int bar) {
        return 0;
    }

    public void bar() {
    }
}

@FactorySetup(type = ClassWithInvalidSetupDefinition.class)
class ClassWithInvalidSetupDefinitionSetup {

    public int undefinedMethod() {
        return 0;
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

    @Test(expected = FactorySetupException.class)
    public void shouldThrowExceptionIfSetupMethodIsNotFound() {
        Factory.create(ClassWithInvalidSetupDefinition.class);
    }
}
