package will.wzhihu.main.binder;

import android.net.Uri;
import com.facebook.drawee.view.SimpleDraweeView;
import will.wzhihu.common.binder.PresenterBinder;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.common.utils.StringUtils;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class StoryImageBinder extends PresenterBinder<StoryPresenter> {
    private static final String TAG = "StoryImageBinder";

    public StoryImageBinder(final SimpleDraweeView image, final StoryPresenter presenter, boolean thumbnail) {
        super(presenter);
        if (thumbnail) {
            add("thumbnail", new PropertyChangeListener() {
                @Override
                public void propertyChanged() {
                    setImage(image, presenter.getThumbnail());
                }
            });
            return;
        }

        add("image", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                Log.d(TAG, "story image property change");
                String imageUrl = presenter.getImage();
                if (StringUtils.isEmpty(imageUrl)) {
                    imageUrl = presenter.getThumbnail();
                }
                setImage(image, imageUrl);
            }
        });
    }

    private void setImage(SimpleDraweeView image, String imageUrl) {
        Uri uri = null;
        if (!StringUtils.isEmpty(imageUrl)) {
            uri = Uri.parse(imageUrl);
        }

        image.setImageURI(uri);
    }
}
