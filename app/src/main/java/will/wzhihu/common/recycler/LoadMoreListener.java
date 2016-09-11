package will.wzhihu.common.recycler;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import will.wzhihu.common.presenter.RecyclerPresenter;

/**
 * @author dusiyu
 */
public class LoadMoreListener extends RecyclerView.OnScrollListener {
    private final RecyclerPresenter<?> recyclerPresenter;
    private final int threshold;
    private int totalDelta = 0;
    private final static int FORWARD = 0;
    private final static int BACKWARD = 1;

    public LoadMoreListener(RecyclerPresenter<?> recyclerPresenter, int threshold) {
        this.recyclerPresenter = recyclerPresenter;
        this.threshold = threshold;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        totalDelta += dy;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            tryToLoad(recyclerView);
            totalDelta = 0;
        }
    }

    private void tryToLoad(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        if (totalItemCount == 0) {
            return;
        }
        int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        int direction = totalDelta > 0 ? FORWARD : BACKWARD;
        if (direction == FORWARD && (lastVisiblePosition + threshold) >= totalItemCount) {
            recyclerPresenter.loadAfter();
        } else if (direction == BACKWARD && firstVisiblePosition < threshold) {
            recyclerPresenter.loadBefore();
        }

    }
}
