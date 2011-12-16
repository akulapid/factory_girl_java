package data;

import factory.FactorySetup;

@FactorySetup(type = StubForClassWithParameterizedPublicSetterSetup.class)
public class ClassWithParameterizedPublicSetterSetup {

    public int foo(int bar) {
        return 0;
    }
}
