package akula.factory;

public abstract class AbstractPersistenceHandler {

    protected String databaseName;

    public AbstractPersistenceHandler(String databaseName) {
        this.databaseName = databaseName;
    }

    public abstract void persist(Object object);
}
