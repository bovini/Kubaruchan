package com.example.genet42.kubaruchan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.genet42.kubaruchan.statistics.CSVManager;
import com.example.genet42.kubaruchan.statistics.Statistics;

import java.util.Arrays;

public class StatisticsActivity extends AppCompatActivity {

    CSVManager cman;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        cman = CSVManager.getInstance(getApplicationContext());
        Spinner csvSelector = (Spinner) findViewById(R.id.CSVSelector);
        //初期グラフの表示
        setCsvSelector(csvSelector);
        spinList(csvSelector);
        //ドロップダウンリストをいじった時の処理
        csvSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                spinList((Spinner) parent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void spinList(Spinner spin) {
        Spinner spinner = spin;
        String item = (String) spinner.getSelectedItem();
        item = item.substring(0, 4) + "_" + item.substring(5, 7) + ".csv";
        setList(cman.makeDataset(item));
    }

    /**
     * スピナーに年月を入れる
     * @param csvSelector 年月を入れるスピナー
     */
    private void setCsvSelector(Spinner csvSelector){
        //setSpinner(csvSelector, (String[]) );
        setSpinner(csvSelector, Arrays.asList(cman.getCSVListLocal().toArray()).toArray(new String[cman.getCSVListLocal().toArray().length]));
    }

    /**
     * スピナーに文字列の配列を入れる
     * @param spinner 文字列を入れるスピナー
     * @param arr 文字列の配列
     */
    private void setSpinner(Spinner spinner, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i].substring(0, 4) + "年" + arr[i].substring(5, 7) + "月";
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * 評価を表にする
     * @param statistics 評価の情報
     */
    private void setList(Statistics statistics){

    }

}
