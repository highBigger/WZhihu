package will.wzhihu.main.binder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.recycler.LoadMoreListener;
import will.wzhihu.common.recycler.RecyclerListAdapter;
import will.wzhihu.main.MainActivity;
import will.wzhihu.main.mapper.StoryDateMapper;
import will.wzhihu.main.mapper.StoryMapper;
import will.wzhihu.main.mapper.TopStoryMapper;
import will.wzhihu.main.model.Story;
import will.wzhihu.main.presenter.MainPresenter;
/**
 * @author wendeping
 */
public class MainListBinder extends CompositeBinder {
    private MainPresenter mainPresenter;
    private LoadMoreListener loadMoreListener;

    public MainListBinder(MainActivity mainActivity, final MainPresenter mainPresenter, final RecyclerView recyclerView) {
        this.mainPresenter = mainPresenter;

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newAdapter());
        add(new Binder() {
            @Override
            public void bind() {
                if (null == loadMoreListener) {
                    loadMoreListener = new LoadMoreListener(mainPresenter, 1);
                    recyclerView.addOnScrollListener(loadMoreListener);
                }
            }

            @Override
            public void unbind() {
                if (null != loadMoreListener) {
                    recyclerView.removeOnScrollListener(loadMoreListener);
                }
            }
        });
    }

    private RecyclerListAdapter newAdapter() {
        RecyclerListAdapter.Builder builder = new RecyclerListAdapter.Builder();
        builder.mapper(Story.ITEM_TYPE_STORY, new StoryMapper());
        builder.mapper(Story.ITEM_TYPE_DATE, new StoryDateMapper());
        builder.mapper(Story.ITEM_TYPE_TOP_STORY, new TopStoryMapper(mainPresenter));

        return builder.build(mainPresenter);
    }
}
