package com.example.genet42.kubaruchan.statistics;

import java.util.Calendar;

/**
 * アンケートの結果を管理するクラス
 */
public class StatisticsSystem {
    /**
     * インスタンス
     */
    private static StatisticsSystem instance;

    /**
     * CSVManager
     */

    /**
     * 外から生成させないコンストラクタ
     */
    private StatisticsSystem(){

    }

    /**
     * インスタンスを返す
     *
     * @return StatisticsSystemのインスタンス
     */
    public static synchronized StatisticsSystem getInstance(){
        if (instance == null) {
            instance = new StatisticsSystem();
        }
        return instance;
    }

    /**
     * 試食品の評価を登録する
     *
     * @param calendar 評価の日時
     * @param eval 評価の値
     */
    public void putCSV(Calendar calendar, Evaluation eval) {
        CSVManager.getInstance().addEvaluation(calendar, eval);
    }



}