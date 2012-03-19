package factory;

import java.util.List;

public class PersistenceHandlerProxy {

    private AbstractPersistenceHandler persistenceHandler;

    public PersistenceHandlerProxy(AbstractPersistenceHandler persistenceHandler) {
        if (persistenceHandler == null)
            throw new PersistenceHandlerMissingException("No persistence handlers found.");
        this.persistenceHandler = persistenceHandler;
    }

    public void execute(ObjectDependency objectDependency) {
        while (true) {
            List<ObjectDependency> dependencies = objectDependency.getDependencies();
            for (ObjectDependency dependency : dependencies)
                execute(dependency);

            Object object = objectDependency.getObject();
            if (object != null)
                persistenceHandler.persist(object);
            return;
        }
    }
}
