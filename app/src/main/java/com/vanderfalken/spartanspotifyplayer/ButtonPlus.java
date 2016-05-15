package com.vanderfalken.spartanspotifyplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Josh on 4-5-2016.
 */
public class ButtonPlus extends Button {

    public ButtonPlus(Context context) {
        super(context);
    }

    public ButtonPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    public ButtonPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }
}