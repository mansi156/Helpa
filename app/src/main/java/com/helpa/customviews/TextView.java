package com.helpa.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.helpa.R;


public class TextView extends android.support.v7.widget.AppCompatTextView {
    public TextView(Context context) {
        super(context);
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Lato-Regular.ttf"));

    }

    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        Typeface myTypeface;
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextView);
            String fontName = a.getString(R.styleable.TextView_fontName);
            if (fontName == null) {
                myTypeface = Typeface.createFromAsset(getContext().getAssets(), "Lato-Regular.ttf");
                setTypeface(myTypeface);
            } else {
                if (fontName.equalsIgnoreCase("light")) {
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "Lato-Light.ttf");
                    setTypeface(myTypeface);
                } else if (fontName.equalsIgnoreCase("bold")) {
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "Lato-Bold.ttf");
                    setTypeface(myTypeface);
                } else if(fontName.equalsIgnoreCase("medium")) {
                    myTypeface = Typeface.createFromAsset(getContext().getAssets(), "Lato-Medium.ttf");
                    setTypeface(myTypeface);
                }
                a.recycle();
            }
        }


    }
}
