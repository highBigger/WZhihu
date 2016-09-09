package will.wzhihu.common.client;

import java.lang.reflect.ParameterizedType;
import javax.inject.Inject;
import retrofit.Retrofit;
import will.wzhihu.common.log.Log;
import will.wzhihu.common.okhttp.OkHttpClients;
import will.wzhihu.common.retrofit.Retrofits;

public class RetrofitBaseClient<T> {
    private static final String TAG = "RetrofitBaseClient" ;

    protected T endpoint;

    private Class<T> endpointClass;

    @Inject
    public RetrofitBaseClient() {
        endpointClass = (Class<T>)((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];

        endpoint = createRetrofit().create(endpointClass);
        Log.d(TAG, "create retrofit client");
    }

    protected Retrofit createRetrofit() {
        return Retrofits.defaultBuilder()
            .client(OkHttpClients.defaultClient())
            .build();
    }

    public T getEndpoint() {
        return endpoint;
    }
}

