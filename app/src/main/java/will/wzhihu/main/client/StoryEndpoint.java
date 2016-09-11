package will.wzhihu.main.client;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import will.wzhihu.main.model.Latest;
import will.wzhihu.main.model.Stories;

/**
 * @author wendeping
 */
public interface StoryEndpoint {
    @GET("/api/4/news/latest")
    Observable<Latest> getLatest();

    @GET("/api/4/news/before/{date}")
    Observable<Stories> getStoriesBefore(@Path("date") String date);
}
