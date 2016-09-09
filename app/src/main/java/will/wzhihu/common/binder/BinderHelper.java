package will.wzhihu.common.binder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;
import will.wzhihu.R;
import will.wzhihu.common.helper.ThreadHelper;
import will.wzhihu.common.log.Log;

public class BinderHelper {

    public static final String TAG = "BinderHelper";

    private static WeakHashMap<Object, Set<Binder>> RegisteredBinders = new WeakHashMap<>();

    public static void bind(View view, Binder binder) {
        ThreadHelper.checkMainThread("bind should be invoked from main thread");
        Binder oldBinder = getBinder(view);
        if (oldBinder != binder) {
            if (oldBinder != null) {
                oldBinder.unbind();
                unregister(view.getContext(), oldBinder);
            }
            view.setTag(R.id.binder, binder);
            binder.bind();
            register(view.getContext(), binder);
        }
    }


    public static void unbind(View view) {
        ThreadHelper.checkMainThread("unbind should be invoked from main thread");
        Binder old = (Binder) view.getTag(R.id.binder);
        if (old != null) {
            old.unbind();
            unregister(view.getContext(), old);
            view.setTag(R.id.binder, null);
        }
    }

    public static void unbindAll(ViewGroup view) {
        unbind(view);
        for (int i = 0, count = view.getChildCount(); i < count; i++) {
            View child = view.getChildAt(i);
            if (child instanceof ViewGroup) {
                unbindAll((ViewGroup) child);
            } else {
                unbind(child);
            }
        }
    }

    public static Binder getBinder(View view) {
        return (Binder) view.getTag(R.id.binder);
    }

    public static void unbindAll(Context context) {
        ThreadHelper.checkMainThread("unbindAll should be invoked from main thread");
        Log.d(TAG, "unbind all for context %s", context);
        Set<Binder> binders = RegisteredBinders.remove(context);
        if (binders != null) {
            for (Binder binder : binders) {
                Log.d(TAG, "unbound %s from %s", binder, context);
                binder.unbind();
            }
        }
    }

    private static void register(Context context, Binder binder) {
        Set<Binder> binders = RegisteredBinders.get(context);
        if (binders == null) {
            binders = new HashSet<>();
            RegisteredBinders.put(context, binders);
        }
        binders.add(binder);
        Log.d(TAG, "registered %s on %s", binder, context);
    }

    private static void unregister(Context context, Binder binder) {
        Set<Binder> binders = RegisteredBinders.get(context);
        if (binders != null) {
            binders.remove(binder);
            Log.d(TAG, "unregistered %s on %s", binder, context);
        }
    }
}
