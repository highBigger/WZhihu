package will.wzhihu.common.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Gsons {

    private static Gson defaultInstance;

    static {
        GsonBuilder builder = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        defaultInstance = builder.create();
    }

    public static Gson get() {
        return defaultInstance;
    }
}
