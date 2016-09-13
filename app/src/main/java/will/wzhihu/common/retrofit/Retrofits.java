package will.wzhihu.common.retrofit;

import com.squareup.okhttp.HttpUrl;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import will.wzhihu.common.Hosts;
import will.wzhihu.common.gson.Gsons;

/**
 * @author wendeping
 */
public class Retrofits {

    public static Retrofit.Builder defaultBuilder() {
        return new Retrofit.Builder()
                .baseUrl(new HttpUrl.Builder().scheme("http").host(Hosts.getHost()).build())
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(Gsons.get()));
    }

}
