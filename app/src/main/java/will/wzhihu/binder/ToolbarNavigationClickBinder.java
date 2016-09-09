package will.wzhihu.binder;

import android.app.Activity;
import android.view.View;

import will.wzhihu.common.binder.Binder;
import will.wzhihu.common.binder.CompositeBinder;
import will.wzhihu.common.widget.WToolbar;

public class ToolbarNavigationClickBinder extends CompositeBinder {

    public ToolbarNavigationClickBinder(final WToolbar toolbar, final Activity activity) {
        this.add(new Binder() {
            @Override
            public void bind() {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });
            }

            @Override
            public void unbind() {
                toolbar.setNavigationOnClickListener(null);
            }
        });
    }
}
