package will.wzhihu.detail;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import will.wzhihu.WApplication;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.store.StoryStore;
import will.wzhihu.detail.client.StoryDetailClient;

/**
 * @author wendeping
 */
public class StoryDetailFinder {
    private static final String TAG = "StoryDetailFinder";

    @Inject
    StoryDetailClient storyDetailClient;

    @Inject
    StoryStore storyStore;

    public StoryDetailFinder() {
        WApplication.getInjector().inject(this);
    }

    public Observable<StoryDetail> find(final String storyId) {
        return Observable.fromCallable(new Callable<StoryDetail>() {
            @Override
            public StoryDetail call() throws Exception {
                return storyStore.getStoryDetail(storyId);
            }
        }).observeOn(Schedulers.io()).flatMap(new Func1<StoryDetail, Observable<StoryDetail>>() {
            @Override
            public Observable<StoryDetail> call(StoryDetail detail) {
                if (null != detail) {
                    Log.d(TAG, "load story from db success");
                    return Observable.just(detail);
                }

                return getRemoteStory(storyId);
            }
        });
    }

    private Observable<StoryDetail> getRemoteStory(String storyId) {
        return storyDetailClient.getStory(storyId).map(new Func1<StoryDetail, StoryDetail>() {
            @Override
            public StoryDetail call(StoryDetail detail) {
                storyStore.putStoryDetail(detail);
                Log.d(TAG, "put remote story to db");
                return detail;
            }
        });
    }
}
