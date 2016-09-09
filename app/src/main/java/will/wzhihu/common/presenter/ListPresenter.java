package will.wzhihu.common.presenter;

import java.util.ArrayList;
import java.util.List;

public abstract class ListPresenter<T> extends TaskPresenter {
    public static String ITEMS_FIELD = "items";

    private List<T> items = new ArrayList<T>();
    private boolean hasAfter = true;
    private boolean hasBefore = false;

    public void refresh() {}

    public void loadBefore() {}

    public void loadAfter() {}

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        setItems(items, true);
    }

    public void setItems(List<T> items, boolean notify) {
        if (items == null) {
            items = new ArrayList<>();
        }
        this.items = items;
        if (notify) {
            notifyChanges();
        }
    }

    public T get(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    public void addAll(List<T> items, boolean notify) {
        if (items == null) {
            items = new ArrayList<T>();
        }
        this.items.addAll(items);
        if (notify) {
            notifyChanges();
        }
    }

    protected void reset() {
        hasAfter = true;
        hasBefore = false;
        items = new ArrayList<>();
    }

    public long getItemId(int position) {
        return this.get(position).hashCode();
    }

    public int size() {
        return this.items.size();
    }

    public void addAll(List<T> items) {
        addAll(items, true);
    }

    public void notifyChanges() {
        firePropertyChange(ITEMS_FIELD);
    }

    public void addItemsListener(PropertyChangeListener listener) {
        addPropertyChangeListener(ITEMS_FIELD, listener);
    }

    public void removeItemsListener(PropertyChangeListener listener) {
        removePropertyChangeListener(ITEMS_FIELD, listener);
    }

    public void setHasAfter(boolean hasAfter) {
        this.hasAfter = hasAfter;
        firePropertyChange("hasAfter");
    }

    public boolean getHasAfter() {
        return this.hasAfter;
    }

    public void setHasBefore(boolean hasBefore) {
        this.hasBefore = hasBefore;
        firePropertyChange("hasBefore");
    }

    public boolean getHasBefore() {
        return this.hasBefore;
    }
}

