package will.wzhihu;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import will.wzhihu.splash.client.RetrofitSplashClient;
import will.wzhihu.splash.client.SplashClient;

/**
 * @author wendeping
 */
@Module
public class ClientModule {

    @Singleton
    @Provides
    SplashClient provideSplashClient() {
        return new RetrofitSplashClient();
    }
}
