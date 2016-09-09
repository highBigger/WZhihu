package will.wzhihu.splash.client;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.schedulers.Schedulers;
import will.wzhihu.common.client.RetrofitBaseClient;
import will.wzhihu.splash.model.Splash;

/**
 * @author wendeping
 */

@Singleton
public class RetrofitSplashClient extends RetrofitBaseClient<SplashEndpoint> implements SplashClient{
    @Inject
    public RetrofitSplashClient() {

    }

    private static final String REG = "%1$d*%2$d";

    @Override
    public Observable<Splash> getSplash(int width, int height) {
        return getEndpoint().getSplash(String.format(REG, width, height))
            .subscribeOn(Schedulers.io());
    }
}
