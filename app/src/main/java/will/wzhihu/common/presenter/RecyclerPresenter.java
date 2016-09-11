package will.wzhihu.common.presenter;

import will.wzhihu.common.model.FeedItem;

public abstract class RecyclerPresenter<T extends FeedItem> extends EditableListPresenter<T>{
    public static String ITEMS_FIELD = "items";

    public abstract String[] getFeedItemTypes();

    public void addItemsListener(PropertyChangeListener listener) {
        addPropertyChangeListener(ITEMS_FIELD, listener);
    }

    public void removeItemsListener(PropertyChangeListener listener) {
        removePropertyChangeListener(ITEMS_FIELD, listener);
    }

    public long getItemId(int position) {
        return this.get(position).getItemId().hashCode();
    }

    public abstract void loadAfter();

    public void loadBefore() {};
}
