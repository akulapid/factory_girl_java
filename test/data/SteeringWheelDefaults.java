package data;

import factory.FactoryDefault;

@FactoryDefault(type = SteeringWheel.class)
public class SteeringWheelDefaults {

    public int tension() {
        return 25;
    }
}
