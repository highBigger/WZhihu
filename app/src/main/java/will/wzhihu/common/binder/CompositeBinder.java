package will.wzhihu.common.binder;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class CompositeBinder implements Binder, StatefulBinder{
    List<Binder> binders = new ArrayList<>();

    public CompositeBinder add(Binder binder) {
        binders.add(binder);
        return this;
    }

    @Override
    public void bind() {
        for (Binder binder : binders) {
            binder.bind();
        }
    }

    @Override
    public void unbind() {
        // unbind in reverse order
        int size = binders.size();
        for (int i = size - 1; i >= 0; i--) {
            Binder binder = binders.get(i);
            binder.unbind();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        for (Binder binder : binders) {
            if (binder instanceof StatefulBinder) {
                ((StatefulBinder) binder).onSaveInstanceState(bundle);
            }
        }
    }

    @Override
    public void restoreInstanceState(Bundle bundle) {
        for (Binder binder : binders) {
            if (binder instanceof StatefulBinder) {
                ((StatefulBinder) binder).restoreInstanceState(bundle);
            }
        }

    }
}
