package will.wzhihu.main.presenter;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import will.wzhihu.WApplication;
import will.wzhihu.common.model.ItemPresentationModel;
import will.wzhihu.common.presenter.BasePresenter;
import will.wzhihu.common.store.StoryStore;
import will.wzhihu.main.model.Story;

/**
 * @author wendeping
 */
public class StoryPresenter extends BasePresenter implements ItemPresentationModel<Story> {
    private Story story;

    @Inject
    StoryStore storyStore;

    public StoryPresenter() {
        WApplication.getInjector().inject(this);
    }

    @Override
    public void updateData(int index, Story bean) {
        this.story = bean;
        fireChangeAll();
    }

    public String getTitle() {
        return story.title;
    }

    public String getImage() {
        return story.getImage();
    }

    public Story getStory() {
        return story;
    }

    public boolean getMultiPic() {
        return story.multiPic;
    }

    public void setRead() {
        story.read = true;
        firePropertyChange("read");
        Observable.just(String.valueOf(story.id)).map(new Func1<String, Void>() {
            @Override
            public Void call(String id) {
                storyStore.read(id);
                return null;
            }
        }).subscribeOn(Schedulers.io());
    }

    public boolean getRead() {
        return story.read;
    }

    public String getThumbnail() {
        return story.getThumbnail();
    }

    public void setImage(String image) {
        story.image = image;
        firePropertyChange("image");
    }
}
