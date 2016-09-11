package will.wzhihu.main.mapper;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import will.wzhihu.R;
import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.mapper.RecyclerModelMapper;
import will.wzhihu.common.model.ItemPresentationModel;
import will.wzhihu.common.presenter.EditableListPresenter;
import will.wzhihu.main.binder.StoryBinder;
import will.wzhihu.main.model.Story;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class StoryMapper extends RecyclerModelMapper<Story> {

    @Override
    public View createView(int type, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story, parent, false);
    }

    @Override
    public ItemPresentationModel<Story> createPresenter(@Nullable EditableListPresenter<Story> feedPresenter) {
        return new StoryPresenter();
    }

    @Override
    public Binder createBinder(int type, View view, @Nullable EditableListPresenter<Story> feedListPresenter, ItemPresentationModel<Story> presenter) {
        return new StoryBinder(view, (StoryPresenter) presenter);
    }
}
