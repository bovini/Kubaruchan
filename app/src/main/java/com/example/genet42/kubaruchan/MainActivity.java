package com.example.genet42.kubaruchan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.genet42.kubaruchan.statistics.StatisticsSystem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatisticsActivity sa = new StatisticsActivity();
        Button test = (Button) findViewById(R.id.Testbutton);
        Button statistics = (Button) findViewById(R.id.statistics);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent test = new Intent(MainActivity.this, TestActivity.class);
                startActivity(test);
            }
        });

        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statistic = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(statistic);
            }
        });
    }
}
