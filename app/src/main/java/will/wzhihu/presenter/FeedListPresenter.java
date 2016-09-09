package will.wzhihu.presenter;

import android.os.Bundle;

import java.util.List;
import bolts.Task;
import will.wzhihu.common.model.Container;
import will.wzhihu.common.model.FeedItem;
import will.wzhihu.common.presenter.EditableListPresenter;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.common.task.CancelToken;
import will.wzhihu.common.usecase.FeedSource;
import will.wzhihu.common.usecase.ListFinderTaskSource;
import will.wzhihu.common.utils.CollectionUtils;
import will.wzhihu.common.utils.Functions;
import will.wzhihu.common.utils.ListUtils;
import will.wzhihu.common.utils.ObjectUtils;
/*
 * Note: We make the FeedListPresenter as abstract, so we have to, at least, initialize the class as
 * a anonymous class, so that we can deduce the generic type
 *
 */
public abstract class FeedListPresenter<T extends FeedItem> extends EditableListPresenter<T> {

    private static final String TAG = "FeedListPresenter";

    public static final String FEED_SOURCE_FEED_ID = "FeedListPresenter.feedId";
    public static final String FEED_SOURCE_ARGUMENTS = "FeedListPresenter.arguments";
    public static final String TAG_AFTER = "after";
    public static final String TAG_BEFORE = "before";
    public static final String KEY_FEED_ID = "FeedListPresenter.feedId";
    public static final String KEY_ITEMS = "FeedListPresenter.items";
    public static final String KEY_BEFORE_SCROLL_ID = "FeedListPresenter.beforeScrollId";
    public static final String KEY_AFTER_SCROLL_ID = "FeedListPresenter.afterScrollId";

    public static String ITEMS_FIELD = "items";

    private FeedSource<T> mFeedSource;

    protected Object mBeforeScrollId;

    protected Object mAfterScrollId;

    private boolean mHasAfter = true;

    private boolean mHasBefore = false;

    private Exception mException;

    public static final Functions.Func1<? extends FeedItem, Comparable> GetSort = new Functions.Func1<FeedItem, Comparable>() {

        @Override
        public Comparable call(FeedItem feedItem) {
            return feedItem.getSort();
        }
    };

    private final static Functions.Func1<? extends FeedItem, String> GetItemId = new Functions.Func1<FeedItem, String>() {

        @Override
        public String call(FeedItem feedItem) {
            return feedItem.getItemId();
        }
    };


    private ListFinderTaskSource<T> mRefreshTask = new ListFinderTaskSource<T>() {
        @Override
        protected Task<Container<T>> find(CancelToken cancelToken) {
            return findAfterRemotely(null);
        }

        @Override
        protected void onFailure(Exception exception) {
            super.onFailure(exception);
            setException(exception);
        }

        @Override
        protected void onSuccess(Container<T> result) {
            onRefresh(result);
            setException(null);
        }
    };

    public Exception getException() {
        return mException;
    }

    protected void onRefresh(Container<T> result) {
        reset();
        mBeforeScrollId = result.getBeforeScrollId();
        onAppend(result);
    }

    protected void onAppend(Container<T> result) {
        boolean hasMore = hasMore(result);
        mAfterScrollId = result.getAfterScrollId();
        addAll(sortIfNecessary(result.getItems()));
        setHasAfter(hasMore);
    }

    protected void removeByItemId(String itemId) {
        int index = indexOfByItemId(itemId);
        if (index >= 0) {
            remove(index);
        }
    }

    protected void setByItemId(String itemId, T item) {
        int index = indexOfByItemId(itemId);
        if (index >= 0) {
            set(index, item);
        }
    }

    protected int indexOfByItemId(String itemId) {
        return ListUtils.indexOfByKey(getItems(), (Functions.Func1<T, String>) GetItemId, itemId);
    }

    protected T getByItemId(String itemId) {
        for (T item : getItems()) {
            if (ObjectUtils.equals(item.getItemId(), itemId)) {
                return item;
            }
        }
        return null;
    }

    protected boolean hasMore(Container<T> result) {
        return !(result.getAfterScrollId() == null || CollectionUtils.isEmpty(result.getItems()));
    }

    protected List<T> sortIfNecessary(List<T> items) {
        if (items.size() > 1) {
            T first = items.get(0);
            Comparable sort = first.getSort();
            if (sort != null) {
                items = ListUtils.sortByKey(items, (Functions.Func1<T, Comparable>) GetSort, getFeedSource()
                    .comparator());
            }
        }
        return items;
    }

    public FeedSource<T> getFeedSource() {
        return mFeedSource;
    }

    public FeedListPresenter() {
    }

    public FeedListPresenter(FeedSource<T> feedSource) {
        this.mFeedSource = feedSource;
    }

    public void setFeedSource(FeedSource<T> feedSource) {
        if (this.mFeedSource != feedSource) {
            if (mFeedSource != null) {
                // cancel all previous tasks
                this.cancelAll();
            }
            FeedSource<T> previousFeedSource = this.mFeedSource;
            this.mFeedSource = feedSource;
            if (previousFeedSource != null) {
                this.reset();
            }
            firePropertyChange("feedSource");
        }
    }

