package will.wzhihu.common.mapper;

import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.presenter.EditableListPresenter;
import will.wzhihu.common.model.ItemPresentationModel;

/**
 * @author dusiyu
 */
public interface ListModelMapper<T, P extends EditableListPresenter<T>> {

    int getViewTypeCount();

    int getItemViewType(int position, T model);

    View createView(int type, ViewGroup parent);

    ItemPresentationModel<T> createPresenter(@Nullable P feedPresenter);

    Binder createBinder(int type, View view, @Nullable P feedListPresenter, ItemPresentationModel<T> presenter);

}
