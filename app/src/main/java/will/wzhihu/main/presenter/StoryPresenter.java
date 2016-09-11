package will.wzhihu.main.presenter;

import java.util.List;

import will.wzhihu.common.model.ItemPresentationModel;
import will.wzhihu.common.presenter.BasePresenter;
import will.wzhihu.common.utils.CollectionUtils;
import will.wzhihu.main.model.Story;

/**
 * @author wendeping
 */
public class StoryPresenter extends BasePresenter implements ItemPresentationModel<Story> {
    private Story story;

    @Override
    public void updateData(int index, Story bean) {
        this.story = bean;
        fireChangeAll();
    }

    public String getTitle() {
        return story == null ? null : story.title;
    }

    public String getImage() {
        if (story == null) {
            return null;
        }

        List<String> images = story.images;
        if (CollectionUtils.isEmpty(images)) {
            return null;
        }

        return images.get(0);
    }
}
