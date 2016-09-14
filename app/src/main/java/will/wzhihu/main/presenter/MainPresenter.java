package will.wzhihu.main.presenter;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import will.wzhihu.WApplication;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.presenter.RecyclerPresenter;
import will.wzhihu.common.rxjava.LoadingPresenterSubscriber;
import will.wzhihu.main.StoryFinder;
import will.wzhihu.main.model.Latest;
import will.wzhihu.main.model.Stories;
import will.wzhihu.main.model.Story;

/**
 * @author wendeping
 */
public class MainPresenter extends RecyclerPresenter<Story> {
    private static final String TAG = "MainPresenter";

    @Inject
    StoryFinder storyFinder;

    private List<Story> topStories;

    private String currentDate;

    private boolean enableRefresh = false;

    public MainPresenter() {
        WApplication.getInjector().inject(this);
    }

    public List<Story> getTopStories() {
        return topStories;
    }

    private void setTopStories(List<Story> topStories) {
        if (null != topStories) {
            for (Story story : topStories) {
                story.setItemType(Story.ITEM_TYPE_TOP_STORY);
            }
        }
        this.topStories = topStories;
        firePropertyChange("topStories");
    }

    public boolean getEnableRefresh() {
        return enableRefresh;
    }

    private void setEnableRefresh(boolean enable) {
        if (enableRefresh != enable) {
            enableRefresh = enable;
            firePropertyChange("enableRefresh");
        }
    }

    public void refresh() {
        loadLatest();
    }

    public void loadLatest() {
        if (getLoading()) {
            return;
        }

        setLoading(true);
        storyFinder.getLatest().observeOn(AndroidSchedulers.mainThread()).map(new Func1<Latest, List<Story>>() {
            @Override
            public List<Story> call(Latest latest) {
                Log.d(TAG, latest.toString());
                currentDate = latest.date;
                setTopStories(latest.topStories);
                return latest.stories;
            }
        }).subscribe(new LoadingPresenterSubscriber<List<Story>>(this) {
            @Override
            public void onNext(List<Story> stories) {
                setItems(stories);
                setEnableRefresh(false);
                Log.d(TAG, "latest story size %d ", stories.size());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.d(TAG, "error", e);
                setEnableRefresh(true);
            }
        });;
    }

    @Override
    public void loadAfter() {
        if (getLoading()) {
            return;
        }

        setLoading(true);
        storyFinder.getStories(currentDate).observeOn(AndroidSchedulers.mainThread()).subscribe(new LoadingPresenterSubscriber<Stories>(this) {
            @Override
            public void onNext(Stories stories) {
                currentDate = stories.date;
                addAll(stories.stories);
            }
        });
    }

    @Override
    public String[] getFeedItemTypes() {
        return new String[] {Story.ITEM_TYPE_STORY, Story.ITEM_TYPE_DATE, Story.ITEM_TYPE_TOP_STORY};
    }
}
