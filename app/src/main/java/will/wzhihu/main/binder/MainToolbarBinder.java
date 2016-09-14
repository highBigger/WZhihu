package will.wzhihu.main.binder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.utils.DateUtils;
import will.wzhihu.common.widget.WToolbar;
import will.wzhihu.main.model.Story;
import will.wzhihu.main.presenter.MainPresenter;

/**
 * @author wendeping
 */
public class MainToolbarBinder extends CompositeBinder {
    private MainPresenter mainPresenter;
    private WToolbar toolbar;
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            if (firstVisibleItemPosition != 0) {
                Story story = mainPresenter.get(firstVisibleItemPosition);
                toolbar.setHeaderTitle(DateUtils.getShowTime(story.date));
            }
        }
    };

    public MainToolbarBinder(final WToolbar toolbar, final RecyclerView recyclerView, MainPresenter mainPresenter) {
        this.toolbar = toolbar;
        this.mainPresenter = mainPresenter;
        add(new Binder() {
            @Override
            public void bind() {
                recyclerView.addOnScrollListener(scrollListener);
            }

            @Override
            public void unbind() {
                recyclerView.removeOnScrollListener(scrollListener);
            }
        });

        add(new Binder() {
            @Override
            public void bind() {
                toolbar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       recyclerView.smoothScrollToPosition(0);
                    }
                });
            }

            @Override
            public void unbind() {
                toolbar.setOnClickListener(null);
            }
        });

    }
}
