package will.wzhihu.common.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import will.wzhihu.common.binder.CompositeBinder;

/**
 * @author dusiyu
 */
public abstract class BindingFragment extends BaseFragment {
    private CompositeBinder mBinder;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinder = new CompositeBinder();
        prepareBinder(view, mBinder);
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
