package will.wzhihu.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import will.wzhihu.R;

/**
 * @author wendeping
 */
public class AspectRatioFrameLayout extends FrameLayout {
    private float aspect;
    public AspectRatioFrameLayout(Context context) {
        this(context, null);
    }

    public AspectRatioFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs,
            R.styleable.AspectRatioFrameLayout,  defStyleAttr, 0);
        try {
            aspect = a.getFloat(R.styleable.AspectRatioFrameLayout_aspect, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (aspect != 0) {
            int measuredWidth = getMeasuredWidth();
            setMeasuredDimension(measuredWidth, (int)(measuredWidth / aspect));
        }
    }
}
