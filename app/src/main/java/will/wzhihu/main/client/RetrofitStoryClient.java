package will.wzhihu.main.client;

import javax.inject.Inject;
import rx.Observable;
import rx.schedulers.Schedulers;
import will.wzhihu.common.client.RetrofitBaseClient;
import will.wzhihu.main.model.Latest;
import will.wzhihu.main.model.Stories;

/**
 * @author wendeping
 */
public class RetrofitStoryClient extends RetrofitBaseClient<StoryEndpoint> implements StoryClient {

    @Inject
    public RetrofitStoryClient() {

    }

    @Override
    public Observable<Latest> getLatest() {
        return getEndpoint().getLatest().subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Stories> getBefore(String date) {
        return getEndpoint().getStoriesBefore(date).subscribeOn(Schedulers.io());
    }
}
