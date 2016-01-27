package com.example.genet42.kubaruchan.ui;

import android.view.MotionEvent;
import android.view.View;

/**
 * ボタンのタッチの開始と終了をとるリスナ
 */
public abstract class ButtonListener implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            onTouchDown();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            onTouchUp();
        }
        return false;
    }

    /**
     * ボタンのタッチが開始されたときに呼ばれる
     */
    protected abstract void onTouchDown();

    /**
     * ボタンのタッチが終了したときに呼ばれる
     */
    protected abstract void onTouchUp();
}
