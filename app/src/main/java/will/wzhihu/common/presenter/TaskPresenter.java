package will.wzhihu.common.presenter;

import android.support.v4.util.ArrayMap;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.model.SetMultimap;
import will.wzhihu.common.task.CancelToken;
import will.wzhihu.common.task.CancellableTaskSource;
import will.wzhihu.common.task.TaskExecutor;

public class TaskPresenter extends BasePresenter {

    public static final String DEFAULT_TASK = "__DEFAULT_TASK__";

    public static final String DEFAULT_ENSURE_TASK = "__DEFAULT_ENSURE_TASK__";

    private TaskExecutor taskExecutor = new TaskExecutor(providesTaskFilter());

    private final Object LOCK = new Object();

    private SetMultimap<String, LoadingTaskSource<?>> taskSources = new SetMultimap<>();

    private void submitTaskSource(LoadingTaskSource<?> taskSource) {
        synchronized (LOCK) {
            if (taskSources.put(taskSource.tag, taskSource) && taskSources.size() == 1) {
                fireLoadingChange();
            }
        }
    }

    private void finishTaskSource(LoadingTaskSource<?> taskSource) {
        synchronized (LOCK) {
            if (taskSources.remove(taskSource.tag, taskSource) && taskSources.size() == 0) {
                fireLoadingChange();
            }
        }
    }

    public boolean isLoading(String tag) {
        synchronized (LOCK) {
            return taskSources.size(tag) > 0;
        }
    }

    private TaskExecutor.TaskFilter providesTaskFilter() {
        return TaskExecutor.SingleTaskPerTag;
    }

    private void fireLoadingChange() {
        Task.call(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                firePropertyChange("loading");
                return null;
            }
        });
    }

    public boolean getLoading() {
        synchronized (LOCK) {
            return taskSources.size() > 0;
        }
    }


    public void cancelAll() {
        taskExecutor.cancel();
        synchronized (LOCK) {
            taskSources.clear();
        }
        firePropertyChange("loading");
    }

    public <T> void ensureExecute(CancellableTaskSource<T> taskSource) {
        this.ensureExecute(taskSource, LoadingIndicator.Show);
    }

    public <T> void ensureExecute(CancellableTaskSource<T> taskSource, LoadingIndicator loadingIndicator) {
        this.ensureExecute(taskSource, loadingIndicator, DEFAULT_ENSURE_TASK);
    }
    public <T> void ensureExecute(CancellableTaskSource<T> taskSource, String tag) {
        this.ensureExecute(taskSource, LoadingIndicator.Show, tag);
    }

    public <T> void ensureExecute(CancellableTaskSource<T> taskSource, LoadingIndicator loadingIndicator, String tag) {
        taskExecutor.ensureExecute(new LoadingTaskSource<T>(taskSource, loadingIndicator, tag), tag);
    }


    public <T> boolean execute(CancellableTaskSource<T> taskSource) {
        return execute(taskSource, LoadingIndicator.Show);
    }



    public <T> boolean execute(final CancellableTaskSource<T> taskSource,
                               final LoadingIndicator loadingIndicator) {
        return this.execute(taskSource, loadingIndicator, DEFAULT_TASK);
    }

    public <T> boolean execute(final CancellableTaskSource<T> taskSource, String tag) {
        return this.execute(taskSource, LoadingIndicator.Show, tag);
    }

    /*
     * @return whether the task has been executed
     */
    public <T> boolean execute(final CancellableTaskSource<T> taskSource,
                            final LoadingIndicator loadingIndicator, String tag) {
        return taskExecutor.execute(new LoadingTaskSource<T>(taskSource, loadingIndicator, tag), tag);
    }

    public enum LoadingIndicator {
        Hide, Show
    }

    private class LoadingTaskSource<T> implements CancellableTaskSource<T> {

        private final CancellableTaskSource<T> taskSource;

        private final LoadingIndicator loadingIndicator;

        private final String tag;

        private LoadingTaskSource(CancellableTaskSource<T> taskSource, LoadingIndicator loadingIndicator, String tag) {
            this.taskSource = taskSource;
            this.loadingIndicator = loadingIndicator;
            this.tag = tag;
        }

        @Override
        public Task<T> getTask(final CancelToken cancel) {
            if (loadingIndicator == LoadingIndicator.Show) {
                submitTaskSource(LoadingTaskSource.this);
            }

            return taskSource
                .getTask(cancel)
                .continueWithTask(new Continuation<T, Task<T>>() {
                    @Override
                    public Task<T> then(Task<T> task) throws Exception {
                        if (loadingIndicator == LoadingIndicator.Show) {
                            finishTaskSource(LoadingTaskSource.this);
                        }
                        return task;
                    }
                });
        }
    }

    protected abstract class ActionTaskSource<T> implements CancellableTaskSource<T> {

        protected final String action;

        protected final ArrayMap<String, Object> args = new ArrayMap<>();

        public ActionTaskSource(String action, Object... entry) {
            this.action = action;
            for (int i = 0; i < entry.length - 1; i += 2) {
                this.args.put(String.valueOf(entry[i]), entry[i+1]);
            }
        }

        public abstract Task<T> act(CancelToken cancel);

        protected void onCancelled() {
        }

        protected void onFailure(Throwable t) {
            Log.e("TaskPresenter", this + ":execute error", t);
        }

        protected void onSuccess(T result) {
        }

        @Override
        public Task<T> getTask(final CancelToken cancelToken) {
            Task<T> task = act(cancelToken);
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
            }, Task.UI_THREAD_EXECUTOR).continueWithTask(
                // fire acted callback
                new Continuation<T, Task<T>>() {
                    @Override
                    public Task<T> then(Task<T> task) throws Exception {
                        if (task.isCancelled()) {
                            fireActed(ActionListener.ActionResult.cancelled(action).args(args));
                        } else if (task.isFaulted()) {
                            fireActed(ActionListener.ActionResult.failed(action, task.getError()).args(args));
                        } else {
                            fireActed(ActionListener.ActionResult.succeeded(action).args(args));
                        }
                        return task;
                    }
                }
            );
        }
    }

}
