package will.wzhihu.common.fragment;

import butterknife.Bind;
import will.wzhihu.R;
import will.wzhihu.common.widget.WToolbar;

/**
 * Created by taoming on 2016/9/9.
 */
public class DetailFragment extends BaseFragment {

    @Bind(R.id.detail_toolbar)
    WToolbar toolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void initView() {
        toolbar.setHeaderTitle("detailpage");
    }

    @Override
    protected void initData() {

    }
}
