package will.wzhihu.main.presenter;

import will.wzhihu.common.model.ItemPresentationModel;
import will.wzhihu.common.presenter.BasePresenter;
import will.wzhihu.common.utils.StringUtils;
import will.wzhihu.main.model.Story;

/**
 * @author wendeping
 */
public class DatePresenter extends BasePresenter implements ItemPresentationModel<Story> {
    private String date;

    @Override
    public void updateData(int index, Story bean) {
        if (null == bean && date != null) {
            date = null;
            firePropertyChange("date");
        }

        if (StringUtils.isEmpty(date) && bean != null) {
            date = bean.date;
            firePropertyChange("date");
        }

        if (!StringUtils.isEqual(date, bean.date)) {
            this.date = bean.date;
            firePropertyChange("date");
        }
    }

    public String getDate() {
        return date;
    }
}
