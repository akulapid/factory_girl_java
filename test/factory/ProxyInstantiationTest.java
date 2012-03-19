package factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

class Furniture {
    String madeOf;

    public String getMadeOf() {
        return madeOf;
    }

    public void setMadeOf(String madeOf) {
        this.madeOf = madeOf;
    }
}

class Drawer {
    int capacity;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}

class Table extends Furniture {
    String shape;
    Drawer drawer;

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Drawer getDrawer() {
        return drawer;
    }

    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }
}

@Setup(Furniture.class)
class FurnitureSetup {
    public String madeOf() {
        return "wood";
    }
}

@Setup(Table.class)
class TableSetup {
    public String shape() {
        return "rectangle";
    }
}

@Setup(value = Table.class, name = "RoundTable")
class RoundTable {
    public String shape() {
        return "circle";
    }
}

@Setup(Drawer.class)
class DrawerSetup {
    public int capacity() {
        return 2000;
    }
}

class TableProxy extends Table {
    public TableProxy(PersistenceHandlerProxy persistenceHandlerProxy, ObjectDependency objectDependency) {
    }
}

@__SetupForProxy(value = TableProxy.class, factory = "")
class TableProxySetup {
}

public class ProxyInstantiationTest {

    @Test
    public void shouldInstantiateProxyAndSetupActualSuperFields() {
        assertEquals("wood", Instantiator.createProxy(TableProxy.class, "").getMadeOf());
    }

    @Test
    public void shouldInstantiateProxyAndSetupActualThisFields() {
        assertEquals("rectangle", Instantiator.createProxy(TableProxy.class, "").getShape());
    }

    @Test
    public void shouldInstantiateProxyAndSetupActualThisFieldsFromAlias() {
        assertEquals("circle", Instantiator.createProxy(TableProxy.class, "RoundTable").getShape());
    }

    @Test
    public void shouldInstantiateProxyAndInstantiateAndSetupThisFields() {
        assertEquals(2000, Instantiator.createProxy(TableProxy.class, "RoundTable").getDrawer().getCapacity());
    }
}
