package factory;

import data.Motor;

@FactoryDefault(type = Motor.class)
class MotorDefaults {

    public int capacity() {
        return 1000;
    }
}
