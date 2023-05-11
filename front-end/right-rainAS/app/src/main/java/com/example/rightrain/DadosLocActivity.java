package com.example.rightrain;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DadosLocActivity extends AppCompatActivity {
    LineChart lineChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_loc);

        lineChart = findViewById(R.id.graph1);
        LineDataSet lineDataSet = new LineDataSet(dataValues(), "Data Set");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    private List<Entry> dataValues(){
        ArrayList<Entry> dataValue = new ArrayList<>();
        dataValue.add(new Entry(0 ,10));
        dataValue.add(new Entry(1 ,20));
        dataValue.add(new Entry(2 ,30));
        dataValue.add(new Entry(3 ,40));
        dataValue.add(new Entry(4 ,50));
        return dataValue;
    }
}
