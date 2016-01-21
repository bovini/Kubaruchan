package com.example.genet42.kubaruchan.statistics;

import android.app.UiModeManager;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * CSVファイルの管理
 */
public class CSVManager {
    /**
     * csvファイルにおける日付の位置
     */
    private final static int INDEX_DAY = 0;

    /**
     * csvファイルにおける日付うーんの位置
     */
    private final static int INDEX_UMMM = 1;

    /**
     * csvファイルにおけるまあまあの位置
     */
    private final static int INDEX_SOSO = 2;

    /**
     * csvファイルにおけるよいの位置
     */
    private final static int INDEX_GOOD = 3;

    /**
     * 一ヶ月の最大の日数
     */
    public static final int MAX_DAYS = 31;

    /**
     *
     */
    public static final int COLUMN = 4;
    /**
     *評価値の最大値
     */
    public static final int MAX_EVALUATION = 40;
    /**
     * 外部ストレージ(SDカード)の場所
     */
    private final static File PATHreal = Environment.getExternalStorageDirectory();

    /**
     * テスト用ストレージ
     */
    private final static String PATH = "/data/data/Kubaruchan/files/";

    /**
     * インスタンス
     */
    private static CSVManager instance;

    private Context context;


    /**
     * 外から生成させないコンストラクタ
     */
    private CSVManager(Context context){
        this.context = context;

    }

    /**
     * インスタンスを返す
     *
     * @return  CSVManagerのインスタンス
     */
    public static synchronized CSVManager getInstance(Context context){
        if (instance == null){
            instance = new CSVManager(context);
        }
        instance.context = context;
        return instance;
    }

