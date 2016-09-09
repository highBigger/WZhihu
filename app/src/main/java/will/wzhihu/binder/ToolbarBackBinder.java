package will.wzhihu.binder;

import android.view.View;

import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.fragment.BaseFragment;
import will.wzhihu.common.widget.WToolbar;

public class ToolbarBackBinder extends CompositeBinder {
    private WToolbar mWToolbar;
    private BaseFragment mFragment;

    public ToolbarBackBinder(final BaseFragment fragment, WToolbar toolbar) {
        this.mWToolbar = toolbar;
        this.mFragment = fragment;
        this.add(new Binder() {
            @Override
            public void bind() {
                mWToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFragment.getActivity().onBackPressed();
                    }
                });
            }

            @Override
            public void unbind() {
                mWToolbar.setNavigationOnClickListener(null);
            }
        });
    }
}
