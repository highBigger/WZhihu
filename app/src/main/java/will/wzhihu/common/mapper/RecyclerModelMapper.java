package will.wzhihu.common.mapper;

import will.wzhihu.common.model.FeedItem;
import will.wzhihu.common.presenter.EditableListPresenter;

public abstract class RecyclerModelMapper<T extends FeedItem> implements ListModelMapper<T, EditableListPresenter<T>> {

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position, T model) {
        return 0;
    }

}
