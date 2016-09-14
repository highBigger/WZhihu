package will.wzhihu.detail.binder;

import android.app.Activity;

import will.wzhihu.binder.ToolbarNavigationClickBinder;
import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.common.widget.WToolbar;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class DetailToolbarBinder extends CompositeBinder {
    private static final String TAG = "DetailToolbarBinder";

    public DetailToolbarBinder(final WToolbar toolbar, Activity activity, final StoryPresenter presenter) {
        add(new ToolbarNavigationClickBinder(toolbar, activity));
        add(new Binder() {
            @Override
            public void bind() {
                presenter.addPropertyChangeListener("title", new PropertyChangeListener() {
                    @Override
                    public void propertyChanged() {
                        String title = presenter.getTitle();
                        Log.d(TAG, "bind toolbar title %s", title);
                        if (title.length() > 16) {
                            title = title.substring(0, 15) + "...";
                        }
                        toolbar.setHeaderTitle(title);
                    }
                });
            }

            @Override
            public void unbind() {

            }
        });
    }
}
