package will.wzhihu.main.binder;

import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import will.wzhihu.R;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class StoryBinder extends CompositeBinder {

    @Bind(R.id.image)
    SimpleDraweeView image;

    @Bind(R.id.title)
    TextView title;

    public StoryBinder(View view, StoryPresenter storyPresenter, boolean bindRead, boolean thumbnail) {
        this(view, storyPresenter, bindRead, thumbnail, true);
    }

    public StoryBinder(View view, StoryPresenter storyPresenter, boolean bindRead, boolean thumbnail, boolean clickable) {
        ButterKnife.bind(this, view);
        if (clickable) {
            add(new StoryClickBinder(view, storyPresenter));
        }
        add(new StoryImageBinder(image, storyPresenter, thumbnail));
        add(new StoryTitleBinder(title, storyPresenter, bindRead));
    }
}
