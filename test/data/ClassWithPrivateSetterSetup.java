package data;

import factory.FactorySetup;

@FactorySetup(type = StubForClassWithPrivateSetterSetup.class)
public class ClassWithPrivateSetterSetup {

    private int foo() {
        return 100;
    }
}
