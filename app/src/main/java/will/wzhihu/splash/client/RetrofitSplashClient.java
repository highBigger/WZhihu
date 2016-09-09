package will.wzhihu.splash.client;

import javax.inject.Inject;
import javax.inject.Singleton;
import rx.Observable;
import rx.schedulers.Schedulers;
import will.wzhihu.WApplication;
import will.wzhihu.common.client.RetrofitBaseClient;
import will.wzhihu.common.log.Log;
import will.wzhihu.splash.SplashSizeUtil;
import will.wzhihu.splash.model.Splash;

/**
 * @author wendeping
 */

@Singleton
public class RetrofitSplashClient extends RetrofitBaseClient<SplashEndpoint> implements SplashClient{
    private static final String TAG = "RetrofitSplashClient";

    @Inject
    public RetrofitSplashClient() {

    }

    @Override
    public Observable<Splash> getSplash() {
        String sizeString = SplashSizeUtil.getSizeString(WApplication.getInstance());
        Log.d(TAG, sizeString);
        return getEndpoint().getSplash(sizeString)
            .subscribeOn(Schedulers.io());
    }
}
