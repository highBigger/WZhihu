package will.wzhihu.detail.client;

import rx.Observable;
import rx.schedulers.Schedulers;
import will.wzhihu.common.client.RetrofitBaseClient;
import will.wzhihu.detail.StoryDetail;

/**
 * @author wendeping
 */
public class RetrofitStoryDetailClient extends RetrofitBaseClient<StoryDetailEndpoint> implements StoryDetailClient {

    @Override
    public Observable<StoryDetail> getStory(String storyId) {
        return getEndpoint().getStory(storyId).observeOn(Schedulers.io());
    }
}
