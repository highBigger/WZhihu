package will.wzhihu.common.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


public abstract class BaseFragment extends Fragment {

    protected Activity mContent;
    protected Bundle mBundle;
    protected View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getActivity();
        mBundle = getArguments();
        ButterKnife.bind(mContent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        initView();
        initData();
        return mRootView;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();
}
