package will.wzhihu.main.binder;

import android.support.v4.view.ViewPager;
import android.view.View;

import will.wzhihu.R;
import will.wzhihu.common.binder.PresenterBinder;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.common.widget.CircleIndicator;
import will.wzhihu.main.adapter.TopStoryAdapter;
import will.wzhihu.main.presenter.MainPresenter;

/**
 * @author wendeping
 */
public class TopStoryBinder extends PresenterBinder<MainPresenter> {
    public static final String TAG = "TopStoryBinder";

    private TopStoryAdapter topStoryAdapter;
    public TopStoryBinder(final View view, final MainPresenter mainPresenter) {
        super(mainPresenter);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final CircleIndicator circleIndicator = (CircleIndicator) view.findViewById(R.id.indicator);
        circleIndicator.setViewPager(viewPager);
        initializeAndAdd("topStories", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                if (null == topStoryAdapter) {
                    Log.d(TAG, "adapter is null");
                    topStoryAdapter = new TopStoryAdapter(mainPresenter.getTopStories());
                    viewPager.setAdapter(topStoryAdapter);
                } else {
                    topStoryAdapter.setStories(mainPresenter.getTopStories());
                    Log.d(TAG, "adapter is not null");
                }
                circleIndicator.notifyDataSetChange();
            }
        });
    }
}
