package will.wzhihu.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import will.wzhihu.R;
import will.wzhihu.common.activity.BaseActivity;
import will.wzhihu.main.model.Story;

/**
 * Created by taoming on 2016/9/9.
 */
public class DetailActivity extends BaseActivity {
    public static final String PARAMS_STORY = "params_story";

    public static void start(Story story, Activity activity) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(PARAMS_STORY, story);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_content, new DetailFragment()).commit();
    }
}
