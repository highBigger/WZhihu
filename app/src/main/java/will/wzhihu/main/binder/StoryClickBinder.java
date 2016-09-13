package will.wzhihu.main.binder;

import android.app.Activity;
import android.view.View;

import will.wzhihu.common.binder.Binder;
import will.wzhihu.detail.DetailActivity;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class StoryClickBinder implements Binder {
    private View view;
    private StoryPresenter presenter;

    public StoryClickBinder(final View view, final StoryPresenter presenter) {
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void bind() {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.start(presenter.getStory(), (Activity) view.getContext());
            }
        });
    }

    @Override
    public void unbind() {
        view.setOnClickListener(null);
    }
}
