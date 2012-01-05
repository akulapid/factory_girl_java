package factory;

import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;

class ClassComposingAnInterface {

    List list;

    public List getList() {
        return list;
    }
}
public class InterfaceInstantiationTest {

    @Test
    public void shouldNotInstantiateInterfaceFields() {
        assertNull(Factory.create(ClassComposingAnInterface.class).getList());
    }
}
