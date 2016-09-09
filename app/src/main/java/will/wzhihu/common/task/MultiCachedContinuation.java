package will.wzhihu.common.task;

import android.support.annotation.Nullable;

import java.util.concurrent.ConcurrentHashMap;

import bolts.Continuation;
import bolts.Task;

public abstract class MultiCachedContinuation<T, R, Key> implements Continuation<T, Task<R>> {

    private ConcurrentHashMap<Key, Task<R>.TaskCompletionSource> cachedTasks
            = new ConcurrentHashMap<Key, Task<R>.TaskCompletionSource>();


    public MultiCachedContinuation() {
    }

    protected abstract Task<R> convert(T source);

    protected abstract Key getKey(@Nullable T result);

    @Override
    public Task<R> then(Task<T> task) throws Exception {
        if (task.isFaulted()) {
            return Task.forError(task.getError());
        } else if (task.isCancelled()) {
            return Task.cancelled();
        } else {
            T result = task.getResult();
            try {
                Key key = getKey(result);
                Task<R>.TaskCompletionSource source = cachedTasks.get(key);
                if (source != null) {
                    return source.getTask();
                }
                source = Task.create();
                Task<R>.TaskCompletionSource previous = cachedTasks.putIfAbsent(key,
                        source);
                if (previous == null) {
                    execute(key, result, source);
                } else {
                    source = previous;
                }
                return source.getTask();
            } catch (Exception e) {
                return Task.forError(e);
            }
        }
    }

    private void execute(final Key key,
                         final T taskResult,
                         final Task<R>.TaskCompletionSource source) {
        try {
            convert(taskResult).continueWith(new Continuation<R, Void>() {
                @Override
                public Void then(Task<R> task) throws Exception {
                    if (task.isFaulted()) {
                        source.setError(task.getError());
                    } else if (task.isCancelled()) {
                        source.setCancelled();
                    } else {
                        source.setResult(task.getResult());
                    }
                    return null;
                }
            }, Task.BACKGROUND_EXECUTOR).continueWith(new Continuation<Void, Void>() {
                @Override
                public Void then(Task<Void> task) throws Exception {
                    cachedTasks.remove(key);
                    return null;
                }
            });
        } catch (Exception e) {
            cachedTasks.remove(key);
            source.setError(e);
        }
    }
}
