package will.wzhihu.main.mapper;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import will.wzhihu.R;
import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.mapper.RecyclerModelMapper;
import will.wzhihu.common.model.ItemPresentationModel;
import will.wzhihu.common.presenter.EditableListPresenter;
import will.wzhihu.main.binder.DateBinder;
import will.wzhihu.main.model.Story;
import will.wzhihu.main.presenter.DatePresenter;

/**
 * @author wendeping
 */
public class StoryDateMapper extends RecyclerModelMapper<Story> {
    @Override
    public View createView(int type, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
    }

    @Override
    public ItemPresentationModel<Story> createPresenter(@Nullable EditableListPresenter<Story> feedPresenter) {
        return new DatePresenter();
    }


    @Override
    public Binder createBinder(int type, View view, @Nullable EditableListPresenter<Story> feedListPresenter, ItemPresentationModel<Story> presenter) {
        return new DateBinder((TextView) view, (DatePresenter) presenter);
    }
}
