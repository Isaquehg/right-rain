package com.example.rightrain;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.widget.Toolbar;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    MapView mapView;
    ArrayList<String> userList;
    ListView userList1;
    ArrayAdapter<String> listAdapter;
    ImageView bt_menu;
    RequestQueue mQueue;
    Double latitude_aux;
    Double longitude_aux;
    List<Pair<Double, Double>> coordinates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_menu = findViewById(R.id.bot_menu);
        userList1 = findViewById(R.id.userList);
        bt_menu.setOnClickListener(v->{
            drawerLayout.openDrawer(GravityCompat.START);
        });
        mapView = findViewById(R.id.mapView);

        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);

        mQueue = Volley.newRequestQueue(this);
        getData();
        userList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DevicesActivity.class);
                intent.putExtra("name_loc", userList.get(position));
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getData(){
        String url = "https://raw.githubusercontent.com/Giv314/json_teste/main/home_uid.json";
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            userList = new ArrayList<>();
                            coordinates = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            int i = 0;
                            for (i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String name = jsonObject.getString("d_name");
                                String latitude = jsonObject.getString("latitude");
                                String longitude = jsonObject.getString("longitude");
                                Log.d("Latitude", latitude);
                                Log.d("Longitude", longitude);
                                latitude_aux = Double.parseDouble(latitude);
                                longitude_aux = Double.parseDouble(longitude);
                                coordinates.add(new Pair<>(latitude_aux, longitude_aux));
                                userList.add(name);
                                setLocations(coordinates);
                            }
                            listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, userList);
                            userList1.setAdapter(listAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
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
}
