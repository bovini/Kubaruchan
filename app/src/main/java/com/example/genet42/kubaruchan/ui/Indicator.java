package com.example.genet42.kubaruchan.ui;

import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * 模型車がやばいときにその旨を表示する TextView まわりのあれこれ
 */
public class Indicator {
    private static final int COLOR_TEXT_EMERGENCY = 0xFF000000;
    private static final int COLOR_TEXT_PEACETIME = 0xFF808080;
    private static final int COLOR_BG_EMERGENCY = 0xFFFF0000;
    private static final int COLOR_BG_PEACETIME = 0xFFFFFFFF;
    private static final String TEXT_PEACETIME = "通知があるとここに表示されます";
    private static final String TEXT_EMERGENCY = "試食品がありません\nor\n転倒の可能性あり";

    private final Handler handler = new Handler();

    /**
     * 模型車がやばいときにその旨を表示するための TextView
     */
    TextView textView;

    /**
     * TextView を指定してインジケータを作成する．
     *
     * @param textView 模型車がばいときにその旨を表示するための TextView
     */
    public Indicator(TextView textView) {
        this.textView = textView;
        updateIndication(false);
    }

    /**
     * 表示を更新する
     *
     * @param isEmergency 模型車がやばいかどうか
     */
    public void updateIndication(final boolean isEmergency) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                // 色合いを更新
                textView.setTextColor(isEmergency ? COLOR_TEXT_EMERGENCY : COLOR_TEXT_PEACETIME);
                textView.setBackgroundColor(isEmergency ? COLOR_BG_EMERGENCY : COLOR_BG_PEACETIME);
                // 文字列を更新
                textView.setText(isEmergency ? TEXT_EMERGENCY : TEXT_PEACETIME);
                // 文字の大きさを更新
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, isEmergency ? 80 : 30);
            }
        });
    }
}
