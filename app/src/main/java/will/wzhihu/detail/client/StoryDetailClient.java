package will.wzhihu.detail.client;

import rx.Observable;
import will.wzhihu.detail.StoryDetail;

/**
 * @author wendeping
 */
public interface StoryDetailClient {
    Observable<StoryDetail> getStory(String storyId);
}
