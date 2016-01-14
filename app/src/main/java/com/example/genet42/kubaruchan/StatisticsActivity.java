package com.example.genet42.kubaruchan;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.genet42.kubaruchan.statistics.CSVManager;
import com.example.genet42.kubaruchan.statistics.GraphView;

import org.afree.chart.AFreeChart;
import org.afree.chart.ChartFactory;
import org.afree.chart.axis.CategoryAxis;
import org.afree.chart.axis.CategoryLabelPositions;
import org.afree.chart.axis.NumberAxis;
import org.afree.chart.plot.CategoryPlot;
import org.afree.chart.plot.PlotOrientation;
import org.afree.chart.renderer.category.BarRenderer;
import org.afree.data.category.CategoryDataset;
import org.afree.data.category.DefaultCategoryDataset;
import org.afree.graphics.GradientColor;
import org.afree.graphics.SolidColor;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;

public class StatisticsActivity extends AppCompatActivity {

    CSVManager cman;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        cman = CSVManager.getInstance(getApplicationContext());
        final GraphView spcv = (GraphView) findViewById(R.id.graphView1);
        Spinner csvSelector = (Spinner) findViewById(R.id.CSVSelector);
        //初期グラフの表示
        if(!cman.getCSVListLocal().isEmpty()){
            DefaultCategoryDataset dataset = createDataset(cman.getCSVListLocal().get(0));
            AFreeChart chart = createChart(dataset);
            spcv.setChart(chart);
        }else{

        }
        setCsvSelector(csvSelector);

        //ドロップダウンリストをいじった時の処理
        csvSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                String item = (String) spinner.getSelectedItem();
                item = item.substring(0, 4) + "_" + item.substring(5, 7) + ".csv";
                AFreeChart chart = createChart(createDataset(item));
                spcv.setChart(chart);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
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
    private void setSpinner(Spinner spinner, String[] arr){
        for(int i = 0; i < arr.length; i++){
            arr[i] = arr[i].substring(0,4)+"年"+arr[i].substring(5,7)+"月";
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, arr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * グラフを作成する
     * @param dataset データセット
     * @return
     */
    private AFreeChart createChart(CategoryDataset dataset) {
        AFreeChart chart = ChartFactory.createBarChart(
                "試食品の評価",       // chart title
                "評価",               // domain axis label
                "評価値",                  // range axis label
                dataset,                  // data
                PlotOrientation.VERTICAL, // orientation
                false,                     // include legend
                false,                     // tooltips?
                false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaintType(new SolidColor(Color.WHITE));

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientColor gp0 = new GradientColor(Color.BLUE, Color.rgb(0, 0, 64));
        GradientColor gp1 = new GradientColor(Color.GREEN, Color.rgb(0, 64, 0));
        GradientColor gp2 = new GradientColor(Color.RED, Color.rgb(64, 0, 0));
        renderer.setSeriesPaintType(0, gp0);
        renderer.setSeriesPaintType(1, gp1);
        renderer.setSeriesPaintType(2, gp2);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;
    }

    private DefaultCategoryDataset createDataset(String filename) {
        return cman.makeDataset(filename);
    }

}
