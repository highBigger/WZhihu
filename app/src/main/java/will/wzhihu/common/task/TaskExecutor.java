package will.wzhihu.common.task;

import java.util.LinkedList;
import java.util.List;

import bolts.Continuation;
import bolts.Task;
import will.wzhihu.common.utils.StringUtils;

/**
 * @author dusiyu
 */
public class TaskExecutor {

    private TaskFilter mFilter = SingleTaskPerTag;

    private LinkedList<TaskHolder> mQueue = new LinkedList<>();

    private final Object LOCK = new Object();

    public TaskExecutor(TaskFilter filter) {
        this.mFilter = filter;
    }

    private Continuation moveOn = new Continuation() {
        @Override
        public Object then(Task task) throws Exception {
            synchronized (LOCK) {
                mQueue.poll();
                executeNext();
                return task;
            }
        }
    };

    // guarded by LOCK
    private void executeNext() {
        if (!mQueue.isEmpty()) {
            TaskHolder holder = mQueue.peek();
            if (holder.cancelToken == null) {
                holder.cancelToken = new CancelToken();
                holder.task.getTask(holder.cancelToken)
                           .continueWith(moveOn);
            }
        }
    }

    public <T> boolean execute(CancellableTaskSource<T> task, String tag) {
        TaskHolder<T> holder = new TaskHolder<>(task, tag);
        synchronized (LOCK) {
            boolean retained = mFilter.filter(mQueue, holder);
            if (retained) {
                boolean execute = mQueue.isEmpty();
                mQueue.offer(holder);
                if (execute) {
                    executeNext();
                }
            }
            return retained;
        }
    }

    public <T> void ensureExecuteNext(CancellableTaskSource<T> task, String tag) {
        synchronized (LOCK) {
            if (mQueue.isEmpty()) {
                execute(task, tag);
            } else {
                TaskHolder<T> holder = new TaskHolder<>(task, tag);
                mQueue.add(1, holder);
            }
        }
    }

    public <T> void ensureExecute(CancellableTaskSource<T> task, String tag) {
        synchronized (LOCK) {
            if (mQueue.isEmpty()) {
                execute(task, tag);
            } else {
                TaskHolder<T> holder = new TaskHolder<>(task, tag);
                mQueue.add(holder);
            }
        }

    }

    public void cancel() {
        synchronized (LOCK) {
            for (TaskHolder holder : mQueue) {
                if (holder.cancelToken != null) {
                    holder.cancelToken.cancel(true);
                }
            }
            mQueue.clear();
        }
    }

    public interface TaskFilter {
        boolean filter(List<TaskHolder> queue, TaskHolder newTask);
    }

    public static class TaskHolder<T> {
        public CancellableTaskSource<T> task;
        public String tag;
        private CancelToken cancelToken;

        public TaskHolder(CancellableTaskSource<T> task, String tag) {
            this.task = task;
            this.tag = tag;
        }
    }

    public static TaskFilter SingleTaskPerTag = new TaskFilter() {
        @Override
        public boolean filter(List<TaskHolder> queue, TaskHolder newTask) {
            for (TaskHolder holder : queue) {
                if (StringUtils.isEqual(holder.tag, newTask.tag)) {
                    return false;
                }
            }
            return true;
        }
    };
}
