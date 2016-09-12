package will.wzhihu.main.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import will.wzhihu.WApplication;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.presenter.RecyclerPresenter;
import will.wzhihu.common.rxjava.LoadingPresenterSubscriber;
import will.wzhihu.common.utils.CollectionUtils;
import will.wzhihu.main.client.StoryClient;
import will.wzhihu.main.model.Latest;
import will.wzhihu.main.model.Stories;
import will.wzhihu.main.model.Story;

/**
 * @author wendeping
 */
public class MainPresenter extends RecyclerPresenter<Story> {
    private static final String TAG = "MainPresenter";

    @Inject
    StoryClient storyClient;

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
        storyClient.getLatest().observeOn(AndroidSchedulers.mainThread()).flatMap(new Func1<Latest, Observable<List<Story>>>() {
            @Override
            public Observable<List<Story>> call(Latest latest) {
                Log.d(TAG, latest.toString());
                return Observable.just(ConvertLatest(latest));
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
        });
    }

    @Override
    public void loadAfter() {
        if (getLoading()) {
            return;
        }

        setLoading(true);
        storyClient.getBefore(currentDate).observeOn(AndroidSchedulers.mainThread()).flatMap(new Func1<Stories, Observable<List<Story>>>() {
            @Override
            public Observable<List<Story>> call(Stories stories) {
                Log.d(TAG, "get before %s story", stories.date);
                return Observable.just(convertStories(stories.date, stories.stories));
            }
        }).subscribe(new LoadingPresenterSubscriber<List<Story>>(this) {

            @Override
            public void onNext(List<Story> stories) {
                addAll(stories);
            }
        });
    }

    private List<Story> ConvertLatest(Latest latest) {
        if (null == latest) {
            return null;
        }
        List<Story> resultStories = new ArrayList<>();

        if (!CollectionUtils.isEmpty(latest.topStories)) {
            Story topStory = new Story();
            topStory.setItemType(Story.ITEM_TYPE_TOP_STORY);
            topStory.date = latest.date;
            resultStories.add(topStory);
            setTopStories(latest.topStories);
        } else {
            setTopStories(null);
        }

        resultStories.addAll(convertStories(latest.date, latest.stories));
        return resultStories;
    }

    private List<Story> convertStories(String date, List<Story> stories) {
        List<Story> resultStories = new ArrayList<>();
        Story dateStory = new Story();
        dateStory.date = date;
        dateStory.setItemType(Story.ITEM_TYPE_DATE);
        resultStories.add(dateStory);

        if (!CollectionUtils.isEmpty(stories)) {
            for (Story story : stories) {
                story.date = date;
                resultStories.add(story);
            }
        }

        currentDate = date;
        return resultStories;
    }

    @Override
    public String[] getFeedItemTypes() {
        return new String[] {Story.ITEM_TYPE_STORY, Story.ITEM_TYPE_DATE, Story.ITEM_TYPE_TOP_STORY};
    }
}