    protected void reset() {
        mBeforeScrollId = mAfterScrollId = null;
        mHasBefore = false;
        mHasAfter = true;
        clear();
    }

    public void init(final boolean refresh) {
        if (getFeedSource() == null) {
            return;
        }
        ensureExecute(new ListFinderTaskSource<T>() {
            @Override
            protected Task<Container<T>> find(CancelToken cancelToken) {
                Task<Container<T>> task = findLocally();
                if (cancelToken.isCancelled()) {
                    return Task.cancelled();
                } else {
                    return task;
                }
            }

            @Override
            protected void onFailure(Exception exception) {
                super.onFailure(exception);
                setException(exception);
            }

            @Override
            protected void onSuccess(Container<T> result) {
                if (CollectionUtils.isEmpty(result.getItems()) || refresh) {
                    ensureExecute(mRefreshTask);
                }
                if (!CollectionUtils.isEmpty(result.getItems())) {
                    updateFromCachedValue(result);
                }
            }
        }, TAG_AFTER);
    }

    private Task<Container<T>> findLocally() {
        if (mFeedSource != null) {
            return mFeedSource.findLocally();
        } else {
            return Task.forResult(new Container<T>());
        }
    }

    protected void updateFromCachedValue(Container<T> result) {
        setItems(result.getItems());
        mBeforeScrollId = result.getBeforeScrollId();
        mAfterScrollId = result.getAfterScrollId();
        this.setHasAfter(mAfterScrollId != null);
    }

    public long getItemId(int position) {
        return this.get(position).getItemId().hashCode();
    }

    public void refresh() {
        ensureExecute(mRefreshTask);
    }

    public void loadAfter() {
        if (getFeedSource() == null || !getHasAfter() || mAfterScrollId == null) {
            return;
        }
        execute(new ListFinderTaskSource<T>() {
            @Override
            protected Task<Container<T>> find(CancelToken cancelToken) {
                return findAfterRemotely(mAfterScrollId);
            }

            @Override
            protected void onFailure(Exception exception) {
                super.onFailure(exception);
                setException(exception);
            }

            @Override
            protected void onSuccess(Container<T> result) {
                onAppend(result);
            }
        }, TAG_AFTER);
    }

    private void setException(Exception e) {
        this.mException = e;
        firePropertyChange("exception");
    }

    protected Task<Container<T>> findAfterRemotely(Object afterScrollId) {
        if (mFeedSource != null) {
            return mFeedSource.findAfterRemotely(afterScrollId);
        } else {
            return Task.forResult(new Container<T>());
        }
    }

    public void loadBefore() {
        if (getFeedSource() == null || !getHasBefore() || mBeforeScrollId == null) {
            return;
        }
        execute(new ListFinderTaskSource<T>() {
            @Override
            protected Task<Container<T>> find(CancelToken cancelToken) {
                return findBeforeRemotely(mBeforeScrollId);
            }

            @Override
            protected void onFailure(Exception exception) {
                super.onFailure(exception);
                setException(exception);
            }

            @Override
            protected void onSuccess(Container<T> result) {
                onPrepend(result);
            }
        }, TAG_BEFORE);
    }

    protected Task<Container<T>> findBeforeRemotely(Object beforeScrollId) {
        if (mFeedSource != null) {
            return mFeedSource.findBeforeRemotely(mBeforeScrollId);
        } else {
            return Task.forResult(new Container<T>());
        }
    }

    protected void onPrepend(Container<T> result) {
        boolean hasBefore = true;
        if (CollectionUtils.isEmpty(result.getItems())) {
            hasBefore = false;
        } else {
            addAll(0, sortIfNecessary(result.getItems()));
        }
        mBeforeScrollId = result.getAfterScrollId();
        setHasBefore(hasBefore);
    }

    public void setHasAfter(boolean hasAfter) {
        if (this.mHasAfter != hasAfter) {
            this.mHasAfter = hasAfter;
            firePropertyChange("hasAfter");
        }
    }

    public boolean getHasAfter() {
        return this.mHasAfter;
    }

    public void setHasBefore(boolean hasBefore) {
        if (this.mHasBefore != hasBefore) {
            this.mHasBefore = hasBefore;
            firePropertyChange("hasBefore");
        }
    }

    public boolean getHasBefore() {
        return this.mHasBefore;
    }

    public void addItemsListener(PropertyChangeListener listener) {
        addPropertyChangeListener(ITEMS_FIELD, listener);
    }

    public void removeItemsListener(PropertyChangeListener listener) {
        removePropertyChangeListener(ITEMS_FIELD, listener);
    }

    public boolean clear() {
        mBeforeScrollId = null;
        mAfterScrollId = null;
        if (super.clear()) {
            fireItemsChanged();
            return true;
        } else {
            return false;
        }
    }

    public String[] getFeedItemTypes() {
        return mFeedSource.getFeedItemTypes();
    }

    public Bundle serialize() {
        Bundle bundle = new Bundle();
        bundle.putString(FEED_SOURCE_FEED_ID, mFeedSource.mFeedId);
        Bundle arguments = mFeedSource.saveInstanceState();
        bundle.putBundle(FEED_SOURCE_ARGUMENTS, arguments);
        return bundle;
    }
}
