package will.wzhihu.main.binder;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import will.wzhihu.R;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class MultiPicStoryBinder extends StoryBinder {
    @Bind(R.id.multi_pic)
    TextView multiPic;

    public MultiPicStoryBinder(final View view, final StoryPresenter presenter) {
        super(view, presenter, true, true);
        ButterKnife.bind(this, view);
        presenter.addPropertyChangeListener("multiPic", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                multiPic.setVisibility(presenter.getMultiPic() ? View.VISIBLE : View.GONE);
            }
        });
    }
}
