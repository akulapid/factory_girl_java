package data;

import factory.FactorySetup;

@FactorySetup(type = ClassWithoutNullaryConstructor.class)
public class ClassWithoutNullaryConstructorDefined {

    public ClassWithoutNullaryConstructor constructor() {
        return new ClassWithoutNullaryConstructor(4);
    }
}
