package data;

import factory.FactoryDefault;

@FactoryDefault(type = ClassWithoutNullaryConstructorNorFactoryConstructor.class)
public class ClassWithoutNullaryConstructorNorFactoryConstructorDefaults {

    public void foo(Object something) {

    }
}
