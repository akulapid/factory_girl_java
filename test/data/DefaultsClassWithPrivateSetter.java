package data;

import factory.FactoryDefault;

@FactoryDefault(type = StubForDefaultsClassWithPrivateSetter.class)
public class DefaultsClassWithPrivateSetter {

    private int foo() {
        return 100;
    }
}
