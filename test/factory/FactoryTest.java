package factory;

import data.*;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class FactoryTest {

    @Test
    public void shouldCreateAnInstanceOfTheClass() throws InstantiationException {
        assertTrue(Factory.create(SampleClass.class) instanceof SampleClass);
    }

    @Test(expected = InstantiationException.class)
    public void shouldThrowExceptionWhenUsedOnAClassWithoutANullaryConstructor() throws InstantiationException {
        Factory.create(Integer.class);
    }

    @Test
    public void shouldNotInstantiatePrimitiveField() throws InstantiationException {
        Factory.create(ClassWithPrimitiveField.class);
    }

    @Test
    public void shouldNotInstantiateArrayField() throws InstantiationException {
        Factory.create(ClassWithArrayField.class);
    }

    @Test
    public void shouldNotInstantiateEnumField() throws InstantiationException {
        Factory.create(ClassWithEnumField.class);
    }

    @Test
    public void shouldInstantiateFieldsInTheSuperClass() throws InstantiationException {
        Car car = Factory.create(Car.class);
        assertNotNull(car.getMotor());
        assertNotNull(car.getChassis());
    }

    @Test
    public void shouldInstantiateNonPrimitiveFields() throws InstantiationException {
        Car car = Factory.create(Car.class);
        assertNotNull(car.getDashboard());
        assertNotNull(car.getDashboard().getSteeringWheel());
    }
}
