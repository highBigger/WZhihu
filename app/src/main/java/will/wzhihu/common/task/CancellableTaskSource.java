package will.wzhihu.common.task;

import bolts.Task;

public interface CancellableTaskSource<T> {
    Task<T> getTask(CancelToken cancel);
}

