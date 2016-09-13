package will.wzhihu.main.binder;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import will.wzhihu.R;
import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.detail.DetailActivity;
import will.wzhihu.main.model.Story;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class StoryBinder extends CompositeBinder {
    @Bind(R.id.image)
    SimpleDraweeView image;

    @Bind(R.id.title)
    TextView title;

    TextView multiPic;

    public StoryBinder(final View view, final StoryPresenter presenter) {
        image = (SimpleDraweeView) view.findViewById(R.id.image);
        title = (TextView) view.findViewById(R.id.title);
        multiPic = (TextView) view.findViewById(R.id.multi_pic);
        presenter.addPropertyChangeListener("image", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                if (null != presenter.getImage()) {
                    image.setImageURI(Uri.parse(presenter.getImage()));
                }
            }
        });

        presenter.addPropertyChangeListener("title", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                title.setText(presenter.getTitle());
            }
        });

        presenter.addPropertyChangeListener("multiPic", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                multiPic.setVisibility(presenter.getMultiPic() ? View.VISIBLE : View.INVISIBLE);
            }
        });

        add(new Binder() {
            @Override
            public void bind() {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Story story = presenter.getStory();
                        DetailActivity.start(story, (Activity) view.getContext());
                    }
                });
            }

            @Override
            public void unbind() {
                view.setOnClickListener(null);
            }
        });
    }
}
