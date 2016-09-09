package will.wzhihu.common.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import will.wzhihu.common.binder.CompositeBinder;

/**
 * @author wendeping
 */
public abstract class BindingActivity extends BaseActivity {
    private CompositeBinder mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(this).inflate(getContentLayoutId(), null);
        setContentView(contentView);

        mBinder = new CompositeBinder();
        prepareBinder(contentView, mBinder);
        mBinder.bind();
        updatePresenters();
    }

    protected abstract int getContentLayoutId();

    protected abstract void prepareBinder(View view, final CompositeBinder binder);


    protected void updatePresenters() {
    }

    @Override
    public void onDestroy() {
        mBinder.unbind();
        super.onDestroy();
    }
}
