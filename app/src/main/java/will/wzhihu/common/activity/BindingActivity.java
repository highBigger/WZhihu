package will.wzhihu.common.activity;

import android.view.View;

import will.wzhihu.common.binder.CompositeBinder;

/**
 * @author wendeping
 */
public abstract class BindingActivity extends BaseActivity {
    private CompositeBinder mBinder;

    @Override
    protected void initView(View contentView) {
        mBinder = new CompositeBinder();
        prepareBinder(contentView, mBinder);
        mBinder.bind();

        updatePresenters();
    }

    protected abstract void prepareBinder(View view, final CompositeBinder binder);


    protected abstract void updatePresenters();


    @Override
    public void onDestroy() {
        mBinder.unbind();
        super.onDestroy();
    }
}
