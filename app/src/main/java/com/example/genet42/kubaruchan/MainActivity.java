package com.example.genet42.kubaruchan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.genet42.kubaruchan.communication.WiPort;
import com.example.genet42.kubaruchan.statistics.Evaluation;
import com.example.genet42.kubaruchan.statistics.StatisticsSystem;
import com.example.genet42.kubaruchan.ui.ButtonListener;
import com.example.genet42.kubaruchan.ui.Indicator;

import java.net.InetAddress;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    /**
     * WiPortのIPアドレスの規定値
     */
    private static final String ADDR_WIPORT = "192.168.64.208";

    /**
     * WiPortのCPの状態設定用ポート番号の規定値
     */
    private static final int PORT_WIPORT_CP = 30704;

    /**
     * WiPort を確認する間隔 [ms]
     */
    public static final int PERIOD_CHECK_WIPORT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 統計システム
        final StatisticsSystem statisticsSystem = StatisticsSystem.getInstance(this);

        // やばいときに表示するところ
        TextView textView = (TextView) findViewById(R.id.textView);
        final Indicator indicator = new Indicator(textView);

        // わいぽーと
        InetAddress host = null;
        try {
            host = InetAddress.getByName(ADDR_WIPORT);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        final WiPort wiPort = new WiPort(host, PORT_WIPORT_CP, PERIOD_CHECK_WIPORT);
        wiPort.setOnEmergencyChangeListener(new WiPort.EmergencyChangeListener() {
            @Override
            public void onEmergencyChanged(boolean isEmergency) {
                Log.d("onEmergencyChange", Boolean.toString(isEmergency));
                indicator.updateIndication(isEmergency);
            }
        });
        wiPort.setOnEvaluationListener(new WiPort.EvaluationListener() {
            @Override
            public void onEvaluation(Evaluation evaluation) {
                Log.d("onEvaluation", evaluation.toString());
                statisticsSystem.putCSV(Calendar.getInstance(), evaluation);
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

        // アンケート結果
        Button statButton = (Button) findViewById(R.id.button);
        statButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
                startActivity(intent);
                return false;
            }
        });

        // TEST
        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnTouchListener(new ButtonListener() {
            @Override
            protected void onTouchDown() {
                wiPort.setTest(true);
            }

            @Override
            protected void onTouchUp() {
                wiPort.setTest(false);
            }
        });
    }
}
