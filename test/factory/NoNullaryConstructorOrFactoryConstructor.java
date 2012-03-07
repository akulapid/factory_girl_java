package factory;

import org.junit.Test;

class ClassWithoutNullaryConstructorNorFactoryConstructor {

    public ClassWithoutNullaryConstructorNorFactoryConstructor(int foo) {
    }
}

@FactorySetup(type = ClassWithoutNullaryConstructorNorFactoryConstructor.class)
class ClassWithoutNullaryConstructorNorFactoryConstructorSetup {

    public void foo(Object something) {
    }
}

public class NoNullaryConstructorOrFactoryConstructor {

    @Test(expected = FactoryInstantiationException.class)
    public void shouldThrowExceptionForClassWithoutNullaryConstructorNorFactoryConstructor() {
        Instantiator.create(ClassWithoutNullaryConstructorNorFactoryConstructor.class);
    }
}
