package will.wzhihu.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import will.wzhihu.detail.StoryDetailFinder;
import will.wzhihu.main.StoryListFinder;

/**
 * @author wendeping
 */

@Module
public class FinderModule {

    @Singleton
    @Provides
    public StoryListFinder providesStoryFinder() {
        return new StoryListFinder();
    }

    @Singleton
    @Provides
    public StoryDetailFinder providesStoryDetailFinder() {
        return new StoryDetailFinder();
    }
}
