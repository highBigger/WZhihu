package will.wzhihu.main.adapter;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import will.wzhihu.R;
import will.wzhihu.common.log.Log;
import will.wzhihu.main.model.Story;

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
        Story story = stories.get(position);
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager_top_story, container, false);
        Log.d(TAG, "%d story %s", position, story.toString());
        SimpleDraweeView image = (SimpleDraweeView) view.findViewById(R.id.image);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(story.title);
        Log.d(TAG, "instantiateItem simpleDraweeView");
        image.setImageURI(Uri.parse(story.getImage()));
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
