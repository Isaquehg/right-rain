package com.example.rightrain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DevicesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        // Buttons
        Button title = findViewById(R.id.name_loc_btn);
        Button rainfall_btn = findViewById(R.id.rainfall);
        Button temperatue_btn = findViewById(R.id.temperature);
        Button humidity_btn = findViewById(R.id.humidity);

        title.setText(getIntent().getStringExtra("name_loc"));

        rainfall_btn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), LocDataActivity.class);
            intent.putExtra("type", "Pluviosidade");
            startActivity(intent);
        });
        temperatue_btn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), LocDataActivity.class);
            intent.putExtra("type", "Temperatura");
            startActivity(intent);
        });
        humidity_btn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), LocDataActivity.class);
            intent.putExtra("type", "Umidade");
            startActivity(intent);
        });
    }
}
