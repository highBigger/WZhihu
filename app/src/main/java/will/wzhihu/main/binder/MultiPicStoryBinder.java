package will.wzhihu.main.binder;

import android.view.View;
import android.widget.TextView;

import will.wzhihu.R;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class MultiPicStoryBinder extends StoryBinder {
    private TextView multiPic;

    public MultiPicStoryBinder(final View view, final StoryPresenter presenter) {
        super(view, presenter);

        multiPic = (TextView) view.findViewById(R.id.multi_pic);
        presenter.addPropertyChangeListener("multiPic", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                multiPic.setVisibility(presenter.getMultiPic() ? View.VISIBLE : View.GONE);
            }
        });
    }
}
