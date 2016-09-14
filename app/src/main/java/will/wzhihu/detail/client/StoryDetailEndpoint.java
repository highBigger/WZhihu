package will.wzhihu.detail.client;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import will.wzhihu.detail.StoryDetail;

/**
 * @author wendeping
 */
public interface StoryDetailEndpoint {
    @GET("/api/4/news/{storyId}")
    Observable<StoryDetail> getStory(@Path("storyId") String storyId);
}
