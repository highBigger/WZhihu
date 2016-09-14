package will.wzhihu.main.binder;

import android.widget.TextView;

import will.wzhihu.R;
import will.wzhihu.common.binder.PresenterBinder;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class StoryTitleBinder extends PresenterBinder<StoryPresenter> {

    public StoryTitleBinder(final TextView textView, final StoryPresenter presenter, boolean bindRead) {
        super(presenter);
        add("title", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                textView.setText(presenter.getTitle());
            }
        });

        if (!bindRead) {
            return;
        }

        add("read", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                int color = textView.getResources().getColor(getPresenter().getRead() ? R.color.black_50 : R.color.black);
                textView.setTextColor(color);
            }
        });
    }
}
