package com.example.genet42.kubaruchan.communication;

import com.example.genet42.kubaruchan.statistics.Evaluation;

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
     * 緊急かどうか
     */
    private boolean emergency= false;

    /**
     * 評価値
     */
    private Evaluation evaluation = Evaluation.NULL;

    /**
     * CP の確認間隔を指定して生成．
     *
     * @param period CP を確認する間隔 [ms]
     */
    public WiPort(int period) {

        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                WiPortRequest request = new WiPortRequest();
                // 模型車の動作が有効かどうかの設定
                request.setVehicleActive(active);
                // 通信
                request.send();
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
        }, 0, period);
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
}
