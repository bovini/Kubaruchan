package com.example.genet42.kubaruchan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.genet42.kubaruchan.statistics.Evaluation;
import com.example.genet42.kubaruchan.statistics.StatisticsSystem;

import java.util.Calendar;

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final Button umai = (Button) findViewById(R.id.GOOD);
        final Button mazumazu = (Button) findViewById(R.id.SOSO);
        final Button mazui = (Button) findViewById(R.id.UMMM);
        final Button finish = (Button) findViewById(R.id.returnButton);
        final StatisticsSystem ss = StatisticsSystem.getInstance(getApplicationContext());

        umai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ss.putCSV(Calendar.getInstance(), Evaluation.GOOD);
            }
        });

        mazumazu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ss.putCSV(Calendar.getInstance(), Evaluation.SOSO);
            }
        });

        mazui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ss.putCSV(Calendar.getInstance(), Evaluation.UMMM);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
