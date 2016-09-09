package will.wzhihu.splash;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import will.wzhihu.Injectors;
import will.wzhihu.R;
import will.wzhihu.common.activity.BaseActivity;
import will.wzhihu.common.log.Log;
import will.wzhihu.splash.client.SplashClient;
import will.wzhihu.splash.model.Splash;

/**
 * @author wendeping
 */
public class SplashActivity extends BaseActivity {
    public static final String TAG = "SplashActivity";

    @Bind(R.id.image)
    SimpleDraweeView image;

    @Inject
    SplashClient splashClient;

    private Observable<Splash> observable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Injectors.getInjector().inject(this);
        ButterKnife.bind(this);

        observable = splashClient.getSplash(720, 1280);
        Observer<Splash> observer = new Observer<Splash>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "failed");
            }

            @Override
            public void onNext(Splash splash) {
                ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this.getApplicationContext(), R.anim.splash_fade_in);
                        image.startAnimation(animation);
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        Log.d(TAG, "onFailure");
                    }
                };

                DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(controllerListener)
                    .setUri(Uri.parse(splash.img))
                    .build();

                image.setController(controller);
            }
        };
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
