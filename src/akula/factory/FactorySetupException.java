package akula.factory;

public class FactorySetupException extends RuntimeException {
    public FactorySetupException(String message, Throwable e) {
        super(message, e);
    }
}
