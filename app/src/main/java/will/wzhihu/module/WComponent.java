package will.wzhihu.module;

import javax.inject.Singleton;

import dagger.Component;
import will.wzhihu.main.StoryFinder;
import will.wzhihu.main.presenter.MainPresenter;
import will.wzhihu.main.presenter.StoryPresenter;
import will.wzhihu.splash.SplashActivity;

/**
 * @author wendeping
 */
@Singleton
@Component(
    modules = {
        ClientModule.class,
        StoreModule.class,
        FinderModule.class
    }
)
public interface WComponent {
    void inject(SplashActivity splashActivity);
    void inject(MainPresenter mainPresenter);
    void inject(StoryFinder storyFinder);
    void inject(StoryPresenter storyPresenter);
}
