package will.wzhihu.module;

import javax.inject.Singleton;

import dagger.Component;
import will.wzhihu.main.presenter.MainPresenter;
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
    void inject(MainPresenter mainPresenter);
}
