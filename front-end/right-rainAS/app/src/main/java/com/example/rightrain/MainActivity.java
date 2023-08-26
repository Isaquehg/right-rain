package com.example.rightrain;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rightrain.databinding.ActivityMainBinding;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.Pair;

public class MainActivity extends DrawerBaseActivity {
    private MapView mapView;
    private ArrayList<String> locList;
    private ListView de_list1;
    private ArrayAdapter<String> listAdapter;
    private RequestQueue mQueue;
    private Double latitude_aux;
    private Double longitude_aux;
    private List<Pair<Double, Double>> coordinates;
    private ArrayList<String> d_id;

    // Strings do banco de dados das notificações
    private static final String NAME_DB = "bd_notf";
    private static final String NAME_TABLE = "tb_notf";
    private static final String COLUMN_CODE = "id";
    private static final String NOTF_COLUMN = "Notf";
    private static final String DATE_COLUMN = "Date";
    private static final String HOUR_COLUMN = "Hour";
    private static final String USER_COLUMN = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Base activity setting
        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        allocateDrawerParms(name, email);

        // User data settings
        de_list1 = findViewById(R.id.de_list);
        mapView = findViewById(R.id.mapView);
        mQueue = Volley.newRequestQueue(this);
        // Getting data from devices
        getData();
        // We are going to set rounded corners for de_list1
        de_list1.setBackgroundResource(R.drawable.custom_listview);
        de_list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DevicesActivity.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("d_id", d_id.get(position));
                intent.putExtra("d_name", locList.get(position));
                intent.putExtra("user_key", user_key);
                intent.putExtra("ip", ip);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        // Notifications
        boolean primeiro_login = getIntent().getBooleanExtra("primeiro_login", false);
        SharedPreferences sharedPr = PreferenceManager.getDefaultSharedPreferences(this);
        boolean is_enabled = sharedPr.getBoolean("Login", false);
        if (primeiro_login && is_enabled) {
            WorkRequest uploadWorkRequest =
                    new OneTimeWorkRequest.Builder(NotificationWorker.class)
                            .build();
            WorkManager
                    .getInstance(this)
                    .enqueue(uploadWorkRequest);
            createDatabase();
            createNotOnDatabase("Novo Login!");
        }
    }
    private void getData(){
        String url = ip + "/home/" + u_id;
        d_id = new ArrayList<>();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            locList = new ArrayList<>();
                            coordinates = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                d_id.add(jsonObject.getString("d_id"));
                                locList.add(jsonObject.getString("d_name"));
                                String latitude = jsonObject.getString("latitude");
                                String longitude = jsonObject.getString("longitude");
                                latitude_aux = Double.parseDouble(latitude);
                                longitude_aux = Double.parseDouble(longitude);
                                coordinates.add(new Pair<>(latitude_aux, longitude_aux));
                                setLocations(coordinates);
                            }
                            listAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.custom_adapter, R.id.textview, locList);
                            de_list1.setAdapter(listAdapter);
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
    public void setLocations(List<Pair<Double, Double>> coordinates){
        Map map = new Map();
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                map.addAnnotationToMap(MainActivity.this, mapView, coordinates);
            }
        });
    }
        public void createDatabase(){
            SQLiteDatabase db;
            try {
                db = openOrCreateDatabase(NAME_DB, MODE_PRIVATE, null);
                String QUERY_COLUNA = "CREATE TABLE IF NOT EXISTS " + NAME_TABLE + " ("
                        + COLUMN_CODE + " INTEGER PRIMARY KEY," + NOTF_COLUMN + " TEXT, " + DATE_COLUMN + " TEXT, " + HOUR_COLUMN +
                        " TEXT, " + USER_COLUMN + " TEXT)";
                db.execSQL(QUERY_COLUNA);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void createNotOnDatabase(String aviso){
        SQLiteDatabase db;
        try{
            db = openOrCreateDatabase(NAME_DB, MODE_PRIVATE, null);
            String sql = "INSERT INTO " + NAME_TABLE + " (" + NOTF_COLUMN + "," + DATE_COLUMN + "," + HOUR_COLUMN + "," +
                    USER_COLUMN + ") VALUES (?, ?, ?, ?)";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1,aviso);
            stmt.bindString(2, String.valueOf(LocalDate.now()));
            stmt.bindString(3, String.valueOf(LocalTime.now()));
            stmt.bindString(4, name);
            stmt.executeInsert();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
