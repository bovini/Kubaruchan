package com.example.genet42.kubaruchan.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

/**
 * 色が変わるトグルボタン
 */
public class ColorfulToggleButton extends ToggleButton {

    public static final int COLOR_BG_CHECKED = 0xFF4BBDE7;
    public static final int COLOR_BG_UNCHECKED = 0xFFFF8F8F;

    public ColorfulToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(COLOR_BG_UNCHECKED);
    }

    public ColorfulToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(COLOR_BG_UNCHECKED);
    }

    public ColorfulToggleButton(Context context) {
        super(context);
        setBackgroundColor(COLOR_BG_UNCHECKED);
    }

    @Override
    public void setOnCheckedChangeListener(final OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onCheckedChanged(buttonView, isChecked);
                setBackgroundColor(isChecked ? COLOR_BG_CHECKED : COLOR_BG_UNCHECKED);
            }
        });
    }
}
