package factory;

public abstract class AbstractPersistenceHandler {

    private String resourceName;

    public AbstractPersistenceHandler(String resourceName) {
        this.resourceName = resourceName;
    }

    public abstract void built(Object object);
}
