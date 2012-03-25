package akula.factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

class Stub {
}

class ClassWithInvalidSetupDefinition {
}

@Factory(Stub.class)
class StubSetup {

    public int foo(int bar) {
        return 0;
    }

    public void bar() {
    }
}

@Factory(ClassWithInvalidSetupDefinition.class)
class ClassWithInvalidSetupDefinitionSetup {

    public int undefinedMethod() {
        return 0;
    }
}

class Coffee {
    int caffeineContent;
    int chicoryId;

    public int getCaffeineContent() {
        return caffeineContent;
    }

    public void setCaffeineContent(int caffeineContent) {
        this.caffeineContent = caffeineContent;
    }

    public int getChicoryId() {
        return chicoryId;
    }

    public void setChicoryId(int chicoryId) {
        this.chicoryId = chicoryId;
    }
}

class Chicory {
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

@Factory(Coffee.class)
class CoffeeSetup {
    public int caffeineContent() {
        return 5;
    }

    public int chicoryId(Chicory chicory) {
        return chicory.getId();
    }
}

@Factory(value = Coffee.class, name = "Mocha")
class Mocha extends CoffeeSetup {
}

@Factory(Chicory.class)
class ChicorySetup {
    public int id() {
        return 2;
    }
}

public class SetupDefinitionTest {

    @Test(expected = FactorySetupException.class)
    public void shouldThrowExceptionIfSetupClassHasAPublicMethodThatTakesAnArgument() {
        Instantiator.create(Stub.class);
    }

    @Test(expected = FactorySetupException.class)
    public void shouldThrowExceptionIfSetupClassHasAPublicMethodThatReturnsVoid() {
        Instantiator.create(Stub.class);
    }

    @Test(expected = FactorySetupException.class)
    public void shouldThrowExceptionIfSetupMethodIsNotFound() {
        Instantiator.create(ClassWithInvalidSetupDefinition.class);
    }

    @Test
    public void shouldInheritSettingsFromSuperSetup() {
        assertEquals(5, Instantiator.create(Coffee.class, "Mocha").getCaffeineContent());
        assertEquals(2, Instantiator.create(Coffee.class, "Mocha").getChicoryId());
    }
}
