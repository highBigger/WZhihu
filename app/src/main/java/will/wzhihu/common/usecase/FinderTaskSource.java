package will.wzhihu.common.usecase;

import bolts.Continuation;
import bolts.Task;
import will.wzhihu.BuildConfig;
import will.wzhihu.R;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.task.CancelToken;
import will.wzhihu.common.task.CancellableTaskSource;
import will.wzhihu.common.utils.ToastUtils;

public abstract class FinderTaskSource<T> implements CancellableTaskSource<T> {

    private final static String TAG = "FinderTaskSource";

    protected abstract bolts.Task<T> find(CancelToken cancelToken);

    protected abstract void onSuccess(T result);

    protected void onFailure(Exception exception) {
        if (BuildConfig.DEBUG) {
           ToastUtils.toast(exception.toString(), 0);
        } else {
            ToastUtils.toast(R.string.loading_failed);
        }
        Log.e(TAG, "loading failed", exception);
    }

    protected void onCancelled() {}

    protected boolean shouldFind() {
        return true;
    }

    @Override
    public Task<T> getTask(final CancelToken cancelToken) {
        if (shouldFind()) {
            Task<T> task = find(cancelToken);
            if (task == null) {
                task = Task.forResult(null);
            }
            return task.continueWithTask(new Continuation<T, Task<T>>() {
                @Override
                public Task<T> then(Task<T> task) throws Exception {
                    if (task.isCancelled() || cancelToken.isCancelled()) {
                        onCancelled();
                        return Task.cancelled();
                    } else if (task.isFaulted()) {
                        onFailure(task.getError());
                    } else {
                        onSuccess(task.getResult());
                    }
                    return task;
                }
            }, Task.UI_THREAD_EXECUTOR);
        } else {
            return Task.forResult(null);
        }
    }
}
