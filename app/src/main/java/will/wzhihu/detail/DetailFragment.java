package will.wzhihu.detail;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import will.wzhihu.R;
import will.wzhihu.common.fragment.BaseFragment;
import will.wzhihu.common.widget.WToolbar;
import will.wzhihu.common.widget.WrapWebView;
import will.wzhihu.main.model.Story;

/**
 * Created by taoming on 2016/9/9.
 */
public class DetailFragment extends BaseFragment {

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
    protected void initView() {

    }

    @Override
    protected void initData() {
        Story story = getArguments().getParcelable(DetailActivity.PARAMS_STORY);
        toolbar.setHeaderTitle(story.title);
    }
}
