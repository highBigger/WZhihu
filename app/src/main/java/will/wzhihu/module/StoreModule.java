package will.wzhihu.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import will.wzhihu.common.store.StoryStore;

/**
 * @author wendeping
 */
@Module
public class StoreModule {

    @Singleton
    @Provides
    StoryStore provideStoryStore() {
        return new StoryStore();
    }
}
