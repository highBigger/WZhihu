package will.wzhihu.common.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;

import will.wzhihu.common.log.Log;
import will.wzhihu.common.utils.PropertyUtils;

public class BasePresenter {

    private static final String TAG = "BasePresenter";

    protected PropertyChangeSupport propertyChangeSupport;

    protected final ArrayList<ActionListener> mActionListeners = new ArrayList<>();

    private boolean mNotify = true;

    public BasePresenter() {
        Class<?> klass = this.getClass();

        this.propertyChangeSupport = new PropertyChangeSupport(
            klass.getName(),
            PropertyUtils.getPropertyNames(klass)
        );
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public void fireChangeAll() {
        if (mNotify) {
            propertyChangeSupport.fireChangeAll();
        }
    }

    public void firePropertyChange(String name) {
        if (mNotify) {
            propertyChangeSupport.firePropertyChange(name);
        }
    }


    public void initializeAndAddPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        listener.propertyChanged();
        addPropertyChangeListener(propertyName, listener);
    }

    public void notify(boolean notify) {
        mNotify = notify;
    }

    public void onSaveInstanceState(Bundle out) {
    }

    public void onRestoreInstanceState(Bundle state) {
    }

    public void clearListeners() {
        propertyChangeSupport.clear();
    }

    public void addActionListener(ActionListener actionListener) {
        this.mActionListeners.add(actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        this.mActionListeners.remove(actionListener);
    }

    public void fireActed(final ActionListener.ActionResult result) {
        if (mActionListeners.isEmpty()) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            fireActionListeners(result);
        } else {
            new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        fireActionListeners(result);
                    }
                }
            );
        }
    }

    private void fireActionListeners(ActionListener.ActionResult result) {
        for (ActionListener listener : mActionListeners) {
            try {
                listener.onActed(result);
            } catch (Exception e) {
                Log.e(TAG, "onActed error", e);
            }
        }
    }
}


