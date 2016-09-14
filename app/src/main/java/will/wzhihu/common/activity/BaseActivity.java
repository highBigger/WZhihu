package will.wzhihu.common.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;

/**
 * @author wendeping
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected  View contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = LayoutInflater.from(this).inflate(getContentLayoutId(), null);
        setContentView(contentView);
        ButterKnife.bind(this);
        initView(contentView);
    }

    protected abstract int getContentLayoutId();
    protected abstract void initView(View contentView);
}
