package com.example.genet42.kubaruchan.communication;

import android.util.Log;

import com.example.genet42.kubaruchan.statistics.Evaluation;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;

/**
 * わいぽーと（模型車の WiFi インタフェース）
 */
public class WiPort {
    /**
     * Interface definition for a callback to be invoked when the state of emergency has been changed.
     */
    public interface EmergencyChangeListener {
        /**
         * Called when the state of emergency has been changed.
         */
        void onEmergencyChanged(boolean isEmergency);
    }

    /**
     * Interface definition for a callback to be invoked when the evaluation has been voted.
     */
    public interface EvaluationListener {
        /**
         * Called when the evaluation has been voted.
         */
        void onEvaluation(Evaluation evaluation);
    }

    private EmergencyChangeListener emergencyChangeListener;
    private EvaluationListener evaluationListener;

    /**
     * 模型車の動作が有効かどうか．
     */
    private boolean active = false;

    /**
     * WiPort上のテスト用のLEDを点灯させるかどうか
     */
    private boolean test = false;

    /**
     * 緊急かどうか
     */
    private boolean emergency= false;

    /**
     * 評価値
     */
    private Evaluation evaluation = Evaluation.NULL;

    /**
     * リクエストのための TimerTask
     */
    private class RequestTimerTask extends TimerTask {
        private final InetAddress address;
        private final int port;
        private final int timeout;

        public RequestTimerTask(InetAddress address, int port, int timeout) {
            this.address = address;
            this.port = port;
            this.timeout = timeout;
            Log.v("RequestTimerTask", "create");
        }

        @Override
        public void run() {
            Log.v("RequestTimerTask", "run");
            WiPortRequest request = new WiPortRequest(address, port);
            // 模型車の動作が有効かどうかの設定
            request.setVehicleActive(active);
            // テスト用LEDの点灯状態の設定
            request.setLEDTest(test);
            // 通信
            try {
                request.send(timeout);
            } catch (Exception e) {
                Log.e("WiPortRequest", e.toString());
                return;
            }
            // 緊急かどうかを取得して値に変化があれば通知
            boolean emgcy = request.isEmergency();
            if (emgcy != emergency) {
                emergencyChangeListener.onEmergencyChanged(emgcy);
            }
            emergency = emgcy;
            // 評価値を取得して値が NULL から変化したとき(どれかが押されたとき)だけ通知
            Evaluation eval = request.getEvaluation();
            if (evaluation == Evaluation.NULL && eval != Evaluation.NULL) {
                evaluationListener.onEvaluation(eval);
            }
            evaluation = eval;
        }
    }

    /**
     * WiPortのIPアドレスとリモートアドレスおよび CP の確認間隔を指定して生成．
     *
     * @param address IPアドレス.
     * @param port ポート番号.
     * @param period CP を確認する間隔 [ms]
     * @param timeout タイムアウト [ms]
     */
    public WiPort(InetAddress address, int port, int period, int timeout) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new RequestTimerTask(address, port, timeout), 0, period);
    }

    /**
     * Register a callback to be invoked when the state of emergency has been changed.
     *
     * @param listener the callback that will be run
     */
    public void setOnEmergencyChangeListener(EmergencyChangeListener listener)
    {
        emergencyChangeListener = listener;
    }

    /**
     * Register a callback to be invoked when the evaluation has been voted.
     *
     * @param listener the callback that will be run
     */
    public void setOnEvaluationListener(EvaluationListener listener)
    {
        evaluationListener = listener;
    }

    /**
     * 模型車の動作を有効または無効にする．
     *
     * @param active true で模型車の動作が有効
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * WiPort上のテスト用LEDを点灯または滅灯させる．
     *
     * @param test true で LED が点灯
     */
    public void setTest(boolean test) {
        this.test = test;
    }
}
