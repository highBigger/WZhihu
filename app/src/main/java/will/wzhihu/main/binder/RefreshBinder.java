package will.wzhihu.main.binder;

import android.support.v4.widget.SwipeRefreshLayout;

import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.main.presenter.MainPresenter;

/**
 * @author wendeping
 */
public class RefreshBinder extends CompositeBinder {
    public RefreshBinder(final SwipeRefreshLayout layout, final MainPresenter presenter) {
        presenter.addPropertyChangeListener("loading", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                layout.setRefreshing(presenter.getLoading());
            }
        });

        presenter.addPropertyChangeListener("enableRefresh", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                layout.setEnabled(presenter.getEnableRefresh());
            }
        });

        add(new Binder() {
            @Override
            public void bind() {
                layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        presenter.refresh();
                    }
                });
            }

            @Override
            public void unbind() {
                layout.setOnRefreshListener(null);
            }
        });
    }
}
