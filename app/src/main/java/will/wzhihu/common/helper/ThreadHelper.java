package will.wzhihu.common.helper;

import android.os.Looper;

/**
 * @author dusiyu
 */
public class ThreadHelper {
    public static void checkMainThread(String message) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new RuntimeException(message);
        }
    }
}
