package will.wzhihu.main.binder;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import will.wzhihu.common.adapter.RecyclerListAdapter;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.main.MainActivity;
import will.wzhihu.main.mapper.StoryDateMapper;
import will.wzhihu.main.mapper.StoryMapper;
import will.wzhihu.main.mapper.TopStoryMapper;
import will.wzhihu.main.model.Story;
import will.wzhihu.main.presenter.MainPresenter;
/**
 * @author wendeping
 */
public class MainBinder extends CompositeBinder{
    private MainPresenter mainPresenter;
    private RecyclerView recyclerView;
    private MainActivity mainActivity;

    public MainBinder(MainActivity mainActivity, MainPresenter mainPresenter, RecyclerView recyclerView) {
        this.mainActivity = mainActivity;
        this.mainPresenter = mainPresenter;
        this.recyclerView = recyclerView;

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newAdapter());
    }

    private RecyclerListAdapter newAdapter() {
        RecyclerListAdapter.Builder builder = new RecyclerListAdapter.Builder();
        builder.mapper(Story.ITEM_TYPE_STORY, new StoryMapper());
        builder.mapper(Story.ITEM_TYPE_DATE, new StoryDateMapper());
        builder.mapper(Story.ITEM_TYPE_TOP_STORY, new TopStoryMapper(mainPresenter));

        return builder.build(mainPresenter);
    }
}
