package will.wzhihu.main;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import will.wzhihu.R;
import will.wzhihu.common.activity.BindingActivity;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.widget.WToolbar;
import will.wzhihu.main.binder.MainListBinder;
import will.wzhihu.main.binder.MainToolbarBinder;
import will.wzhihu.main.binder.RefreshBinder;
import will.wzhihu.main.presenter.MainPresenter;

public class MainActivity extends BindingActivity {
    @Bind(R.id.toolbar)
    WToolbar toolbar;

    @Bind(R.id.list)
    RecyclerView recyclerView;

    @Bind(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    private MainPresenter mainPresenter;
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void prepareBinder(View view, CompositeBinder binder) {
        ButterKnife.bind(this);
        refreshLayout.setEnabled(false);
        mainPresenter = new MainPresenter();
        binder.add(new MainToolbarBinder(toolbar, this, recyclerView, mainPresenter));
        binder.add(new MainListBinder(this, mainPresenter, recyclerView));
        binder.add(new RefreshBinder(refreshLayout, mainPresenter));
        mainPresenter.loadLatest();
    }
}
