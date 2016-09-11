package will.wzhihu.main.binder;

import android.widget.TextView;

import will.wzhihu.common.binder.PresenterBinder;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.presenter.PropertyChangeListener;
import will.wzhihu.common.utils.DateUtils;
import will.wzhihu.main.presenter.DatePresenter;

/**
 * @author wendeping
 */
public class DateBinder extends PresenterBinder<DatePresenter> {
    private static final String TAG = "DateBinder";
    public DateBinder(final TextView textView, final DatePresenter presenter) {
        super(presenter);
        Log.d(TAG, "date binder construct");
        add("date", new PropertyChangeListener() {
            @Override
            public void propertyChanged() {
                textView.setText(DateUtils.getShowTime(presenter.getDate()));
            }
        });
    }
}
