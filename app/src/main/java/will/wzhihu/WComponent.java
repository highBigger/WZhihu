package will.wzhihu;

import javax.inject.Singleton;

import dagger.Component;
import will.wzhihu.splash.SplashActivity;

/**
 * @author wendeping
 */
@Singleton
@Component(
    modules = {
        ClientModule.class,
    }
)
public interface WComponent {
    void inject(SplashActivity splashActivity);
}
