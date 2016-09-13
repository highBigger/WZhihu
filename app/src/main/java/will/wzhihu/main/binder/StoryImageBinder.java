package will.wzhihu.main.binder;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

import will.wzhihu.common.binder.PresenterBinder;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.common.utils.StringUtils;
import will.wzhihu.main.presenter.StoryPresenter;

/**
 * @author wendeping
 */
public class StoryImageBinder extends PresenterBinder<StoryPresenter> {

    public StoryImageBinder(final SimpleDraweeView image, final StoryPresenter presenter) {
        super(presenter);
        add("image", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                Uri uri = null;
                String imageUrl = presenter.getImage();
                if (!StringUtils.isEmpty(imageUrl)) {
                    uri = Uri.parse(imageUrl);
                }

                image.setImageURI(uri);
            }
        });
    }
}
