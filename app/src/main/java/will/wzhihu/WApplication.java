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
        //init fresco image lib
        initFresco();
        wComponent = DaggerWComponent.builder().clientModule(new ClientModule()).build();
    }

    private void initFresco() {
        Fresco.initialize(this);
    }

    public static WComponent getInjector() {
        return context.wComponent;
    }
}
