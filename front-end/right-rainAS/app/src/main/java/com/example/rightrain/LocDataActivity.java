package com.example.rightrain;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class LocDataActivity extends AppCompatActivity {
    LineChart lineChart;
    Button startDate;
    Button endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_loc);
        String type = getIntent().getStringExtra("type");

        Button type_btn = findViewById(R.id.type_btn);
        startDate = findViewById(R.id.start_date_btn);
        endDate = findViewById(R.id.end_date_btn);

        lineChart = findViewById(R.id.graph1);

        startDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    LocDataActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    String date = dayOfMonth + "/" + month + "/" + year;
                    startDate.setText(date);
                }
            }, 0, 0, 0);
            datePickerDialog.show();
        });

        endDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    LocDataActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    String date = dayOfMonth + "/" + month + "/" + year;
                    endDate.setText(date);
                }
            }, 0, 1, 0);
            datePickerDialog.show();
        });

        type_btn.setText(type);
        LineDataSet lineDataSet = new LineDataSet(dataValues(), "Data Set");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();

        type_btn.setOnClickListener(v->{
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotification")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Alerta de Temperatura!")
                    .setContentText("A temperatura está muito alta.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(1, builder.build());
        });
    }

    private List<Entry> dataValues() {
        ArrayList<Entry> dataValue = new ArrayList<>();
        dataValue.add(new Entry(0, 10));
        dataValue.add(new Entry(1, 20));
        dataValue.add(new Entry(2, 30));
        dataValue.add(new Entry(3, 40));
        dataValue.add(new Entry(4, 50));
        return dataValue;
    }
    public void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel("MyNotification", "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
