package factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
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

@FactorySetup(VehicleSetup.class)
class VehicleSetup {
}

@FactorySetup(MotorVehicleSetup.class)
class MotorVehicleSetup {
}

@FactorySetup(Car.class)
class CarSetup {
}

@FactorySetup(Motor.class)
class MotorSetup {
}

@FactorySetup(Chassis.class)
class ChassisSetup {
}

class SuperClassWithSetup {
    int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

public class InheritanceInstantiationTest {

    @Test
    public void shouldInstantiateFieldsInSuperClass() {
        Car car = Instantiator.create(Car.class);
        assertNotNull(car.getMotor());
        assertNotNull(car.getChassis());
    }
}
