package com.podekrast.acaradorock.config;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ProgressBar;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;

public class App {
    public static final double APP_VERSION = 2;

    public static void setProgressBarColor(ProgressBar progressBar, @ColorInt int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(drawable, color);
            progressBar.setIndeterminateDrawable(drawable);
        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }
}
