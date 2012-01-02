package factory;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

class Vehicle {

    private Chassis chassis;

    public Chassis getChassis() {
        return chassis;
    }
}

class MotorVehicle extends Vehicle {

    private Motor motor;

    public Motor getMotor() {
        return motor;
    }
}

class Car extends MotorVehicle{
}

class Motor {
}

class Chassis {
}

public class InheritanceInstantiationTest {

    @Test
    public void shouldInstantiateFieldsInSuperClass() {
        Car car = Factory.create(Car.class);
        assertNotNull(car.getMotor());
        assertNotNull(car.getChassis());
    }
}
