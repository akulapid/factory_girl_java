package data;

import data.ClassWithDefaultsDefined;
import factory.FactoryDefault;

@FactoryDefault(type = ClassWithDefaultsDefined.class)
class DefaultsForAClass {

    public int capacity() {
        return 1000;
    }
}
