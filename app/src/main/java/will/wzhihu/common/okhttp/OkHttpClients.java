package will.wzhihu.common.okhttp;

import android.content.Context;
import android.util.Log;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import will.wzhihu.WApplication;

/**
 * @author wendeping
 */
public class OkHttpClients {
    private final static Cache cache = buildCache();

    private final static int CONNECT_TIMEOUT = 15;

    private static final OkHttpClient DEFAULT = buildClient();

    public static OkHttpClient defaultClient() {
        return DEFAULT;
    }

    public static OkHttpClient buildClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        client.interceptors().add(new CookieInterceptor());
        client.interceptors().add(new LoggingInterceptor());
        client.setCache(cache);
        client.setRetryOnConnectionFailure(false);
        return client;
    }

    static class CookieInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            // TODO: hard coded
            builder.addHeader("Cookie", "running=staging");
            return chain.proceed(builder.build());
        }
    }

    static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();

            try {
                Response response = chain.proceed(request);
                long t2 = System.nanoTime();
                double duration = (t2 - t1) / 1e6d;
                String bodyString = response.body().string();
                Log.d("OkHttpClient", String.format("Request method is %s received %d response for %s in %.1fms body is %s %n", request.method(), response.code(), request.url(), duration, bodyString));
                return response.newBuilder()
                        .body(ResponseBody.create(response.body().contentType(), bodyString))
                        .build();
            } catch (IOException exception) {
                long t2 = System.nanoTime();
                double duration = (t2 - t1) / 1e6d;
                Log.d("OkHttpClient", String.format("Failed to request method is %s receive response for %s in %.1fms", request.method(), request.url(), duration));
                throw exception;
            }
        }
    }

    private static Cache buildCache() {
        Context context = WApplication.getInstance();

        File cacheFile = new File(context.getCacheDir(), "request_cache");
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }

        return new Cache(cacheFile, 5 * 1024 * 1024);
    }

    public static OkHttpClient getNetworkImageClient() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static OkHttpClient INSTANCE = createNetworkImageClient();
    }

    private static OkHttpClient createNetworkImageClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        client.setCache(buildNetworkImageCache());
        return client;
    }

    private static Cache buildNetworkImageCache() {
        Context context = WApplication.getInstance();
        File cacheFile = new File(context.getCacheDir(), "image_request_cache");
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
        return new Cache(cacheFile, 20 * 1024 * 1024);
    }
}
