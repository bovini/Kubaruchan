package com.example.genet42.kubaruchan.statistics;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * CSVファイルの管理
 */
public class CSVManager {
    /**
     * 外部ストレージ(SDカード)の場所
     */
    private final static File PATH = Environment.getExternalStorageDirectory();

    /**
     * インスタンス
     */
    private static CSVManager instance = new CSVManager();


    /**
     * 外から生成させないコンストラクタ
     */
    private CSVManager(){

    }

    /**
     * インスタンスを返す
     *
     * @return  CSVManagerのインスタンス
     */
    public static synchronized CSVManager getInstance(){
        if (instance == null){
            instance = new CSVManager();
        }
        return instance;
    }

    /**
     * 評価をCSVファイルに追加する
     * @param calendar 日付
     * @param eval 評価
     */
    public void addEvalaution(Calendar calendar, Evaluation eval){
        SimpleDateFormat fnm = new SimpleDateFormat("yyyy_mm", Locale.JAPAN);
        SimpleDateFormat d = new SimpleDateFormat("dd", Locale.JAPAN);
        String filename = fnm.format(calendar.getTime());
        String day = d.format(calendar.getTime());
        updateCSV(PATH + filename, day ,eval);
    }

    /**
     * 受け取った評価を以下の形式のcsvファイルとして記録する
     * filename: yyyy_mm.csv
     * 記入規則: dd,[UMMM],[SOSO],[GOOD]
     *
     * @param filepath ファイルの保存先
     * @param day 日付
     * @param eval 評価
     */
    private void updateCSV(String filepath, String day, Evaluation eval){
        File file = new File(filepath+"csv");
        boolean fileexist;
        fileexist = false;
        try {
            fileexist = !file.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
        if(fileexist){
            //すでにファイルがあった場合の処理
            /*
            ファイルを開いて日付を確認し，受け取った日付と同じ日付の行があればその行の評価を更新する
            同じ日付の行でなければ新たな行に評価を追記する
             */
        }
        else{
            //ファイルがなかった場合の処理
            /*
            作成したファイルを開き，新たに評価を記入する
             */
        }
    }
}
