package akula.factory;

import java.util.List;

public class PersistenceHandlerProxy {

    private AbstractPersistenceHandler persistenceHandler;

    public PersistenceHandlerProxy(AbstractPersistenceHandler persistenceHandler) {
        this.persistenceHandler = persistenceHandler;
    }

    public void execute(ObjectDependency objectDependency) {
        if (persistenceHandler == null)
            throw new PersistenceHandlerMissingException("No persistence handlers found.");
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
