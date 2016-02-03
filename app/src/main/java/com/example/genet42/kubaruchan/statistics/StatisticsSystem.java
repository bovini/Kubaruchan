package com.example.genet42.kubaruchan.statistics;

import android.content.Context;
import android.util.Log;

import java.util.Calendar;

/**
 * アンケートの結果を管理するクラス
 */
public class StatisticsSystem {
    /**
     * インスタンス
     */
    private static StatisticsSystem instance;

    private Context context;

    /**
     * CSVManager
     */

    /**
     * 外から生成させないコンストラクタ
     */
    private StatisticsSystem(Context context){
        this.context = context;
    }

    /**
     * インスタンスを返す
     *
     * @return StatisticsSystemのインスタンス
     */
    public static synchronized StatisticsSystem getInstance(Context context){
        if (instance == null) {
            instance = new StatisticsSystem(context);
        }
        instance.context = context;
        return instance;
    }

    /**
     * 試食品の評価を登録する
     *
     * @param calendar 評価の日時
     * @param eval 評価の値
     */
    public void putCSV(Calendar calendar, Evaluation eval) {
        Log.e("putCSV","呼び出し");
        CSVManager.getInstance(this.context).addEvaluation(calendar, eval);
    }



}