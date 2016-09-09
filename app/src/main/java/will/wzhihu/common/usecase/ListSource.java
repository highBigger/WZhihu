package will.wzhihu.common.usecase;

import android.os.Bundle;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.model.Container;

public abstract class ListSource<T> {
    public final String TAG = getClass().getName();

    public ListSource() {
    }

    public final Task<Container<T>> findLocally() {
        Log.d(TAG, "findLocally");
        return Task.callInBackground(new Callable<Container<T>>() {
            @Override
            public Container<T> call() throws Exception {
                return decorateFeed(findFromCache(), false);
            }
        });
    }

    protected abstract Container<T> findFromCache();

    protected Container<T> decorateFeed(Container<T> container, boolean remote) {
        return container;
    }

    public final Task<Container<T>> findAfterRemotely(final Object scrollId) {
        Log.d(TAG, "findAfterRemotely, scrollId: %s", scrollId);
        return doFindAfter(scrollId)
                .onSuccess(new Continuation<Container<T>, Container<T>>() {
                    @Override
                    public Container<T> then(Task<Container<T>> task) throws Exception {
                        Container<T> feeds = task.getResult();
                        cache(feeds, scrollId, true);
                        return decorateFeed(feeds, true);
                    }
                });
    }

    public final Task<Container<T>> findBeforeRemotely(final Object scrollId) {
        Log.d(TAG, "findBeforeRemotely, scrollId: %s", scrollId);
        return doFindBefore(scrollId)
                .onSuccess(new Continuation<Container<T>, Container<T>>() {
                    @Override
                    public Container<T> then(Task<Container<T>> task) throws Exception {
                        Container<T> feeds = task.getResult();
                        cache(feeds, scrollId, false);
                        return decorateFeed(feeds, true);
                    }
                });
    }

    protected abstract Task<Container<T>> doFindAfter(Object scrollId);

    protected abstract Task<Container<T>> doFindBefore(Object scrollId);

    protected void cache(Container<T> feeds, Object scrollId, boolean after) {
    }

    public Bundle saveInstanceState() {
        return new Bundle();
    }

    public void restoreInstanceState(Bundle bundle) {
    }


}
