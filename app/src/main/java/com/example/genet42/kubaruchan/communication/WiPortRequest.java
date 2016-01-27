package com.example.genet42.kubaruchan.communication;

import android.util.Log;

import com.example.genet42.kubaruchan.statistics.Evaluation;

/**
 * わいぽーとに模型車の有効/無効を設定して評価値と模型車が緊急かどうかを取得するためのリクエスト
 */
public class WiPortRequest {
    /**
     * 模型車の動作の有効/無効
     */
    private boolean vehicleActive = false;

    /**
     * このリクエストが終了しているかどうか
     */
    private boolean done = false;

    /**
     * 評価値
     */
    private static Evaluation evaluation;
    private static Evaluation[] evals = {
        Evaluation.NULL,
        Evaluation.GOOD,
        Evaluation.NULL,
        Evaluation.NULL,
        Evaluation.SOSO,
        Evaluation.SOSO,
        Evaluation.NULL,
        Evaluation.UMMM,
        Evaluation.GOOD
    };
    private static int index = 0;

    /**
     * 模型車が緊急かどうか
     */
    private static boolean emergency;

    /**
     * 模型車の動作の有効/無効を指定する．
     *
     * @param vehicleActive 模型車の動作の有効/無効
     */
    public void setVehicleActive(boolean vehicleActive) {
        if (done) {
            throw new IllegalStateException("this request has already done.");
        }
        this.vehicleActive = vehicleActive;
    }

    /**
     * リクエストを送信する
     */
    public void send() {
        if (done) {
            throw new IllegalStateException("this request has already done.");
        }
        emergency = !emergency;
        evaluation = evals[index];
        index = ++index == evals.length ? 0 : index;
        done = true;
    }

    /**
     * 模型車が緊急かどうかを取得
     *
     * @return 模型車が緊急かどうか
     */
    public boolean isEmergency() {
        if (!done) {
            throw new IllegalStateException("this request has not sent yet.");
        }
        return emergency;
    }

    /**
     * 評価値を取得
     *
     * @return 評価値
     */
    public Evaluation getEvaluation() {
        if (!done) {
            throw new IllegalStateException("this request has not sent yet.");
        }
        return evaluation;
    }
}
