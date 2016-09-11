package will.wzhihu.common.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;
import java.util.Map;
import will.wzhihu.R;
import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.binder.BinderHelper;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.mapper.RecyclerModelMapper;
import will.wzhihu.common.model.FeedItem;
import will.wzhihu.common.model.ItemPresentationModel;
import will.wzhihu.common.presenter.EditableListPresenter;
import will.wzhihu.common.presenter.RecyclerPresenter;
import will.wzhihu.common.utils.ArrayUtils;

public class RecyclerListAdapter<T extends FeedItem> extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder>{

    public static final String TAG = "SingleFeedRecyclerListAdapter";

    private final Map<String, RecyclerModelMapper<T>> mMappers;

    public static class Builder<T extends FeedItem> {

        private Map<String, RecyclerModelMapper<T>> mMappers = new HashMap<>();

        public <F extends T> Builder<T> mapper(String type, RecyclerModelMapper<F> mapper) {
            mMappers.put(type, (RecyclerModelMapper<T>) mapper);
            return this;
        }

        public RecyclerListAdapter<T> build(RecyclerPresenter<T> presenter) {
            for (String feedItemType : presenter.getFeedItemTypes()) {
                if(mMappers.get(feedItemType) == null) {
                    throw new IllegalArgumentException(String.format("Mapper for type '%s' is not specified", feedItemType));
                }
            }
            return new RecyclerListAdapter<T>(mMappers, presenter);
        }
    }

    private EditableListPresenter.ListChangeListener mListChangeListener = new EditableListPresenter.ListChangeListener() {

        @Override
        public void onItemsChanged() {
            Log.d(TAG, "items changed");
            notifyDataSetChanged();
        }

        @Override
        public void onItemChanged(int position) {
            notifyItemChanged(position);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemInserted(int position) {
            notifyItemInserted(position);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRemoved(int position) {
            notifyItemRemoved(position);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart, itemCount);
        }
    };

    private RecyclerPresenter<T> mPresenter;

    private final int[] mViewTypeOffset;

    public RecyclerListAdapter(Map<String, RecyclerModelMapper<T>> mappers, RecyclerPresenter<T>presenter) {
        this.mMappers = mappers;
        this.setPresenter(presenter);
        String[] types = presenter.getFeedItemTypes();
        int count = 0;
        mViewTypeOffset = new int[types.length];
        for (int i = 0, length = types.length; i < length; i++) {
            mViewTypeOffset[i] = count;
            count += mMappers.get(types[i]).getViewTypeCount();
        }
    }

    public void setPresenter(RecyclerPresenter<T> presenter) {
        if (mPresenter != presenter) {
            if (mPresenter != null) {
                mPresenter.removeListChangeListener(mListChangeListener);
            }
        }
        mPresenter = presenter;
        if (mPresenter != null) {
            mPresenter.addListChangeListener(mListChangeListener);
            Log.d(TAG, "setPresenter: add listener");
        }
    }

    @Override
    public long getItemId(int position) {
        return mPresenter.getItemId(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int size = mViewTypeOffset.length;
        String[] feedItemTypes = mPresenter.getFeedItemTypes();
        String type = feedItemTypes[feedItemTypes.length - 1]; // default to the last one
        int viewTypeOffset = mViewTypeOffset[mViewTypeOffset.length - 1];
        for (int i = 0; i < size - 1; i++) {
            if (mViewTypeOffset[i] <= viewType && mViewTypeOffset[i + 1] > viewType) {
                type = feedItemTypes[i];
                viewTypeOffset = mViewTypeOffset[i];
            }
        }
        RecyclerModelMapper<T> mapper = mMappers.get(type);
        return new ViewHolder(mapper.createView(viewType - viewTypeOffset, parent));
    }

    @Override
    public int getItemViewType(int position) {
        String[] types = mPresenter.getFeedItemTypes();
        T item = getItem(position);
        int index = ArrayUtils.indexOf(types, item.getItemType());

        return mViewTypeOffset[index] + mMappers.get(item.getItemType()).getItemViewType(position, item);
    }

    public T getItem(int position) {
        if (mPresenter != null) {
            return mPresenter.getItems().get(position);
        } else {
            return null;
        }
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        if (holder.itemView != null) {
            BinderHelper.unbind(holder.itemView);
            holder.itemView.setTag(R.id.presenter, null);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        T item = mPresenter.get(position);
        RecyclerModelMapper<T> mapper = mMappers.get(item.getItemType());
        if (mapper == null) {
            throw new IllegalStateException(String.format("No mapper could be found for type '%s'", item.getItemType()));
        }
        int itemViewType = mapper.getItemViewType(position, item);
        View view = holder.itemView;
        ItemPresentationModel<T> presenter =
            (ItemPresentationModel<T>) view.getTag(R.id.presenter);
        if (presenter == null) {
            presenter =  mapper.createPresenter(mPresenter);
            view.setTag(R.id.presenter, presenter);
        }
        Binder binder = BinderHelper.getBinder(view);
        if (binder == null) {
            binder = mapper.createBinder(itemViewType, view, mPresenter, presenter);
            BinderHelper.bind(view, binder);
        }
        presenter.updateData(position, item);
    }

    @Override
    public int getItemCount() {
        return mPresenter.size();
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
