package com.example.rightrain;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import kotlin.Pair;

public class LocDataActivity extends AppCompatActivity {
    LineChart lineChart;
    Button startDate;
    Button endDate;
    String startDate1;
    String endDate1;
    String u_id;
    String d_id;
    String user_key;
    String type;
    ArrayList<Entry> dataValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_loc);
        // Strings necessarias para url
        user_key = getIntent().getStringExtra("user_key");
        u_id = getIntent().getStringExtra("u_id");
        d_id = getIntent().getStringExtra("d_id");
        type = getIntent().getStringExtra("type");
        // O tipo está em português, e o URL em ingles.
        if(type.equals("Temperatura")){
            type = "temperature";
        }else{
            if(type.equals("Pluviosidade")){
                type = "rainfall";
            }else{
                if(type.equals("Umidade")){
                    type = "umidity";
                }
            }
        }
        dataValue = new ArrayList<>();
        ImageView home_btn = findViewById(R.id.home_btn);
        Button type_btn = findViewById(R.id.type_btn);
        // Calendar to Show current date
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        startDate = findViewById(R.id.start_date_btn);
        endDate = findViewById(R.id.end_date_btn);

        lineChart = findViewById(R.id.graph1);

        startDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, R.style.DatePickerDialogStyle, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    startDate1 = dayOfMonth + "-" + month + "-" + year;
                    startDate.setText(startDate1);
                    if(endDate1 != null){
                        getData();
                    }
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        endDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, R.style.DatePickerDialogStyle, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    String date = dayOfMonth + "-" + month + "-" + year;
                    endDate.setText(date);
                    if(startDate1 != null){
                        getData();
                    }
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        home_btn.setOnClickListener(v->{
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });

        type_btn.setText(type);
        LineDataSet lineDataSet = new LineDataSet(dataValues(), "Data Set");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();

        type_btn.setOnClickListener(v -> {
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotification")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Alerta de Temperatura!")
                    .setContentText("A temperatura está muito alta.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            managerCompat.notify(1, builder.build());
        });
    }

    private List<Entry> dataValues() {
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

    private void getData(){
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "http://18.191.252.222:8000/home/" + u_id + d_id + startDate1 + endDate1;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data1 = jsonArray.getJSONObject(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                java.util.Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + user_key);
                return headers;
            }
        };
       mQueue.add(request);
    }
}
