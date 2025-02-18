package com.tungsten.fcllibrary.component.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.tungsten.fclcore.fakefx.beans.property.BooleanProperty;
import com.tungsten.fclcore.fakefx.beans.property.BooleanPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.IntegerProperty;
import com.tungsten.fclcore.fakefx.beans.property.IntegerPropertyBase;
import com.tungsten.fclcore.fakefx.beans.property.ObjectProperty;
import com.tungsten.fclcore.fakefx.beans.property.ObjectPropertyBase;
import com.tungsten.fclcore.task.Schedulers;
import com.tungsten.fcllibrary.R;
import com.tungsten.fcllibrary.component.theme.ThemeEngine;
import com.tungsten.fcllibrary.util.ConvertUtils;

public class FCLImageButton extends AppCompatImageButton {

    private ObjectProperty<Drawable> image;
    private boolean autoTint;
    private boolean noPadding;
    private boolean useThemeColor;
    private BooleanProperty visibilityProperty;
    private BooleanProperty disableProperty;

    private final IntegerProperty theme = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
            refreshStyle();
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "theme";
        }
    };

    private final IntegerProperty theme2 = new IntegerPropertyBase() {

        @Override
        protected void invalidated() {
            get();
            refreshStyle();
        }

        @Override
        public Object getBean() {
            return this;
        }

        @Override
        public String getName() {
            return "theme2";
        }
    };

    public void refreshStyle() {
        int[][] state = {
                {

                }
        };
        int[] colorSrc = {
                ThemeEngine.getInstance().getTheme().getAutoTint()
        };
        int[] colorRipple = {
                ThemeEngine.getInstance().getTheme().getLtColor()
        };
        if (autoTint) {
            setImageTintList(new ColorStateList(state, colorSrc));
        }
        if (useThemeColor && getDrawable() != null) {
            getDrawable().setTint(ThemeEngine.getInstance().getTheme().getColor2());
        }
        RippleDrawable drawable = new RippleDrawable(new ColorStateList(state, colorRipple), null, null);
        drawable.setRadius(ConvertUtils.dip2px(getContext(), noPadding ? 12 : 20));
        setBackgroundDrawable(drawable);
    }

    private void init() {
        if (!noPadding) {
            setPadding(
                    ConvertUtils.dip2px(getContext(), 8f),
                    ConvertUtils.dip2px(getContext(), 8f),
                    ConvertUtils.dip2px(getContext(), 8f),
                    ConvertUtils.dip2px(getContext(), 8f)
            );
        } else {
            setPadding(0, 0, 0, 0);
        }
        setScaleType(ScaleType.FIT_XY);
    }

    public FCLImageButton(@NonNull Context context) {
        super(context);
        init();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        theme2.bind(ThemeEngine.getInstance().getTheme().color2Property());
    }

    public FCLImageButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageButton);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageButton_auto_tint, false);
        noPadding = typedArray.getBoolean(R.styleable.FCLImageButton_no_padding, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLImageButton_use_theme_color, false);
        typedArray.recycle();
        init();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        theme2.bind(ThemeEngine.getInstance().getTheme().color2Property());
    }

    public FCLImageButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FCLImageButton);
        autoTint = typedArray.getBoolean(R.styleable.FCLImageButton_auto_tint, false);
        noPadding = typedArray.getBoolean(R.styleable.FCLImageButton_no_padding, false);
        useThemeColor = typedArray.getBoolean(R.styleable.FCLImageButton_use_theme_color, false);
        typedArray.recycle();
        init();
        theme.bind(ThemeEngine.getInstance().getTheme().colorProperty());
        theme2.bind(ThemeEngine.getInstance().getTheme().color2Property());
    }

    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
        refreshStyle();
    }

    public boolean isAutoTint() {
        return autoTint;
    }

    public void setNoPadding(boolean noPadding) {
        this.noPadding = noPadding;
        refreshStyle();
    }

    public boolean isNoPadding() {
        return noPadding;
    }

    public void setUseThemeColor(boolean useThemeColor) {
        this.useThemeColor = useThemeColor;
        refreshStyle();
    }

    public boolean isUseThemeColor() {
        return useThemeColor;
    }

    public final void setImage(Drawable drawable) {
        imageProperty().set(drawable);
    }

    public final Drawable getImage() {
        return image == null ? null : image.get();
    }

    public final ObjectProperty<Drawable> imageProperty() {
        if (image == null) {
            image = new ObjectPropertyBase<Drawable>() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        Drawable drawable = get();
                        setImageDrawable(drawable);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "image";
                }
            };
        }

        return this.image;
    }

    public final void setVisibilityValue(boolean visibility) {
        visibilityProperty().set(visibility);
    }

    public final boolean getVisibilityValue() {
        return visibilityProperty == null || visibilityProperty.get();
    }

    public final BooleanProperty visibilityProperty() {
        if (visibilityProperty == null) {
            visibilityProperty = new BooleanPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        boolean visible = get();
                        setVisibility(visible ? VISIBLE : GONE);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "visibility";
                }
            };
        }

        return visibilityProperty;
    }

    public final void setDisableValue(boolean disableValue) {
        disableProperty().set(disableValue);
    }

    public final boolean getDisableValue() {
        return disableProperty == null || disableProperty.get();
    }

    public final BooleanProperty disableProperty() {
        if (disableProperty == null) {
            disableProperty = new BooleanPropertyBase() {

                public void invalidated() {
                    Schedulers.androidUIThread().execute(() -> {
                        boolean disable = get();
                        setEnabled(!disable);
                    });
                }

                public Object getBean() {
                    return this;
                }

                public String getName() {
                    return "disable";
                }
            };
        }

        return disableProperty;
    }
}
