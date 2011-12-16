package data;

import factory.FactoryDefault;

import javax.annotation.PostConstruct;

@FactoryDefault(type = StubForDefaultsClassWithVoidPublicSetter.class)
public class DefaultsClassWithVoidPublicSetter {

    public void foo() {
    }
}
