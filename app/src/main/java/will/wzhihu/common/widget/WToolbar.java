package will.wzhihu.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import will.wzhihu.R;

public class WToolbar extends Toolbar {
    private int mMenuRightMargin;

    private CharSequence mHeaderText;

    private CharSequence mSubHeaderText;

    private LinearLayout mHeaderContainer;

    private TextView mHeaderView;

    private TextView mSubHeaderView;

    protected int mHeaderTextAppearance;

    private int mSubHeaderTextAppearance;

    protected int mHeaderTitleTextColor;

    private int mSubHeaderTitleTextColor;

    private CharSequence mMenuText;

    private int mMenuTextAppearance;

    private int mMenuTextColor;

    private TextView mMenuView;

    private boolean mMenuEnable;

    public WToolbar(Context context) {
        this(context, null);
    }

    public WToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public WToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WToolbar,  defStyleAttr, 0);
        try {
            mHeaderText = a.getString(R.styleable.WToolbar_headerTitle);
            mHeaderTextAppearance = a.getResourceId(R.styleable.WToolbar_headerTitleTextAppearance, 0);
            mHeaderTitleTextColor = a.getColor(R.styleable.WToolbar_headerTitleTextColor, 0);

            mSubHeaderText = a.getString(R.styleable.WToolbar_subHeaderTitle);
            mSubHeaderTextAppearance = a.getResourceId(R.styleable.WToolbar_subHeaderTitleTextAppearance, 0);
            mSubHeaderTitleTextColor = a.getColor(R.styleable.WToolbar_subHeaderTitleTextColor, 0);

            mMenuText = a.getString(R.styleable.WToolbar_menuText);
            mMenuTextAppearance = a.getResourceId(R.styleable.WToolbar_menuTextAppearance, 0);
            mMenuTextColor = a.getColor(R.styleable.WToolbar_menuTextColor, 0);
            mMenuRightMargin = (int) a.getDimension(R.styleable.WToolbar_menuTextRightMargin, 0);

            mMenuEnable = a.getBoolean(R.styleable.WToolbar_menuEnable, true);
            if (!TextUtils.isEmpty(mHeaderText)) {
                setHeaderTitle(mHeaderText);
            }

            if (!TextUtils.isEmpty(mSubHeaderText)) {
                setSubHeaderTitle(mSubHeaderText);
            }

            if (!TextUtils.isEmpty(mMenuText)) {
                setMenuText(mMenuText);
            }
        } finally {
            a.recycle();
        }
    }
    public void setHeaderTitle(int resId) {
        this.setHeaderTitle(getContext().getString(resId));
    }

    public void setMenuText(CharSequence menu) {
        if (!TextUtils.isEmpty(menu)) {
            mMenuView = getMenuView();
        } else if (mMenuView != null && mMenuView.getParent() != null) {
            removeView(mMenuView);
        }
        if (mMenuView != null) {
            mMenuView.setText(menu);
        }
        mMenuText = menu;
    }

    public void setHeaderTitle(CharSequence header) {
        if (!TextUtils.isEmpty(header)) {
            mHeaderView = getHeaderView();
        } else if (mHeaderView != null && mHeaderView.getParent() != null) {
            removeView(mHeaderView);
        }
        if (mHeaderView != null) {
            mHeaderView.setText(header);
        }
        mHeaderText = header;
    }

    public void setSubHeaderTitle(CharSequence subHeaderText) {
        if (!TextUtils.isEmpty(subHeaderText)) {
            mSubHeaderView = getSubHeaderView();
        } else if (mSubHeaderView != null && mSubHeaderView.getParent() != null) {
            removeHeaderView(mSubHeaderView);
        }

        if (mSubHeaderView != null) {
            mSubHeaderView.setText(subHeaderText);
        }
        mSubHeaderText = subHeaderText;
    }

    private TextView generateMenuView() {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER | Gravity.RIGHT);
        if (mMenuRightMargin != 0) {
            layoutParams.rightMargin = mMenuRightMargin;
        }
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    protected TextView generateHeaderView() {
        TextView textView = new TextView(getContext());
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new LayoutParams(Gravity.CENTER));
        return textView;
    }

    public TextView getMenuView() {
        if (mMenuView == null) {
            mMenuView = generateMenuView();
            if (mMenuTextAppearance != 0) {
                mMenuView.setTextAppearance(getContext(), mMenuTextAppearance);
            }

            mMenuView.setEnabled(mMenuEnable);
            if (mMenuTextColor != 0) {
                mMenuView.setTextColor(mMenuTextColor);
            }

        }

        if (mMenuView.getParent() == null) {
            addView(mMenuView, 0);
        }
        return mMenuView;
    }

    public TextView getHeaderView() {
        if (mHeaderView == null) {
            mHeaderView = generateHeaderView();
            if (mHeaderTextAppearance != 0) {
                mHeaderView.setTextAppearance(getContext(), mHeaderTextAppearance);
            }
            if (mHeaderTitleTextColor != 0) {
                mHeaderView.setTextColor(mHeaderTitleTextColor);
            }

        }

        if (mHeaderView.getParent() == null) {
            addHeaderView(mHeaderView, 0);
        }
        return mHeaderView;
    }

    public TextView getSubHeaderView() {
        if (mSubHeaderView == null) {
            mSubHeaderView = generateHeaderView();
            Typeface typeface = mSubHeaderView.getTypeface();
            if (mSubHeaderTextAppearance != 0) {
                mSubHeaderView.setTextAppearance(getContext(), mSubHeaderTextAppearance);
                if (null != typeface) {
                    mSubHeaderView.setTypeface(typeface);
                }
            }

            if (mSubHeaderTitleTextColor != 0) {
                mSubHeaderView.setTextColor(mSubHeaderTitleTextColor);
            }
        }

        if (mSubHeaderView.getParent() == null) {
            addHeaderView(mSubHeaderView, 1);
        }

        return mSubHeaderView;
    }

    protected void addHeaderView(View view, int position) {
        if (null == mHeaderContainer) {
            mHeaderContainer = new LinearLayout(getContext());
            mHeaderContainer.setOrientation(LinearLayout.VERTICAL);
            mHeaderContainer.setGravity(Gravity.CENTER_HORIZONTAL);
            mHeaderContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        }

        if (mHeaderContainer.getParent() == null) {
            addView(mHeaderContainer);
        }

        mHeaderContainer.addView(view, position);
    }

    private void removeHeaderView(View view) {
        mHeaderContainer.removeView(view);
    }

    public void setTitleTextAppearance(Context context, int resId) {
        mHeaderTextAppearance = resId;
        if (mHeaderView != null) {
            mHeaderView.setTextAppearance(context, resId);
        }
    }

    public void setTitleTextColor(int color) {
        if (mHeaderView != null) {
            mHeaderView.setTextColor(color);
        }
    }

    public void setOnMenuClickListener(OnClickListener onMenuClickListener) {
        this.mMenuView.setOnClickListener(onMenuClickListener);
    }

    public void setMenuEnable(boolean enable) {
        mMenuView.setEnabled(enable);
    }
}
