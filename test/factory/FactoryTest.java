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
    public void shouldSetupWhenInstantiatingClass() {
        assertEquals(1000, Factory.create(ClassWithSetupDefined.class).getCapacity());
    }

    @Test
    public void shouldSetupFieldsOfMembersOfTheClass() {
        assertEquals(25, Factory.create(Dashboard.class).getSteeringWheel().getTension());
    }

    @Test
    public void shouldNotThrowExceptionIfSetupIsNotDefined() {
        try {
            Factory.create(ClassWithoutSetupDefined.class);
        } catch (NullPointerException e) {
            fail("should not try to instantiate undefined setup class");
        }
    }

    @Test
    public void shouldInstantiateThroughConstructor() {
        assertEquals(4, Factory.create(ClassWithoutNullaryConstructor.class).getFoo());
    }

    @Test
    public void shouldNotUseFactoryConstructorForSetup() {
        try {
            Factory.create(ClassWithoutNullaryConstructor.class);
        } catch (Exception e) {
            fail("should not use factory constructor for setup");
        }
    }

    @Test
    public void shouldUsePublicMethodsOnlyForSetup() {
        assertEquals(0,Factory.create(StubForClassWithPrivateSetterSetup.class).getFoo());
    }

    @Test(expected = FactoryInstantiationException.class)
    public void shouldThrowExceptionForClassWithoutNullaryConstructorNorFactoryConstructor() {
        Factory.create(ClassWithoutNullaryConstructorNorFactoryConstructor.class);
    }

    @Test(expected = FactorySetupException.class)
    public void shouldThrowExceptionIfSetupClassHasAPublicMethodThatTakesAnArgument() {
        Factory.create(StubForClassWithParameterizedPublicSetterSetup.class);
    }

    @Test(expected = FactorySetupException.class)
    public void shouldThrowExceptionIfSetupClassHasAPublicMethodThatReturnsVoid() {
        Factory.create(StubForClassWithVoidPublicSetterSetup.class);
    }
}
