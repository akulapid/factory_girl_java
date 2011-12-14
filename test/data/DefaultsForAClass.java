package data;

import data.ClassWithDefaultsDefined;
import factory.FactoryDefault;

@FactoryDefault(type = ClassWithDefaultsDefined.class)
public class DefaultsForAClass {

    public int capacity() {
        return 1000;
    }
}
