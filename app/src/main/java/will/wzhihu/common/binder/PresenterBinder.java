package will.wzhihu.common.binder;

import java.util.ArrayList;
import java.util.List;

import will.wzhihu.common.presenter.BasePresenter;
import will.wzhihu.common.presenter.PropertyChangeListener;

public class PresenterBinder<T extends BasePresenter> implements Binder {
    protected T presenter;

    private static class ListenerInfo {
        private String name;
        private PropertyChangeListener listener;
        private boolean initialize = false;
    }

    private List<ListenerInfo> listenerInfos = new ArrayList<>();

    public PresenterBinder(T presenter) {
        this.presenter = presenter;
    }

    public T getPresenter() {
        return presenter;
    }

    public PresenterBinder initializeAndAdd(String propertyName, PropertyChangeListener listener) {
        return add(propertyName, listener, true);
    }

    public PresenterBinder add(String propertyName, PropertyChangeListener listener) {
        return add(propertyName, listener, false);
    }

    private PresenterBinder add(String propertyName, PropertyChangeListener listener, boolean initialize) {
        ListenerInfo info = new ListenerInfo();
        info.name = propertyName;
        info.listener = listener;
        info.initialize = initialize;
        listenerInfos.add(info);
        return this;
    }

    @Override
    public void unbind() {
        for (ListenerInfo info : listenerInfos) {
            presenter.removePropertyChangeListener(info.name, info.listener);
        }
    }

    @Override
    public void bind() {
        for (ListenerInfo info : listenerInfos) {
            if (info.initialize) {
                info.listener.propertyChanged();
            }
            presenter.addPropertyChangeListener(info.name, info.listener);
        }
    }
}
