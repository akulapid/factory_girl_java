package data;

import factory.FactorySetup;

@FactorySetup(type = ClassWithSetupDefined.class)
public class ClassWithSetupDefinedSetup {

    public int capacity() {
        return 1000;
    }
}
