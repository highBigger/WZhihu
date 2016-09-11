package will.wzhihu.main.binder;

import android.support.v4.widget.SwipeRefreshLayout;

import will.wzhihu.common.binder.PresenterBinder;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.main.presenter.MainPresenter;

/**
 * @author wendeping
 */
public class RefreshBinder extends PresenterBinder<MainPresenter>{
    public RefreshBinder(final SwipeRefreshLayout layout, final MainPresenter presenter) {
        super(presenter);
        add("loading", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                layout.setRefreshing(presenter.getLoading());
            }
        });
    }
}
