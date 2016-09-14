package will.wzhihu.module;

import javax.inject.Singleton;

import dagger.Component;
import will.wzhihu.detail.DetailPresenter;
import will.wzhihu.detail.StoryDetailFinder;
import will.wzhihu.main.StoryListFinder;
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
    void inject(StoryListFinder storyFinder);
    void inject(StoryDetailFinder storyDetailFinder);
    void inject(StoryPresenter storyPresenter);
    void inject(DetailPresenter detailPresenter);
}
