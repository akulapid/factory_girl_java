package factory;

public abstract class FactoryPersistenceHandler {

    private String resourceName;

    public FactoryPersistenceHandler(String resourceName) {
        this.resourceName = resourceName;
    }

    public abstract void built(Object object);
}
