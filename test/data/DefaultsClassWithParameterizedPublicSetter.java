package data;

import factory.FactoryDefault;

@FactoryDefault(type = StubForDefaultsClassWithParameterizedPublicSetter.class)
public class DefaultsClassWithParameterizedPublicSetter {

    public int foo(int bar) {
        return 0;
    }
}
