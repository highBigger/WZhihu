package will.wzhihu.common.binder;

import android.os.Bundle;

/**
 * @author dusiyu
 */
public interface StatefulBinder extends Binder {

    void onSaveInstanceState(Bundle bundle);

    void restoreInstanceState(Bundle bundle);
}
