package com.vanderfalken.spartanspotifyplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Josh on 4-5-2016.
 */
public class TextViewPlus extends TextView {

    public TextViewPlus (Context context) {
        super(context);
    }

    public TextViewPlus (Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    public TextViewPlus (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }
}