package data;

import factory.FactorySetup;

@FactorySetup(type = ClassWithoutNullaryConstructorNorFactoryConstructor.class)
public class ClassWithoutNullaryConstructorNorFactoryConstructorSetup {

    public void foo(Object something) {

    }
}
