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

@FactorySetup(type = VehicleSetup.class)
class VehicleSetup {
}

@FactorySetup(type = MotorVehicleSetup.class)
class MotorVehicleSetup {
}

@FactorySetup(type = Car.class)
class CarSetup {
}

@FactorySetup(type = Motor.class)
class MotorSetup {
}

@FactorySetup(type = Chassis.class)
class ChassisSetup {
}

public class InheritanceInstantiationTest {

    @Test
    public void shouldInstantiateFieldsInSuperClass() {
        Car car = Instantiator.create(Car.class);
        assertNotNull(car.getMotor());
        assertNotNull(car.getChassis());
    }
}
