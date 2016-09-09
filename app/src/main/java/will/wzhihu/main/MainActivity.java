package will.wzhihu.main;

<<<<<<< HEAD
import android.os.Bundle;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import will.wzhihu.Injectors;
import will.wzhihu.R;
import will.wzhihu.common.activity.ActivityStarter;
import will.wzhihu.common.activity.BaseActivity;
import will.wzhihu.detail.DtailActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import will.wzhihu.R;
import will.wzhihu.binder.ToolbarNavigationClickBinder;
import will.wzhihu.common.activity.BindingActivity;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.widget.WToolbar;

public class MainActivity extends BindingActivity {
    @Bind(R.id.toolbar)
    WToolbar toolbar;

    @Bind(R.id.list)
    RecyclerView recyclerView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    protected void prepareBinder(View view, CompositeBinder binder) {
        ButterKnife.bind(this);
        binder.add(new ToolbarNavigationClickBinder(toolbar, this));
    }
}
