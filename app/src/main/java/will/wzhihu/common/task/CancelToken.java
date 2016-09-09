package will.wzhihu.common.task;

import java.util.concurrent.atomic.AtomicBoolean;

public class CancelToken implements Cancellable {
    private AtomicBoolean cancelled = new AtomicBoolean(false);

    public boolean isCancelled() {
        return cancelled.get();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return !cancelled.getAndSet(true);
    }

    public boolean cancel() {
        return cancel(true);
    }
}
