package will.wzhihu.common.rxjava;

import will.wzhihu.common.presenter.LoadingPresenter;

/**
 * @author wendeping
 */
public class LoadingPresenterSubscriber<T> extends BaseSubscriber<T> {
    private LoadingPresenter loadingPresenter;

    public LoadingPresenterSubscriber (LoadingPresenter loadingPresenter) {
        this.loadingPresenter = loadingPresenter;
    }

    @Override
    public void onCompleted() {
        loadingPresenter.setLoading(false);
    }

    @Override
    public void onError(Throwable e) {
        loadingPresenter.setLoading(false);
    }
}
