package factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

class Train {

    private int capacity;
    private Engine engine;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Engine getEngine() {
        return engine;
    }

}

class Engine {

    int kind;

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}

@FactorySetup(type = Train.class)
class TrainSetup {

    public int capacity() {
        return 1000;
    }
}

@FactorySetup(type = Engine.class)
class EngineSetup {

    public int kind() {
        return 25;
    }
}

public class InstantiationTest {

    @Test
    public void shouldInstantiateAClass() {
        assertTrue(Factory.create(Train.class) instanceof Train);
    }

    @Test
    public void shouldSetupWhenInstantiatingClass() {
        assertEquals(1000, Factory.create(Train.class).getCapacity());
    }

    @Test
    public void shouldSetupFieldsOfMembersOfTheClass() {
        assertEquals(25, Factory.create(Train.class).getEngine().getKind());
    }
}

