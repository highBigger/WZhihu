package will.wzhihu.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import will.wzhihu.R;
import will.wzhihu.common.activity.BaseActivity;

/**
 * Created by taoming on 2016/9/9.
 */
public class DtailActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Fragment detail_fragment = DetailFragment.instantiate(this, "detail_fragment", getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, detail_fragment).commit();
    }
}
