package will.wzhihu.main.binder;

import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import will.wzhihu.R;
import will.wzhihu.common.binder.PresenterBinder;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class StoryBinder extends PresenterBinder<StoryPresenter> {
    @Bind(R.id.image)
    SimpleDraweeView image;

    @Bind(R.id.title)
    TextView title;

    public StoryBinder(View view, StoryPresenter presenter) {
        super(presenter);
//        ButterKnife.bind(view);
        image = (SimpleDraweeView) view.findViewById(R.id.image);
        title = (TextView) view.findViewById(R.id.title);
        add("image", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                if (null != getPresenter().getImage()) {
                    image.setImageURI(Uri.parse(getPresenter().getImage()));
                }
            }
        });

        add("title", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                title.setText(getPresenter().getTitle());
            }
        });
    }
}
