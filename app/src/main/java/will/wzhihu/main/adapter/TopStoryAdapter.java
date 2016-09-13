package will.wzhihu.main.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import will.wzhihu.R;
import will.wzhihu.common.log.Log;
import will.wzhihu.main.binder.StoryBinder;
import will.wzhihu.main.model.Story;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class TopStoryAdapter extends PagerAdapter {
    private static final String TAG = "TopStoryAdapter";
    private List<Story> stories;

    public TopStoryAdapter(List<Story> stories) {
        this.stories = stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int size = (null == stories ? 0 : stories.size());
        Log.d(TAG, "adapter size %d", size);
        return size;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager_top_story, container, false);

        Story story = stories.get(position);
        StoryPresenter presenter = new StoryPresenter();
        StoryBinder binder = new StoryBinder(view, presenter);
        binder.bind();
        presenter.updateData(position, story);

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
