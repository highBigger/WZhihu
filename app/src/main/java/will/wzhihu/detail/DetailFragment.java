package will.wzhihu.detail;

import android.view.View;

import butterknife.Bind;
import will.wzhihu.R;
import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.fragment.BindingFragment;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.common.utils.StringUtils;
import will.wzhihu.common.widget.WToolbar;
import will.wzhihu.common.widget.WrapWebView;
import will.wzhihu.detail.binder.WebViewBinder;
import will.wzhihu.detail.presenter.DetailPresenter;
import will.wzhihu.main.binder.StoryBinder;
import will.wzhihu.main.model.Story;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * Created by taoming on 2016/9/9.
 */
public class DetailFragment extends BindingFragment {
    @Bind(R.id.toolbar)
    WToolbar toolbar;

    @Bind(R.id.webView)
    WrapWebView webView;

    private StoryPresenter storyPresenter;

    private DetailPresenter detailPresenter;

    private Story story;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail;
    }

    @Override
    protected void prepareBinder(View view, CompositeBinder binder) {
        story = getArguments().getParcelable(DetailActivity.PARAMS_STORY);
        toolbar.setHeaderTitle(story.title);

        storyPresenter = new StoryPresenter();
        detailPresenter = new DetailPresenter(String.valueOf(story.id));

        binder.add(new StoryBinder(view, storyPresenter, false, false, false));
        binder.add(new WebViewBinder(webView, detailPresenter));
        if (StringUtils.isEmpty(story.image)) {
            binder.add(new Binder() {
                @Override
                public void bind() {
                    detailPresenter.addPropertyChangeListener("image", new PropertyChangeListener() {
                        @Override
                        public void propertyChanged() {
                            if (null != detailPresenter.getImage()) {
                                storyPresenter.setImage(detailPresenter.getImage());
                            }
                        }
                    });
                }

                @Override
                public void unbind() {
                }
            });
        }
    }

    @Override
    protected void updatePresenters() {
        storyPresenter.updateData(0, story);
        detailPresenter.load();
    }
}
