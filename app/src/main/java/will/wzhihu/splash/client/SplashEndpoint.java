package will.wzhihu.splash.client;

import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;
import will.wzhihu.splash.model.Splash;

/**
 * @author wendeping
 */
public interface SplashEndpoint {
    @GET("/api/4/start-image/{size}")
    Observable<Splash> getSplash(@Path("size") String size);
}
