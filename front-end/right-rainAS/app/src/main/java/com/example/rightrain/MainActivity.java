package com.example.rightrain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.widget.Toolbar;

import com.mapbox.maps.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    MapView mapView;
    ArrayList<String> userList;
    ListView userList1;
    ArrayAdapter<String> listAdapter;
    Handler mainHandler = new Handler();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView bt_menu = findViewById(R.id.bot_menu);

        setSupportActionBar(toolbar);

        bt_menu.setOnClickListener(v->{
            drawerLayout.openDrawer(GravityCompat.START);
        });

        getData();
        new fetchData().start();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getData(){
        userList = new ArrayList<>();
        userList1 = findViewById(R.id.userList);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userList);
        userList1.setAdapter(listAdapter);
    }
    class fetchData extends Thread {
        String data = "";
        @Override
        public void run() {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar = findViewById(R.id.progress_bar);
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
            try {
                URL url = new URL("https://raw.githubusercontent.com/Giv314/json_teste/main/index.json");
                HttpURLConnection hc = (HttpURLConnection) url.openConnection();
                InputStream is = hc.getInputStream();
                BufferedReader bf = new BufferedReader(new InputStreamReader(is));
                String line;

                while((line = bf.readLine()) != null){
                    data = data + line;
                }
                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray users = jsonObject.getJSONArray("Users");
                    userList.clear();
                    for (int i = 0; i < users.length(); i++) {
                        org.json.JSONObject names = users.getJSONObject(i);
                        String name = names.getString("name");
                        userList.add(name);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
