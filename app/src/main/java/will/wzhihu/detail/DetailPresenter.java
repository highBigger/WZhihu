package will.wzhihu.detail;

import javax.inject.Inject;

import will.wzhihu.WApplication;
import will.wzhihu.common.model.ItemPresentationModel;
import will.wzhihu.common.presenter.LoadingPresenter;

/**
 * @author wendeping
 */
public class DetailPresenter extends LoadingPresenter implements ItemPresentationModel<StoryDetail> {
    @Inject
    StoryDetailFinder finder;

    private long storyId;

    public DetailPresenter(long storyId) {
        WApplication.getInjector().inject(this);
    }

    public void load() {
        setLoading(true);
        finder.find(storyId);
    }

    @Override
    public void updateData(int index, StoryDetail bean) {

    }
}
