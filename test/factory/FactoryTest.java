package factory;

import data.*;
import org.junit.Test;

import static junit.framework.Assert.*;

public class FactoryTest {

    @Test
    public void shouldInstantiateAClass() {
        assertTrue(Factory.create(SampleClass.class) instanceof SampleClass);
    }

    @Test(expected = FactoryInstantiationException.class)
    public void shouldThrowExceptionForClassWithoutNullaryConstructor() {
        Factory.create(ClassWithoutNullaryConstructor.class);
    }

    @Test
    public void shouldNotInstantiatePrimitiveField() {
        Factory.create(ClassWithPrimitiveField.class);
    }

    @Test
    public void shouldNotInstantiateArrayField() {
        Factory.create(ClassWithArrayField.class);
    }

    @Test
    public void shouldNotInstantiateEnumField() {
        Factory.create(ClassWithEnumField.class);
    }

    @Test
    public void shouldInstantiateNonPrimitiveFields() {
        Car car = Factory.create(Car.class);
        assertNotNull(car.getDashboard());
        assertNotNull(car.getDashboard().getSteeringWheel());
    }

    @Test
    public void shouldInstantiateFieldsInSuperClass() {
        Car car = Factory.create(Car.class);
        assertNotNull(car.getMotor());
        assertNotNull(car.getChassis());
    }

    @Test
    public void shouldSetDefaultsWhenInstantiatingClass() {
        assertEquals(1000, Factory.create(ClassWithDefaultsDefined.class).getCapacity());
    }

    @Test
    public void shouldNotThrowExceptionIfDefaultsIsNotDefined() {
        try {
            Factory.create(ClassWithoutDefaultsDefined.class);
        } catch (NullPointerException e) {
            fail();
        }
    }

    @Test
    public void shouldSetDefaultsForFieldsOfMembersOfTheClass() {
         assertEquals(25, Factory.create(Dashboard.class).getSteeringWheel().getTension());
    }
}
