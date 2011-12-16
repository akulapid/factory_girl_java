package data;

import factory.FactorySetup;

@FactorySetup(type = SteeringWheel.class)
public class SteeringWheelSetup {

    public int tension() {
        return 25;
    }
}
