package will.wzhihu.common.recycler;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.HashMap;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final Integer DEFAULT = Integer.MIN_VALUE;

    private HashMap<Integer, Drawable> mDividerMap = new HashMap<>();

    public DividerItemDecoration(Drawable divider) {
        mDividerMap.put(DEFAULT, divider);
    }

    /**
     * @param index   in adapter, negative stands for backward counting.
     *                For instance, -1 is the last one.
     * @param divider to draw
     */
    public void setDividerFor(Drawable divider, int... index) {
        for (int i : index) {
            mDividerMap.put(i, divider);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {

        LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        if (manager.getOrientation() == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }


    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (shouldOmitVerticalDivider(parent, i + firstVisibleItemPosition)) {
                continue;
            }

            Drawable divider = getDividerFor(i + firstVisibleItemPosition, parent.getAdapter().getItemCount());

            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;

            final int bottom = top + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    /**
     * if omit the first three items, should return i<=2
     * if just the third item , return i==2
     * @param parent
     * @param i the index of list
     * @return
     */
    protected boolean shouldOmitVerticalDivider(RecyclerView parent, int i) {
        return false;
    }

    protected boolean shouldOmitHorizontalDivider(RecyclerView parent, int i) {
        return false;
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (shouldOmitHorizontalDivider(parent, i + firstVisibleItemPosition)) {
                continue;
            }

            Drawable divider = getDividerFor(i + firstVisibleItemPosition, parent.getAdapter().getItemCount());

            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    /*itemPosition is absolutely position*/
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {

        Drawable divider = getDividerFor(itemPosition, parent.getAdapter().getItemCount());

        LinearLayoutManager manager = (LinearLayoutManager) parent.getLayoutManager();
        if (manager.getOrientation() == LinearLayoutManager.VERTICAL) {
            if (shouldOmitVerticalDivider(parent, itemPosition)) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, 0, divider.getIntrinsicHeight());
            }
        } else {
            if (shouldOmitHorizontalDivider(parent, itemPosition)) {
                outRect.set(0, 0, 0, 0);
            } else {
                outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
            }
        }
    }

    /**
     *
     * @param index in adapter, negative stands for backward counting.
     *              For instance, -1 is the last one.
     * @return divider for index
     */
    private Drawable getDividerFor(int index, int total) {
        Drawable divider;
        if (mDividerMap.containsKey(index)) {
            divider = mDividerMap.get(index);
        }else if (mDividerMap.containsKey(index - total)) {
            divider = mDividerMap.get(index - total);
        } else {
            divider = mDividerMap.get(DEFAULT);
        }
        return divider;
    }
}
