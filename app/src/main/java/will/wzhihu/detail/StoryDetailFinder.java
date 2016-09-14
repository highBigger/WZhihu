package will.wzhihu.detail;

import javax.inject.Inject;
import rx.Observable;
import will.wzhihu.WApplication;
import will.wzhihu.common.store.StoryStore;
import will.wzhihu.detail.client.StoryDetailClient;

/**
 * @author wendeping
 */
public class StoryDetailFinder {
    @Inject
    StoryDetailClient storyDetailClient;

    @Inject
    StoryStore storyStore;

    public StoryDetailFinder() {
        WApplication.getInjector().inject(this);
    }

    public Observable<StoryDetail> find(long storyId) {
        return null;
    }
}
