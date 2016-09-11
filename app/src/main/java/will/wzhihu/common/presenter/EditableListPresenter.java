package will.wzhihu.common.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import will.wzhihu.common.log.Log;
import will.wzhihu.common.utils.CollectionUtils;

public class EditableListPresenter<T> extends LoadingPresenter {

    private static final String TAG = "EditableListPresenter";

    public static final String FIELD_ITEMS = "items";

    private List<T> mItems = new ArrayList<T>();

    public void add(int location, T item) {
        mItems.add(location, item);
        this.fireItemInserted(location);
    }

    public void add(T item) {
        int size = mItems.size();
        mItems.add(item);
        this.fireItemInserted(size);
    }

    public void setItems(List<T> items) {
        mItems = new ArrayList<>(items);
        for (ListChangeListener listener : mListChangeListeners) {
            Log.d(TAG, "set items fire listener" + listener);
            listener.onItemsChanged();
        }
        this.fireItemsChanged();
    }

    public void fireItemsChanged() {
        this.firePropertyChange("items");
    }

    public void addAll(int location, Collection<? extends T> collection) {
        if (mItems.addAll(location, collection)) {
            this.fireItemRangeInserted(location, collection.size());
        }
    }

    public void addAll(Collection<? extends T> collection) {
        int size = mItems.size();
        if (mItems.addAll(collection)) {
            this.fireItemRangeInserted(size, collection.size());
        }
    }

    public boolean clear() {
        if (!mItems.isEmpty()) {
            int size = mItems.size();
            mItems.clear();
            if( size > 0) {
                this.fireItemRangeRemoved(0, size);
            }
            return true;
        }
        return false;
    }

    public int size() {
        return mItems.size();
    }

    protected List<T> innerGetItems() {
        return mItems;
    }

    public T get(int location) {
        return mItems.get(location);
    }


    public void remove(int location) {
        mItems.remove(location);
        this.fireItemRemoved(location);
    }

    public boolean remove(Object item) {
        int index = mItems.indexOf(item);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    public void remove(int location, int count) {
        // XXX this can be optimized by manually move elements forward
        count = Math.min(mItems.size() - location, count);
        for (int i = 0; i < count; i++) {
            mItems.remove(location);
        }
        this.fireItemRangeRemoved(location, count);
    }

    public void replace(int location, int size, Collection<? extends T> collection) {
        List<? extends T> list = CollectionUtils.toList(collection);
        int replaceSize = list.size();
        int inPlaceReplacingCount =  Math.min(size, replaceSize);
        int index = 0;
        for (; index < inPlaceReplacingCount; index++) {
            mItems.set(location + index, list.get(index));
        }
        if (inPlaceReplacingCount < replaceSize) {
            mItems.addAll(location + inPlaceReplacingCount, list.subList(inPlaceReplacingCount,
                replaceSize));
        } else if (inPlaceReplacingCount < size) {
            shiftLeft(location + size, size - inPlaceReplacingCount);
        }
        if (inPlaceReplacingCount > 0) {
            fireItemRangeChanged(location, inPlaceReplacingCount);
        }
        if (inPlaceReplacingCount < replaceSize) {
            fireItemRangeInserted(location + inPlaceReplacingCount, replaceSize - inPlaceReplacingCount);
        } else if (inPlaceReplacingCount < size) {
            fireItemRangeRemoved(location + inPlaceReplacingCount, size - inPlaceReplacingCount);
        }
    }

    public void move(int fromIndex, int toIndex) {
        T item = mItems.get(fromIndex);
        shiftLeft(fromIndex, toIndex, item);
        fireItemMoved(fromIndex, toIndex);
    }

    private void shiftLeft(int fromIndex, int toIndex, T fill) {
        for (int i = fromIndex + 1; i < toIndex; i++) {
            mItems.set(i, mItems.get(i + 1));
        }
        mItems.set(toIndex, fill);
    }

    private void shiftLeft(int fromIndex, int step) {
        int size = mItems.size() - fromIndex;
        for (int i = 0; i < size; i++) {
            mItems.set(fromIndex - step + i, mItems.get(fromIndex + i));
        }
        for (int i = 0; i < step; i++) {
            mItems.remove(mItems.size() - 1);
        }
    }

    public List<T> getItems() {
        // XXX should we use protect copy here?
        return mItems;
    }

    public boolean isEmpty() {
        return mItems == null || mItems.size() <= 0;
    }

    public void set(int location, T item) {
        mItems.set(location, item);
        this.fireItemChanged(location);
    }

    private List<ListChangeListener> mListChangeListeners = new ArrayList<>();

    public void addListChangeListener(ListChangeListener listener) {
        this.mListChangeListeners.add(listener);
    }

    public void removeListChangeListener(ListChangeListener listener) {
        this.mListChangeListeners.remove(listener);
    }

    protected void fireItemChanged(int position) {
        for (ListChangeListener listener : mListChangeListeners) {
            listener.onItemChanged(position);
        }
        fireItemsChanged();
    }

    protected void fireItemRangeChanged(int positionStart, int itemCount) {
        for (ListChangeListener listener : mListChangeListeners) {
            listener.onItemRangeChanged(positionStart, itemCount);
        }
        fireItemsChanged();
    }

    protected void fireItemInserted(int position) {
        for (ListChangeListener listener : mListChangeListeners) {
            listener.onItemInserted(position);
        }
        fireItemsChanged();
    }

    protected void fireItemRangeInserted(int positionStart, int itemCount) {
        for (ListChangeListener listener : mListChangeListeners) {
            listener.onItemRangeInserted(positionStart, itemCount);
        }
        fireItemsChanged();
    }

    protected void fireItemMoved(int fromPosition, int toPosition) {
        for (ListChangeListener listener : mListChangeListeners) {
            listener.onItemMoved(fromPosition, toPosition);
        }
        fireItemsChanged();
    }

    protected void fireItemRemoved(int position) {
        for (ListChangeListener listener : mListChangeListeners) {
            listener.onItemRemoved(position);
        }
        fireItemsChanged();
    }

    protected void fireItemRangeRemoved(int positionStart, int itemCount) {
        for (ListChangeListener listener : mListChangeListeners) {
            listener.onItemRangeRemoved(positionStart, itemCount);
        }
        fireItemsChanged();
    }

    public interface ListChangeListener {
        void onItemsChanged();

        void onItemChanged(int position);

        void onItemRangeChanged(int positionStart, int itemCount);

        void onItemInserted(int position);

        void onItemRangeInserted(int positionStart, int itemCount);

        void onItemMoved(int fromPosition, int toPosition);

        void onItemRemoved(int position);

        void onItemRangeRemoved(int positionStart, int itemCount);
    }

    public static class ListChangeListenerAdapter implements ListChangeListener{

        @Override
        public void onItemsChanged() {

        }

        @Override
        public void onItemChanged(int position) {
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
        }

        @Override
        public void onItemInserted(int position) {
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
        }

        @Override
        public void onItemMoved(int fromPosition, int toPosition) {

        }

        @Override
        public void onItemRemoved(int position) {

        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {

        }
    }
}
