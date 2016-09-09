package will.wzhihu.common.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author dusiyu
 */
public class SetMultimap<K, V> {

    private Map<K, Set<V>> map;

    private int totalSize = 0;

    public SetMultimap() {
        map = new HashMap<>();
    }

    public int size() {
        return totalSize;
    }

    public int size(K key) {
        Set<V> collection = map.get(key);
        if (collection == null) {
            return 0;
        } else {
            return collection.size();
        }
    }

    public boolean isEmpty() {
        return totalSize == 0;
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        for (Set<V> collection : map.values()) {
            if (collection.contains(value)) {
                return true;
            }
        }
        return false;
    }

    public boolean remove(Object key, Object value) {
        Set<V> collection = map.get(key);
        if (collection == null) {
            return false;
        }

        boolean changed = collection.remove(value);
        if (changed) {
            totalSize--;
            if (collection.isEmpty()) {
                map.remove(key);
            }
        }
        return changed;
    }

    public boolean put(K key, V value) {
        Collection<V> collection = getOrCreateCollection(key);
        if (collection.add(value)) {
            totalSize++;
            return true;
        } else {
            return false;
        }
    }

    private Collection<V> getOrCreateCollection(K key) {
        Set<V> collection = map.get(key);
        if (collection == null) {
            collection = new HashSet<>();
            map.put(key, collection);
        }
        return collection;
    }

    public void clear() {
        for (Set<V> collection : map.values()) {
            collection.clear();
        }
        map.clear();
        totalSize = 0;
    }
}
