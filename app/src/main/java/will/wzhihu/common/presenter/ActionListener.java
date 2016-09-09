package will.wzhihu.common.presenter;


import android.support.v4.util.ArrayMap;

import java.util.Map;

public interface ActionListener {

    void onActed(ActionResult actionResult);

    enum State {
        SUCCEEDED {
            @Override
            public boolean isSucceeded() {
                return true;
            }
        },
        FAILED,
        CANCELLED;

        public boolean isSucceeded() {
            return false;
        }
    }

    class ActionResult {

        public final String action;

        public final State state;

        public final ArrayMap<String, Object> args = new ArrayMap<>();

        public final Throwable failureCause;

        private ActionResult(String action, State state, Throwable failureCause) {
            this.action = action;
            this.state = state;
            this.failureCause = failureCause;
        }

        public <T> ActionResult arg(String key, T value) {
            args.put(key, value);
            return this;
        }

        public <T> ActionResult args(Map<String, Object> args) {
            this.args.putAll(args);
            return this;
        }

        public <T> ActionResult args(Object... entry) {
            for (int i = 0; i < entry.length - 1; i += 2) {
                args.put(String.valueOf(entry[i]), entry[i+1]);
            }
            return this;
        }

        public <T> T get(String key, Class<T> type) {
            Object value = args.get(key);
            if (value != null && type.isInstance(value)) {
                return (T) value;
            }
            return null;
        }

        public Object get(String key) {
            return args.get(key);
        }

        public static ActionResult succeeded(String action) {
            return new ActionResult(action, State.SUCCEEDED, null);
        }

        public static ActionResult failed(String action, Throwable failureCause) {
            return new ActionResult(action, State.FAILED, failureCause);
        }

        public static ActionResult cancelled(String action) {
            return new ActionResult(action, State.CANCELLED, null);
        }
    }
}
