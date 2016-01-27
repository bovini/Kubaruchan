package com.example.genet42.kubaruchan.statistics;

import android.text.TextUtils;

/**
 *試食品の評価の値を示す
 */
public enum Evaluation {
    /**
     * どの評価値でもない
     */
    NULL(0),

    /**
     * うーん
     */
    UMMM(1),

    /**
     * ふつう
     */
    SOSO(2),

    /**
     * うまい
     */
    GOOD(3);

    private int value;

    Evaluation(int value) {
        this.value = value;
    }

    public static Evaluation toEvaluation(int value) {
        for (Evaluation eval : values()) {
            if (eval.value == value) {
                return eval;
            }
        }
        return Evaluation.NULL;
    }
}
