package will.wzhihu;

import android.app.Application;
import com.facebook.drawee.backends.pipeline.Fresco;
import will.wzhihu.module.ClientModule;
import will.wzhihu.module.DaggerWComponent;
import will.wzhihu.module.WComponent;

/**
 * @author wendeping
 */
public class WApplication extends Application {
    private static WApplication context;
    private WComponent wComponent;

    public static WApplication getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        wComponent = DaggerWComponent.builder().clientModule(new ClientModule()).build();

        //init fresco image lib
        initFresco();
        initToast();
    }

    private void initToast() {
        try {
            // to ensure the Toast will be initialized on the main thread
            Class.forName("android.widget.Toast");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initFresco() {
        Fresco.initialize(this);
    }

    public static WComponent getInjector() {
        return context.wComponent;
    }
}
