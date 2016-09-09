package will.wzhihu.common.task;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import bolts.Continuation;
import bolts.Task;
import will.wzhihu.common.model.Supplier;

/**
 * @author dusiyu
 */
public class CachedAsyncSupplier<T> implements Supplier<Task<T>> {

    private final Supplier<T> mSupplier;

    private AtomicReference<Task<T>.TaskCompletionSource> mSourceReference = new AtomicReference<>();

    public CachedAsyncSupplier(Supplier<T> supplier) {
        mSupplier = supplier;
    }

    @Override
    public Task<T> get() {
        Task<T>.TaskCompletionSource source = mSourceReference.get();
        while(source == null){
            final Task<T>.TaskCompletionSource source0 = source = Task.create();
            if (mSourceReference.compareAndSet(null, source)) {
                Task.callInBackground(new Callable<T>() {
                    @Override
                    public T call() throws Exception {
                        return mSupplier.get();
                    }
                }).continueWith(new Continuation<T, Void>() {
                    @Override
                    public Void then(Task<T> task) throws Exception {
                        Tasks.delegateTo(task, source0);
                        if (task.isFaulted() || task.isCancelled()) {
                            mSourceReference.weakCompareAndSet(source0, null);
                        }
                        return null;
                    }
                });
                break;
            } else {
                source = mSourceReference.get();
            }
        }
        return source.getTask();
    }
}
