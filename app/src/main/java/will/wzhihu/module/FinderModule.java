package will.wzhihu.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import will.wzhihu.main.StoryFinder;

/**
 * @author wendeping
 */

@Module
public class FinderModule {

    @Singleton
    @Provides
    public StoryFinder providesStoryFinder() {
        return new StoryFinder();
    }
}
