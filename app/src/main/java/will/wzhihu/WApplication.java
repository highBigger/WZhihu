package will.wzhihu;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.okhttp.OkHttpClient;
import will.wzhihu.common.okhttp.OkHttpClients;

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
    }

    private void initFresco() {
        OkHttpClient okHttpClient = OkHttpClients.getNetworkImageClient();
        ImagePipelineConfig build = OkHttpImagePipelineConfigFactory
            .newBuilder(this, okHttpClient)
            .build();
        Fresco.initialize(this, build);
    }
}
