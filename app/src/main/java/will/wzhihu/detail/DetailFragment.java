package will.wzhihu.detail;

import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import will.wzhihu.R;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.fragment.BindingFragment;
import will.wzhihu.common.widget.WToolbar;
import will.wzhihu.common.widget.WrapWebView;
import will.wzhihu.main.model.Story;

/**
 * Created by taoming on 2016/9/9.
 */
public class DetailFragment extends BindingFragment {
    @Bind(R.id.toolbar)
    WToolbar toolbar;

    @Bind(R.id.image)
    SimpleDraweeView image;

    @Bind(R.id.webView)
    WrapWebView webView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void prepareBinder(View view, CompositeBinder binder) {
        Story story = getArguments().getParcelable(DetailActivity.PARAMS_STORY);
        toolbar.setHeaderTitle(story.title);
    }
}
