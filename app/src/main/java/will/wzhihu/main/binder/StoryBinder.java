package will.wzhihu.main.binder;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import will.wzhihu.R;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class StoryBinder extends CompositeBinder {

    public StoryBinder(View view, StoryPresenter storyPresenter) {
        add(new StoryClickBinder(view, storyPresenter));
        add(new StoryImageBinder((SimpleDraweeView) view.findViewById(R.id.image), storyPresenter));
        add(new StoryTitleBinder((TextView) view.findViewById(R.id.title), storyPresenter));
    }
}
