package com.example.rightrain;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rightrain.databinding.ActivityLocDataBinding;
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

public class LocDataActivity extends DrawerBaseActivity {
    private LineChart lineChart;
    private Button startDate;
    private Button endDate;
    private String startDate1;
    private String endDate1;
    private String d_id;
    private String type;
    private ArrayList<Entry> dataValue;
    private ArrayList<Integer> values;
    private String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configuração da atividade base
        ActivityLocDataBinding activityLocDataBinding = ActivityLocDataBinding.inflate(getLayoutInflater());
        setContentView(activityLocDataBinding.getRoot());
        // Strings necessarias para o header
       // allocateDrawerParms(name, email);
        d_id = getIntent().getStringExtra("d_id");
        type = getIntent().getStringExtra("type");
        String type_pt = getIntent().getStringExtra("type_pt");
        startDate1 = "2019-01-01";
        endDate1 = "2022-11-25";
        dataValue = new ArrayList<>();
        Button type_btn = findViewById(R.id.type_btn);
        type_btn.setText(type_pt);
        // Descrição para o gráfico

        // Calendar para mostrar o dia atual
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        startDate = findViewById(R.id.start_date_btn);
        endDate = findViewById(R.id.end_date_btn);
        lineChart = findViewById(R.id.graph1);
        lineChart.setNoDataText("Selecione a data inicial e a data final!");

        // Label do gráfico
        if(type.equals("temperature")){
            label = "ºC";
        }else if(type.equals("rainfall")){
            label = "mm";
        }else{
            label = "kg/m³";
        }

        startDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, R.style.DatePickerDialogStyle, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    if(month < 10 && dayOfMonth > 10){
                        startDate1 = year + "-0" + month + "-" + dayOfMonth;
                    }else{
                        if(dayOfMonth < 10 && month > 10){
                            startDate1 = year + "-" + month + "-0" + dayOfMonth;
                        }else{
                            if(dayOfMonth < 10 && month < 10){
                                startDate1 = year + "-0" + month + "-0" + dayOfMonth;
                            }else{
                                startDate1 = year + "-" + month + "-" + dayOfMonth;
                            }
                        }
                    }
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
                    if(month < 10 && dayOfMonth > 10){
                        endDate1 = year + "-0" + month + "-" + dayOfMonth;
                    }else{
                        if(dayOfMonth < 10 && month > 10){
                            endDate1 = year + "-" + month + "-0" + dayOfMonth;
                        }else {
                            if (dayOfMonth < 10 && month < 10) {
                                endDate1 = year + "-0" + month + "-0" + dayOfMonth;
                            } else {
                                endDate1 = year + "-" + month + "-" + dayOfMonth;
                            }
                        }
                    }
                    endDate.setText(endDate1);
                    if(startDate1 != null){
                        getData();
                    }
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    private List<Entry> dataValues() {
        for (int i = 0; i < values.size(); i++) {
            dataValue.add(new Entry(i, values.get(i)));
        }
        return dataValue;
    }

    private void getData(){
        values = new ArrayList<>();
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = ip + "/home/" + u_id + "/" + d_id + "/" + type + "?start_date=" + startDate1 + "&end_date=" + endDate1;
        Log.d("url", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data1 = jsonArray.getJSONObject(i);
                        values.add(data1.getInt("value"));
                    }
                    LineDataSet lineDataSet = new LineDataSet(dataValues(), label);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet);
                    LineData data = new LineData(dataSets);
                    lineChart.setData(data);
                    lineChart.invalidate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext() /* MyActivity */, "Selecione outros parametros!", Toast.LENGTH_SHORT).show();
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
