package will.wzhihu.common.rxjava;

import rx.Subscriber;

/**
 * @author wendeping
 */
public class BaseSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {

    }
}
