package will.wzhihu.main.binder;

import android.widget.TextView;

import will.wzhihu.common.binder.PresenterBinder;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class StoryTitleBinder extends PresenterBinder<StoryPresenter> {

    public StoryTitleBinder(final TextView textView, final StoryPresenter presenter) {
        super(presenter);
        add("title", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                textView.setText(presenter.getTitle());
            }
        });
    }
}
