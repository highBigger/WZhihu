package will.wzhihu.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;

public class ActivityStarter {

    private final Class<? extends Activity> mActivity;

    private final Bundle mArgs;

    private Integer mFlags;

    private boolean mWithParent;

    private Uri mData;

    public ActivityStarter(Class<? extends Activity> activity) {
        this(activity, new Bundle());
    }

    public ActivityStarter(Class<? extends Activity> activity, Bundle args) {
        this.mActivity = activity;
        this.mArgs = args;
    }

    public ActivityStarter clearTask() {
        if (mFlags != null) {
            mFlags |= Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK;
        } else {
            mFlags = Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK;
        }
        return this;
    }

    public ActivityStarter flags(int flags) {
        this.mFlags = flags;
        return this;
    }

    public ActivityStarter data(Uri data) {
        this.mData = data;
        return this;
    }

    public ActivityStarter withParent(boolean withParent) {
        this.mWithParent = withParent;
        return this;
    }


    public Intent build(Context context) {
        Bundle extra = mArgs;
        if (extra == null) {
            extra = new Bundle();
        }
        Intent intent = new Intent(context, mActivity);
        intent.putExtras(extra);
        if (mFlags != null) {
            intent.addFlags(mFlags);
        }
        return intent;
    }

    public void start(Context context) {
        Bundle extra = mArgs;
        if (extra == null) {
            extra = new Bundle();
        }
        Intent intent = new Intent(context, mActivity);
        intent.putExtras(extra);
        if (mFlags != null) {
            intent.setFlags(mFlags);
        }
        if (mData != null) {
            intent.setData(mData);
        }
        if (mWithParent) {
            TaskStackBuilder.create(context).addNextIntentWithParentStack(intent).startActivities();
        } else {
            context.startActivity(intent);
        }
    }

    public void startForResult(Activity activity, int requestCode) {
        Bundle extra = mArgs;
        if (extra == null) {
            extra = new Bundle();
        }
        Intent intent = new Intent(activity, this.mActivity);
        intent.putExtras(extra);
        if (mFlags != null) {
            intent.addFlags(mFlags);
        }
        activity.startActivityForResult(intent, requestCode);
    }

}
