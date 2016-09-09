package will.wzhihu.splash.client;

import rx.Observable;
import will.wzhihu.splash.model.Splash;

/**
 * @author wendeping
 */
public interface SplashClient {
    Observable<Splash> getSplash(int width, int height);
}
