package will.wzhihu.splash;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import will.wzhihu.R;
import will.wzhihu.WApplication;
import will.wzhihu.common.activity.ActivityStarter;
import will.wzhihu.common.activity.BaseActivity;
import will.wzhihu.common.animation.BaseAnimationListener;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.rxjava.BaseSubscriber;
import will.wzhihu.common.utils.ToastUtils;
import will.wzhihu.main.MainActivity;
import will.wzhihu.splash.client.SplashClient;
import will.wzhihu.splash.model.Splash;

/**
 * @author wendeping
 */
public class SplashActivity extends BaseActivity {
    public static final String TAG = "SplashActivity";

    @Bind(R.id.image)
    SimpleDraweeView image;

    @Bind(R.id.loading)
    View loading;

    @Bind(R.id.copy_right)
    TextView copyRight;

    @Inject
    SplashClient splashClient;

    private Subscriber<Splash> subscriber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        WApplication.getInjector().inject(this);
        ButterKnife.bind(this);

        Observable<Splash> observable = splashClient.getSplash();
        subscriber = new BaseSubscriber<Splash>() {
            @Override
            public void onNext(Splash splash) {
                copyRight.setText(splash.text);
                ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        loading.setVisibility(View.GONE);
                        Animation animation = AnimationUtils.loadAnimation(SplashActivity.this.getApplicationContext(), R.anim.splash_fade_in);
                        animation.setAnimationListener(new BaseAnimationListener() {
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                goToMain();
                            }
                        });
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

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "load splash error", e);
                ToastUtils.toast(e.getMessage());
                goToMain();
            }
        };

        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    protected void onDestroy() {
        if (null != subscriber){
            subscriber.unsubscribe();
        }
        super.onDestroy();
    }

    private void goToMain() {
        this.finish();
        new ActivityStarter(MainActivity.class).start(this);
    }
}
