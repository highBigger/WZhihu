package will.wzhihu.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import will.wzhihu.WApplication;

public class ToastUtils {

    public static final String TAG = "ToastUtils";

    public static void toast(int msgResId) {
        String msg = WApplication.getInstance().getString(msgResId);
        toast(msg);
    }

    public static void toast(CharSequence msg) {
        toast(msg, Toast.LENGTH_SHORT);
    }

    public static void toast(CharSequence s, int duration) {
        toast(s, duration, false);
    }

    public static void toast(final CharSequence s, final int duration, final boolean cancelable) {
        final Toast toast = Toast.makeText(WApplication.getInstance(), s, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    toast.show();
                }
            });
        }

        if (cancelable) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, duration);
        }
    }
}
