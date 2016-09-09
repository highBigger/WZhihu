package will.wzhihu.common.binder;

import android.database.DataSetObserver;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class LinearLayoutBinder implements Binder{

    private LinearLayout mLinearLayout;

    private BaseAdapter mAdapter;

    private int mLimit = Integer.MAX_VALUE;

    private DataSetObserver mChangeObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            int childCount = mLinearLayout.getChildCount();
            int totalCount = Math.min(mAdapter.getCount(), mLimit);
            for (int i = 0; i < totalCount; i++) {
                View view = null;
                if ( i < childCount) {
                    view = mLinearLayout.getChildAt(i);
                }
                view = mAdapter.getView(i, view, mLinearLayout);
                if (view.getParent() == null) {
                    mLinearLayout.addView(view);
                }
            }

            while (childCount > totalCount) {
                View child = mLinearLayout.getChildAt(--childCount);
                BinderHelper.unbind(child);
                mLinearLayout.removeViewAt(childCount);
                childCount = mLinearLayout.getChildCount();
            }
        }

        @Override
        public void onInvalidated() {
            BinderHelper.unbindAll(mLinearLayout);
            mLinearLayout.removeAllViews();
        }
    };
    public LinearLayoutBinder(LinearLayout linearLayout, BaseAdapter adapter) {
        this(linearLayout, adapter, Integer.MAX_VALUE);
    }

    public LinearLayoutBinder(LinearLayout linearLayout, BaseAdapter adapter, int limit) {
        this.mLinearLayout = linearLayout;
        this.mAdapter = adapter;
        this.mLimit = limit;
    }

    @Override
    public void bind() {
        mAdapter.registerDataSetObserver(mChangeObserver);
        mChangeObserver.onChanged();
    }

    @Override
    public void unbind() {
        mAdapter.unregisterDataSetObserver(mChangeObserver);
    }
}
