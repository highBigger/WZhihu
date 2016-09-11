package will.wzhihu.main.presenter;

import will.wzhihu.common.model.ItemPresentationModel;
import will.wzhihu.common.presenter.BasePresenter;
import will.wzhihu.main.model.Story;

/**
 * @author wendeping
 */
public class TopStoryPresenter extends BasePresenter implements ItemPresentationModel<Story> {

    @Override
    public void updateData(int index, Story bean) {
        fireChangeAll();
    }
}
