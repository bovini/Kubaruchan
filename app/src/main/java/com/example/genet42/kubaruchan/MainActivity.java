package com.example.genet42.kubaruchan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.genet42.kubaruchan.communication.WiPort;
import com.example.genet42.kubaruchan.statistics.Evaluation;
import com.example.genet42.kubaruchan.statistics.StatisticsSystem;
import com.example.genet42.kubaruchan.ui.Indicator;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    /**
     * WiPort を確認する間隔 [ms]
     */
    public static final int PERIOD_CHECK_WIPORT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 統計システム
        final StatisticsSystem statisticsSystem = StatisticsSystem.getInstance();

        // やばいときに表示するところ
        TextView textView = (TextView) findViewById(R.id.textView);
        final Indicator indicator = new Indicator(textView);

        // わいぽーと
        final WiPort wiPort = new WiPort(PERIOD_CHECK_WIPORT);
        wiPort.setOnEmergencyChangeListener(new WiPort.EmergencyChangeListener() {
            @Override
            public void onEmergencyChanged(boolean isEmergency) {
                indicator.updateIndication(isEmergency);
            }
        });
        wiPort.setOnEvaluationListener(new WiPort.EvaluationListener() {
            @Override
            public void onEvaluation(Evaluation evaluation) {
                Log.d("onEvaluation", evaluation.toString());
                statisticsSystem.put(Calendar.getInstance(), evaluation);
            }
        });

        // 動作の開始・停止ボタン
        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wiPort.setActive(isChecked);
            }
        });
    }
}
