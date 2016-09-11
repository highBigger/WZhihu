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
import will.wzhihu.common.rxjava.BaseSubscriber;
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
        }).subscribe(new BaseSubscriber<List<Story>>() {
            @Override
            public void onNext(List<Story> stories) {
                setItems(stories);
                Log.d(TAG, "latest story size %d ", stories.size());
            }

            @Override
            public void onCompleted() {
                setLoading(false);
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                setLoading(false);
                Log.d(TAG, "error", e);
            }
        });
    }

    public void loadBefore(String date) {
        if (getLoading()) {
            return;
        }

        setLoading(true);
        storyClient.getBefore(date).flatMap(new Func1<Stories, Observable<List<Story>>>() {
            @Override
            public Observable<List<Story>> call(Stories stories) {
                return Observable.just(convertStories(stories.date, stories.stories));
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseSubscriber<List<Story>>() {
            @Override
            public void onNext(List<Story> stories) {
                addAll(stories);
            }

            @Override
            public void onCompleted() {
                setLoading(false);
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
            resultStories.addAll(stories);
        }

        return resultStories;
    }

    @Override
    public String[] getFeedItemTypes() {
        return new String[] {Story.ITEM_TYPE_STORY, Story.ITEM_TYPE_DATE, Story.ITEM_TYPE_TOP_STORY};
    }
}
