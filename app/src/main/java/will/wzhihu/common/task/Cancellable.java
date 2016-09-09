package will.wzhihu.common.task;

public interface Cancellable {

    boolean isCancelled();

    boolean cancel(boolean mayInterruptIfRunning);

}
