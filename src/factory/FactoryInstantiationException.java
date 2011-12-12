package factory;

public class FactoryInstantiationException extends RuntimeException {
    public FactoryInstantiationException(InstantiationException e) {
        super(e);
    }
}
