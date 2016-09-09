package will.wzhihu;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
/**
 * @author wendeping
 */
public class WApplication extends Application {
    private static Application context;

    public static Context getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        //init fresco image lib
        initFresco();
        Injectors.init(this);
    }

    private void initFresco() {
        Fresco.initialize(this);
    }
}
