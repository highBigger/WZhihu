package will.wzhihu.common.task;

import bolts.Continuation;
import bolts.Task;

/**
 * @author dusiyu
 */
public class DelegateContinuation<T> implements Continuation<T, Task<T>> {

    private final Task<T>.TaskCompletionSource mTaskCompletionSource;

    public DelegateContinuation(Task<T>.TaskCompletionSource
                                    taskCompletionSource) {
        mTaskCompletionSource = taskCompletionSource;
    }

    @Override
    public Task<T> then(Task<T> task) throws Exception {
        Tasks.delegateTo(task, mTaskCompletionSource);
        return task;
    }
}
