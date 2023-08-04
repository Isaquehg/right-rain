package com.example.rightrain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;


import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.Pair;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    public Toolbar toolbar;
    private MapView mapView;
    private ArrayList<String> locList;
    private ListView userList1;
    private ArrayAdapter<String> listAdapter;
    private RequestQueue mQueue;
    private Double latitude_aux;
    private Double longitude_aux;
    private List<Pair<Double, Double>> coordinates;
    private String u_id;
    private String ip;
    private String user_key;
    private ArrayList<String> d_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Strings do LoginActivity
        u_id = getIntent().getStringExtra("u_id");
        user_key = getIntent().getStringExtra("user_key");
        ip = getIntent().getStringExtra("ip");
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");

        // Configuração do drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView nameText = header.findViewById(R.id.name);
        TextView emailText = header.findViewById(R.id.email);
        nameText.setText(name);
        emailText.setText(email);
        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);

        // Configuração dos botões
        ImageView bt_menu = findViewById(R.id.bot_menu);
        bt_menu.setOnClickListener(v->{
            drawerLayout.openDrawer(GravityCompat.START);
        });

        ImageView notButton = findViewById(R.id.notification_btn);
        notButton.setOnClickListener(v->{
           Intent intent = new Intent(getApplicationContext(), NotificationClass.class);
           startActivity(intent);
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);
                if(id == R.id.logout) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                return true;
                }
        });

        // Configurações dos dados do usuário
        userList1 = findViewById(R.id.userList);
        mapView = findViewById(R.id.mapView);
        mQueue = Volley.newRequestQueue(this);
        getData();
        userList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DevicesActivity.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("d_id", d_id.get(position));
                intent.putExtra("d_name", locList.get(position));
                intent.putExtra("user_key", user_key);
                intent.putExtra("ip", ip);
                startActivity(intent);
            }
        });

        // Configurações da barra de busca
        SearchView searchView = findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return false;
            }
            });

        // Notificações
        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(NotificationWorker.class)
                        .build();
        WorkManager
                .getInstance(this)
                .enqueue(uploadWorkRequest);
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
                            listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, locList);
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
}
