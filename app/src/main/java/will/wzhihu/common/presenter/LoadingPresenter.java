package will.wzhihu.common.presenter;

public class LoadingPresenter extends BasePresenter {
    private boolean loading;

    public void setLoading(boolean loading) {
        if (this.loading != loading) {
            this.loading = loading;
        }
        firePropertyChange("loading");
    }

    public boolean getLoading() {
        return this.loading;
    }
}
