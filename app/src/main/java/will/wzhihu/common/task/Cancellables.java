package will.wzhihu.common.task;

import com.squareup.okhttp.Call;

public class Cancellables {

    public static Cancellable wrap(Call call) {
        return new CallCancellable(call);
    }

    public static class CallCancellable implements Cancellable {
        private final Call call;

        public CallCancellable(Call call) {
            this.call = call;
        }

        @Override
        public boolean isCancelled() {
            return call.isCanceled();
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            call.cancel();
            return true;
        }
    }
}
