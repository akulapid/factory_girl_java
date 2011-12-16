package data;

import factory.FactoryDefault;

@FactoryDefault(type = ClassWithoutNullaryConstructor.class)
public class ClassWithoutNullaryConstructorDefaults {

    public ClassWithoutNullaryConstructor constructor() {
        return new ClassWithoutNullaryConstructor(4);
    }
}
