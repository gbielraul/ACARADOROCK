package com.podekrast.acaradorock.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.podekrast.acaradorock.R;

public class LoadingButton extends ConstraintLayout {

    private Context context;

    private TextView textView;
    private ProgressBar progressBar;

    public LoadingButton(@NonNull Context context) {
        super(context);
        this.context = context;
        setup(context, null);
    }

    public LoadingButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setup(context, attrs);
    }

    private void setup(Context context, AttributeSet attrs) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton);

        try {
            layoutInflater.inflate(R.layout.loading_button, this, true);

            textView = (TextView) findViewById(R.id.text_view_loading_button);
            progressBar = (ProgressBar) findViewById(R.id.progress_bar_loading_button);


            String text = attributes.getString(R.styleable.LoadingButton_text);
            boolean textAllCaps = attributes.getBoolean(R.styleable.LoadingButton_textAllCaps, false);
            int textColor = attributes.getColor(R.styleable.LoadingButton_textColor, ContextCompat.getColor(context, android.R.color.white));
            boolean progressBarEnabled = attributes.getBoolean(R.styleable.LoadingButton_progressBarEnabled, false);

            progressBarEnabled(progressBarEnabled);
            setProgressBarColor();

            this.setText(text);
            this.textAllCaps(textAllCaps);
            this.setTextColor(textColor);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            attributes.recycle();
        }
    }

    public void setText(CharSequence text) {
        textView.setText(text);
    }

    public void setText(@StringRes int text) {
        textView.setText(text);
    }

    public void textAllCaps(boolean allCaps) {
        String text;
        if (allCaps)
            text = textView.getText().toString().toUpperCase();
        else {
            text = textView.getText().toString().toLowerCase();
        }
        textView.setText(text);
    }

    public void setTextColor(@ColorInt int color) {
        textView.setTextColor(color);
    }

    public void progressBarEnabled(boolean enabled) {
        progressBar.setVisibility(enabled ? VISIBLE : GONE);
        textView.setVisibility(enabled ? GONE : VISIBLE);
    }

    private void setProgressBarColor() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable drawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(drawable, ContextCompat.getColor(context, android.R.color.white));

            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(drawable));
        } else {
            progressBar.getIndeterminateDrawable()
                    .setColorFilter(
                            ContextCompat.getColor(context, android.R.color.white),
                            PorterDuff.Mode.SRC_IN
                    );
        }
    }

    public void setOnClickListener(@Nullable OnClickListener l) {
        ((ImageView) findViewById(R.id.bg_loading_button)).setOnClickListener(l);
    }
}
