package will.wzhihu.main.client;

import rx.Observable;
import will.wzhihu.main.model.Latest;
import will.wzhihu.main.model.Stories;

/**
 * @author wendeping
 */
public interface StoryClient {
    Observable<Latest> getLatest();

    Observable<Stories> getBefore(String date);
}
