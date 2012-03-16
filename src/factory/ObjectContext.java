package factory;

import java.util.HashMap;
import java.util.LinkedList;

public class ObjectContext extends HashMap<String, LinkedList<Object>> {

    public void addDependency(String key, Object object) {
        if (!containsKey(key))
            put(key, new LinkedList<Object>());
        get(key).offer(object);
    }
}
