package will.wzhihu.common.utils;


import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class DensityUtil {  
  

    public static float getDensity(Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return scale;
    }  
    
    public static int dp2px(final Context context, final float dpValue) {
        return dp2px(context.getResources(), dpValue);
    }

    public static int dp2px(final Resources resources, final float dpValue) {
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.getDisplayMetrics()) + 0.5f);
    }

	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager)(context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager)(context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
	}
}  
