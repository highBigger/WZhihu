package will.wzhihu.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import will.wzhihu.R;
import will.wzhihu.binder.ToolbarNavigationClickBinder;
import will.wzhihu.common.activity.BindingActivity;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.widget.WToolbar;
import will.wzhihu.main.binder.MainBinder;
import will.wzhihu.main.presenter.MainPresenter;

public class MainActivity extends BindingActivity {
    @Bind(R.id.toolbar)
    WToolbar toolbar;

    @Bind(R.id.list)
    RecyclerView recyclerView;

    private MainPresenter mainPresenter;
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void prepareBinder(View view, CompositeBinder binder) {
        ButterKnife.bind(this);
        mainPresenter = new MainPresenter();
        binder.add(new ToolbarNavigationClickBinder(toolbar, this));
        binder.add(new MainBinder(this, mainPresenter, recyclerView));
        mainPresenter.loadLatest();
    }
}
