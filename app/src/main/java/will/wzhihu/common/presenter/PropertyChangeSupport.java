package will.wzhihu.common.presenter;

import android.os.Looper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import will.wzhihu.common.log.Log;

/**
 * We use ConcurrentHashMap to avoid ConcurrentModificationException which is caused by modifying
 * the PropertyChangeListeners while iterating through the refresh ( yes, even in the same thread will
 * cause the exception).
 *
 * ConcurrentHashMap use a different mechanism to iterate through the refresh ( which is immune to modCount based checking)
 * but may cause NoSuchElementException( but this is not our concern)
 */
public class PropertyChangeSupport extends HashMap<String, CopyOnWriteArraySet<PropertyChangeListener>> {
    private static final String TAG = "PropertyChangeSupport";
    private final Set<String> properties;
    private final String beanDescription;

    public PropertyChangeSupport(String beanDescription, Set<String> properties) {
        this.properties = properties;
        this.beanDescription = beanDescription;
    }

    public void addPropertyChangeListener(String name, PropertyChangeListener listener) {
        ensureMainThread("addPropertyChangeListener must be invoked from main thread");
        validateProperty(name);
        if (!containsKey(name)) {
            put(name, new CopyOnWriteArraySet<PropertyChangeListener>());
        }
        get(name).add(listener);
    }

    public void removePropertyChangeListener(String name, PropertyChangeListener listener) {
        ensureMainThread("removePropertyChangeListener must be invoked from main thread");
        validateProperty(name);
        if (containsKey(name)) {
            get(name).remove(listener);
        }
    }

    public void firePropertyChange(String name) {
        ensureMainThread("firePropertyChange must be invoked from main thread");
        validateProperty(name);
        Log.d(TAG, "fire %s propertyChange", name);
        if (containsKey(name)) {
            for (PropertyChangeListener listener: get(name)) {
                Log.d(TAG, "fire %s propertyChange listener %s", name, listener);
                listener.propertyChanged();
            }
        }
    }


    public void fireChangeAll() {
        ensureMainThread("fireChangeAll must be invoked from main thread");
        for (Set<PropertyChangeListener> listeners : new ArrayList<>(values())) {
            for (PropertyChangeListener listener : listeners) {
                listener.propertyChanged();
            }
        }
    }

    private void ensureMainThread(String message) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException(message);
        }
    }

    private void validateProperty(String name) {
        if (!properties.contains(name)) {
            throw new IllegalArgumentException("Bean " + beanDescription + " does not contain the given property " + name);
        }
    }
}
