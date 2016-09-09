package will.wzhihu;

import android.app.Application;

/**
 * @author wendeping
 */
public class Injectors {

    private static WComponent wComponent;

    public static void init(Application application) {
        wComponent = DaggerWComponent.builder().clientModule(new ClientModule()).build();
    }

    public static WComponent getInjector() {
        return wComponent;
    }
}