    /**
     * 評価をCSVファイルに追加する
     * @param calendar 日付
     * @param eval 評価
     */
    public void addEvaluation(Calendar calendar, Evaluation eval){

        SimpleDateFormat fnm = new SimpleDateFormat("yyyy_mm", Locale.JAPAN);
        SimpleDateFormat d = new SimpleDateFormat("dd", Locale.JAPAN);

        int day = Integer.parseInt(d.format(calendar.getTime()));
        String filename = fnm.format(calendar.getTime());

        updateCSVLocal(filename + ".csv", day, eval);
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
    private void updateCSV(String filepath, int day, Evaluation eval){

        File file = new File(filepath+"csv");
        try {
            if(!file.createNewFile()){
                overwrite(filepath, day, eval);
            }
            else{

            }

        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 受け取った評価を以下の形式のcsvファイルとして記録する
     * filename: yyyy_mm.csv
     * 記入規則: dd,[UMMM],[SOSO],[GOOD]
     * @param filename
     * @param day
     * @param eval
     */
    private void updateCSVLocal(String filename, int day, Evaluation eval){
        try{
            FileOutputStream fos = context.openFileOutput(filename,context.MODE_PRIVATE);
            fos.close();
            overwriteLocal(filename, day, eval);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * ファイルを上書きする
     *  @param filepath ファイルの保存先
     * @param day 日付
     * @param eval 評価
     */
    private void overwrite(String filepath, int day, Evaluation eval) throws IOException {

        int [][] toWrite = readCSV(filepath);
        initializeNullRows(toWrite);
        updateRow(day, eval, toWrite);
        writeAsCSV(filepath, toWrite);

    }

    /**
     *     /**
     * ファイルを上書きする
     * @param filename
     * @param day
     * @param eval
     * @throws IOException
     */
    private void overwriteLocal(String filename, int day, Evaluation eval) throws IOException {

        int [][] toWrite = readCSVLocal(filename);
        initializeNullRows(toWrite);
        updateRow(day, eval, toWrite);
        writeAsCSVLocal(filename, toWrite);

    }

    /**
     * 押されたボタンの評価をデータに反映させる
     * @param day 日付
     * @param eval 評価
     * @param toUpdate 反映先のデータ
     */
    private void updateRow(int day, Evaluation eval, int[][] toUpdate) {
        switch (eval){
            case UMMM:
                toUpdate[day - 1][INDEX_UMMM]++;
                break;
            case SOSO:
                toUpdate[day - 1][INDEX_SOSO]++;
                break;
            case GOOD:
                toUpdate[day - 1][INDEX_GOOD]++;
                break;
        }
    }

    /**
     * nullの行を初期化する
     * @param toInitialize 行を初期化する対象のデータ
     */
    private void initializeNullRows(int[][] toInitialize) {
        for(int i = 0; i < MAX_DAYS; i++){
            if(toInitialize[i] == null){
                toInitialize [i] = new int [COLUMN];
                toInitialize [i] [INDEX_DAY] = i + 1;
                toInitialize [i] [INDEX_UMMM] = 0;
                toInitialize [i] [INDEX_SOSO] = 0;
                toInitialize [i] [INDEX_GOOD] = 0;
            }
        }
    }

    /**
     * csvファイルを読み込み,[行][列]の形式で返す
     * @param filepath 読込先のファイル
     * @return 読み込んだファイル
     * @throws IOException
     */
    private int[][] readCSV(String filepath) throws IOException {
        int [][] results = new int[MAX_DAYS][];

        FileReader fr = new FileReader(filepath);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while (( line = br.readLine()) != null) {
            String [] result = line.split(",");
            int d = Integer.parseInt(result[INDEX_DAY]);
            results [ d - 1 ] = new int[result.length];
            for( int i = 0; i < result.length; i++ ) {
                results[ d - 1 ] [i] = Integer.parseInt(result[i]);
            }
        }
        return results;
    }
    /**
     * csvファイルを読み込み,[行][列]の形式で返す
     * @param filename 読込先のファイル
     * @return 読み込んだファイル
     * @throws IOException
     */
    private int[][] readCSVLocal(String filename){
        int [][] results = new int[MAX_DAYS][];
        FileInputStream fis;
        try {
            fis = context.openFileInput(filename);
            final BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while (( line = br.readLine()) != null) {
                String [] result = line.split(",");
                int d = Integer.parseInt(result[INDEX_DAY]);
                results [ d - 1 ] = new int[result.length];
                for( int i = 0; i < result.length; i++ ) {
                    results[ d - 1 ] [i] = Integer.parseInt(result[i]);
                }
            }

        }catch(IOException e){
            Log.e("readerr","エラーはファイル読み込みにて発生");
        }

        return results;
    }

    /**
     * 指定されたファイルパスに指定されたデータをcsvとして書き込む
     * @param filepath ファイルパス
     * @param toWrite 書き込むデータ([行][列]の形式で定義される)
     * @throws IOException
     */
    private void writeAsCSV(String filepath, int[][] toWrite) throws IOException{
        FileWriter fw = new FileWriter(filepath);
        for (int i = 0; i < toWrite.length; i++) {
            for (int j = 0; j < COLUMN; j++) {
                fw.write(Integer.toString(toWrite[i][j]));
                if (j < COLUMN - 1) {
                    fw.write(",");
                } else {
                    fw.write("\n");
                }
            }
        }
    }

    /**
     * 指定されたファイルパスに指定されたデータをcsvとして書き込む
     * @param filename
     * @param toWrite
     * @throws IOException
     */
    private void writeAsCSVLocal(String filename, int[][] toWrite) throws IOException{
        FileOutputStream fos = context.openFileOutput(filename,context.MODE_PRIVATE);
        for (int i = 0; i < toWrite.length; i++) {
            for (int j = 0; j < COLUMN; j++) {
                fos.write(Integer.toString(toWrite[i][j]).getBytes());
                if (j < COLUMN - 1) {
                    fos.write(",".getBytes());
                } else {
                    fos.write("\n".getBytes());
                }
            }
        }
    }

    /**
     * あるファイル名を入力するとそのファイルを元に作成したグラフのデータセットを返す
     * @param filename ファイル名
     * @return グラフ作成用のデータセット
     */
    public Statistics makeDataset(String filename){
        return getDataset(filename);
    }

    /**
     * 保存されているcsvファイルの一覧を返す
     * @return
     */
    public List<String> getCSVList(){
        List <String> csvList = new ArrayList<>();
        File [] csvFile = new File(PATH).listFiles();
        for(int i = 0; i < csvFile.length; i++){
            if(csvFile[i].isFile() && csvFile[i].getName().endsWith(".csv")){
                csvList.add(csvFile[i].getName());
            }
        }
        return csvList;
    }

    /**
     * 保存されているcsvファイルの一覧を返す
     * @return csvファイルの一覧
     */
    public List<String> getCSVListLocal(){
        List<String> csvFilenames = new ArrayList<>();
        for (String filename:context.fileList()) {
            if(filename.contains(".csv") ){
                csvFilenames.add(filename);
            }
        }
        return csvFilenames;
    }

    /**
     * あるcsvファイルのパスを入力するとグラフのデータセットを返す
     * @param filepath ファイルのパス
     * @return グラフ用のデータセット
     */
    private Statistics getDataset(String filepath){
        Statistics dataset = new Statistics();
        int evaluations[][] = new int[COLUMN][];
        try {
            evaluations = readCSV(filepath);
        }catch(IOException e){
            e.printStackTrace();
        }
        for(int i = 0; i< evaluations.length; i++){
            for(int j = 0; j< evaluations[i].length; j++){
                switch (j) {
                    case INDEX_SOSO:
                        dataset.addSoso(i, evaluations[i][j]);
                        break;
                    case INDEX_UMMM:
                        dataset.addUmmm(i, evaluations[i][j]);
                        break;
                    case INDEX_GOOD:
                        dataset.addGood(i, evaluations[i][j]);
                        break;

                }
            }
        }

        return dataset;
    }



}
