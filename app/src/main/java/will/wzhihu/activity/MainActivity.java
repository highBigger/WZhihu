package will.wzhihu.activity;

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

public class MainActivity extends BaseActivity {

    @Bind(R.id.button1)
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button1)
    public void onCLick(){
        new ActivityStarter(DtailActivity.class).start(this);
    }
}
