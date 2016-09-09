package will.wzhihu.common.task;

import android.support.v4.util.Pair;

import java.util.Arrays;

import bolts.Continuation;
import bolts.Task;

/**
 * @author dusiyu
 */
public class Tasks {

    public static <T> Task<T> cancellable(Task<T> task, final CancelToken token) {
        return task.continueWithTask(
            new Continuation<T, Task<T>>() {
                @Override
                public Task<T> then(Task<T> task) throws Exception {
                    if (token.isCancelled()) {
                        return Task.cancelled();
                    } else {
                        return task;
                    }
                }
            }
        );
    }

    public static <R1, R2> Task<Pair<R1, R2>> combine(final Task<R1> task1, final Task<R2> task2) {
        return Task.whenAll(Arrays.asList(task1, task2)).onSuccess(new Continuation<Void, Pair<R1, R2>>() {
            @Override
            public Pair<R1, R2> then(Task<Void> task) throws Exception {
                return new Pair<>(task1.getResult(), task2.getResult());
            }
        });
    }

    public static <T> void delegateTo(Task<T> task, Task<T>.TaskCompletionSource source) {
        if (task.isFaulted()) {
            source.setError(task.getError());
        } else if (task.isCancelled()) {
            source.setCancelled();
        } else {
            source.setResult(task.getResult());
        }
    }
}
