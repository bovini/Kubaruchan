package com.example.genet42.kubaruchan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.Color;

import com.example.genet42.kubaruchan.statistics.CSVManager;
import com.example.genet42.kubaruchan.statistics.Statistics;

import java.util.Arrays;

public class StatisticsActivity extends AppCompatActivity {

    CSVManager cman;
    TableLayout List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        cman = CSVManager.getInstance(getApplicationContext());
        Spinner csvSelector = (Spinner) findViewById(R.id.CSVSelector);
        Button returnButton= (Button) findViewById(R.id.returnButton);
        List = (TableLayout) findViewById(R.id.tableLayout);
        //初期グラフの表示
        Log.e("Stav","open");
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
        returnButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();


            }

        });
    }

    private void spinList(Spinner spin){
        Spinner spinner = spin;
        String item = (String) spinner.getSelectedItem();
        Log.e("spin","start");
        if(item != null) {
            item = item.substring(0, 4) + "_" + item.substring(5, 7) + ".csv";
            setList(cman.makeDataset(item));
        }
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * 評価を表にする
     * @param statistics 評価の情報
     */
    private void setList(Statistics statistics){
        List.removeAllViews();




        for(int i = 0; i < CSVManager.MAX_DAYS + 1;i++){
            if(i==0) {
                TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);
                TextView day = (TextView) tableRow.findViewById(R.id.rowtext1);
                day.setText("日付");
                day.setTextSize(25);
                day.setBackgroundColor(Color.BLACK);
                day.setTextColor(Color.WHITE);
                TextView good = (TextView) tableRow.findViewById(R.id.rowtext2);
                good.setText("うまい");
                good.setTextSize(25);
                good.setTextColor(Color.WHITE);
                good.setBackgroundColor(Color.RED);
                TextView soso = (TextView) tableRow.findViewById(R.id.rowtext3);
                soso.setText("ふつう");
                soso.setTextSize(25);
                soso.setTextColor(Color.WHITE);
                soso.setBackgroundColor(Color.GREEN);
                TextView ummm = (TextView) tableRow.findViewById(R.id.rowtext4);
                ummm.setText("まずい");
                ummm.setTextSize(25);
                ummm.setTextColor(Color.WHITE);
                ummm.setBackgroundColor(Color.BLUE);
                List.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            else {
                TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.table_row, null);
                TextView day = (TextView) tableRow.findViewById(R.id.rowtext1);
                day.setText(String.valueOf(i));
                if(i % 2 == 1)
                    day.setBackgroundColor(Color.GRAY);
                TextView good = (TextView) tableRow.findViewById(R.id.rowtext2);
                good.setText(String.valueOf(statistics.getGood(i)));
                if(i % 2 == 1)
                    good.setBackgroundColor(Color.GRAY);
                TextView soso = (TextView) tableRow.findViewById(R.id.rowtext3);
                soso.setText(String.valueOf(statistics.getSoso(i)));
                if(i % 2 == 1)
                    soso.setBackgroundColor(Color.GRAY);
                TextView ummm = (TextView) tableRow.findViewById(R.id.rowtext4);
                ummm.setText(String.valueOf(statistics.getUmmm(i)));
                if(i % 2 == 1)
                    ummm.setBackgroundColor(Color.GRAY);
                List.addView(tableRow, new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }


    }
}