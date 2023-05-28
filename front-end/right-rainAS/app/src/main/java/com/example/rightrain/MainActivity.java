package com.example.rightrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.JsonElement;
import com.mapbox.maps.MapView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MapView mapView;
    ArrayList<String> userList;
    ListView userList1;
    ArrayAdapter<String> listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = findViewById(R.id.mapView);
        setContentView(R.layout.activity_main);
        getData();
        new fetchData().start();
    }
    private void getData(){
        userList1 = (ListView) findViewById(R.id.userList);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userList);
        userList1.setAdapter(listAdapter);
    }
    class fetchData extends Thread {
        String data = "";
        @Override
        public void run() {
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
        }
    }
}
