package will.wzhihu.splash;

/**
 * @author wendeping
 */

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import will.wzhihu.common.utils.DensityUtil;

/**
 * size collection
 320*432
 480*728
 720*1184
 1080*1776
 */
public class SplashSizeUtil {
    private static Map<Integer, String> sizeMap = new HashMap() {
        {
            put(320, "320*432");
            put(480, "480*728");
            put(720, "720*1184");
            put(1080, "1080*1776");
        }
    };

    private static int[] widthArray = {320, 480, 720, 1080};

    public static String getSizeString(Context context) {
        int screenWidth = DensityUtil.getScreenWidth(context);

        int index = 0;
        for (int i = 0; i < widthArray.length; i++) {
            index = i;
            if (widthArray[i] >= screenWidth) {
                break;
            }
        }

        return sizeMap.get(widthArray[index]);
    }
}
