package factory;

import data.*;
import org.junit.Test;

import static junit.framework.Assert.*;

public class FactoryTest {

    @Test
    public void shouldInstantiateAClass() {
        assertTrue(Factory.create(SampleClass.class) instanceof SampleClass);
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
    public void shouldSetDefaultsForFieldsOfMembersOfTheClass() {
        assertEquals(25, Factory.create(Dashboard.class).getSteeringWheel().getTension());
    }

    @Test
    public void shouldNotThrowExceptionIfDefaultsIsNotDefined() {
        try {
            Factory.create(ClassWithoutDefaultsDefined.class);
        } catch (NullPointerException e) {
            fail("should not try to instantiate undefined defaults class");
        }
    }

    @Test
    public void shouldInstantiateThroughConstructor() {
        assertEquals(4, Factory.create(ClassWithoutNullaryConstructor.class).getFoo());
    }

    @Test
    public void shouldNotUseFactoryConstructorForDefaults() {
        try {
            Factory.create(ClassWithoutNullaryConstructor.class);
        } catch (Exception e) {
            fail("should not use factory constructor for defaults");
        }
    }

    @Test
    public void shouldUsePublicMethodsOnlyForDefaults() {
        assertEquals(0,Factory.create(StubForDefaultsClassWithPrivateSetter.class).getFoo());
    }

    @Test(expected = FactoryInstantiationException.class)
    public void shouldThrowExceptionForClassWithoutNullaryConstructorNorFactoryConstructor() {
        Factory.create(ClassWithoutNullaryConstructorNorFactoryConstructor.class);
    }

    @Test(expected = FactoryDefaultsInstantiationException.class)
    public void shouldThrowExceptionIfDefaultsClassHasAPublicMethodThatTakesAnArgument() {
        Factory.create(StubForDefaultsClassWithParameterizedPublicSetter.class);
    }

    @Test(expected = FactoryDefaultsInstantiationException.class)
    public void shouldThrowExceptionIfDefaultsClassHasAPublicMethodThatReturnsVoid() {
        Factory.create(StubForDefaultsClassWithVoidPublicSetter.class);
    }
}
