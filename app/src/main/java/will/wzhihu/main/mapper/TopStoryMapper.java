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
import will.wzhihu.main.binder.TopStoryBinder;
import will.wzhihu.main.model.Story;
import will.wzhihu.main.presenter.MainPresenter;
import will.wzhihu.main.presenter.TopStoryPresenter;

/**
 * @author wendeping
 */
public class TopStoryMapper extends RecyclerModelMapper<Story> {
    private MainPresenter mainPresenter;

    public TopStoryMapper(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
    }

    @Override
    public View createView(int type, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_top_story, parent, false);
    }

    @Override
    public ItemPresentationModel<Story> createPresenter(@Nullable EditableListPresenter<Story> feedPresenter) {
        return new TopStoryPresenter();
    }

    @Override
    public Binder createBinder(int type, View view, @Nullable EditableListPresenter<Story> feedListPresenter, ItemPresentationModel<Story> presenter) {
        return new TopStoryBinder(view, mainPresenter);
    }
}
