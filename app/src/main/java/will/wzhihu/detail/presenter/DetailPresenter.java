package will.wzhihu.detail.presenter;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import will.wzhihu.WApplication;
import will.wzhihu.common.model.ItemPresentationModel;
import will.wzhihu.common.presenter.LoadingPresenter;
import will.wzhihu.common.rxjava.LoadingPresenterSubscriber;
import will.wzhihu.common.utils.CollectionUtils;
import will.wzhihu.common.utils.ToastUtils;
import will.wzhihu.detail.StoryDetail;
import will.wzhihu.detail.StoryDetailFinder;

/**
 * @author wendeping
 */
public class DetailPresenter extends LoadingPresenter implements ItemPresentationModel<StoryDetail> {
    @Inject
    StoryDetailFinder finder;

    private StoryDetail detail;

    private String storyId;

    public DetailPresenter(String storyId) {
        this.storyId = storyId;
        WApplication.getInjector().inject(this);
    }

    public void load() {
        if (getLoading()) {
            return;
        }
        setLoading(true);
        finder.find(storyId).observeOn(AndroidSchedulers.mainThread()).map(new Func1<StoryDetail, Void>() {
            @Override
            public Void call(StoryDetail detail) {
                updateData(0, detail);
                return null;
            }
        }).subscribe(new LoadingPresenterSubscriber<Void>(this) {
            @Override
            public void onError(Throwable e) {
                //show Error
                ToastUtils.toast(e.getMessage());
            }
        });
    }

    @Override
    public void updateData(int index, StoryDetail bean) {
        this.detail = bean;
        fireChangeAll();
    }

    public String getImage() {
        return detail.image;
    }

    public String getCss() {
        if (!CollectionUtils.isEmpty(detail.css)) {
            return detail.css.get(0);
        }

        return null;
    }

    public String getBody() {
        return detail.body;
    }
}
