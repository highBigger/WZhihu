package will.wzhihu.common.binder;


import android.view.View;
import will.wzhihu.presenter.FeedListPresenter;
import will.wzhihu.common.presenter.PropertyChangeListener;

public class FeedEmptyViewLoadingBinder extends PresenterBinder<FeedListPresenter>{

    public FeedEmptyViewLoadingBinder(final View emptyView, final FeedListPresenter presenter) {
        super(presenter);
        add("loading", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                if (presenter.getLoading()) {
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(presenter.isEmpty() ? View.VISIBLE : View.GONE);
                }
            }
        });

        add("items", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                emptyView.setVisibility(presenter.size() > 0 ? View.GONE : View.VISIBLE);
            }
        });
    }
}
