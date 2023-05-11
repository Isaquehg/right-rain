package com.example.rightrain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonElement;
import com.mapbox.maps.MapView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapView = findViewById(R.id.mapView);
        setContentView(R.layout.activity_main);

        Button b1 = findViewById(R.id.bot_loc1);
        b1.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), DadosLocActivity.class);
            startActivity(intent);
        });
    }
}