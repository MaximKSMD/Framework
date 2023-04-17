package zutils;

import java.util.HashMap;
import java.util.Map;

public class VariablesStore {

    private Map<String, Object> variablesStoreMap = new HashMap<>();

    public void put(String key, Object value) {
        variablesStoreMap.put(key, value);
    }

    public <T> T get(String key, Class<T> clazz) {

        return clazz.isInstance(variablesStoreMap.get(key)) ?
                clazz.cast(variablesStoreMap.get(key)) : null;
    }
}
