package com.example.rightrain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.rightrain.databinding.ActivityDevicesBinding;
import com.example.rightrain.databinding.ActivityMainBinding;

public class DevicesActivity extends DrawerBaseActivity {
    private String user_key;
    private String u_id;
    private String d_id;
    private String ip;
    private String name;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configuração da atividade base
        ActivityDevicesBinding activityDevicesBinding = ActivityDevicesBinding.inflate(getLayoutInflater());
        setContentView(activityDevicesBinding.getRoot());
        // Buttons
        Button title = findViewById(R.id.name_loc_btn);
        Button rainfall_btn = findViewById(R.id.rainfall);
        Button temperatue_btn = findViewById(R.id.temperature);
        Button humidity_btn = findViewById(R.id.humidity);
        // Strings da MainActivity
        user_key = getIntent().getStringExtra("user_key");
        u_id = getIntent().getStringExtra("u_id");
        d_id = getIntent().getStringExtra("d_id");
        ip = getIntent().getStringExtra("ip");
        String d_name = getIntent().getStringExtra("d_name");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        // Alocando na BaseActivity
        allocateDrawerParms(name, email);
        title.setText(d_name);

        rainfall_btn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), LocDataActivity.class);
            intent.putExtra("type", "rainfall");
            intent.putExtra("type_pt", "Pluviosidade");
            postUserParms(intent);
        });
        temperatue_btn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), LocDataActivity.class);
            intent.putExtra("type", "temperature");
            intent.putExtra("type_pt", "Temperatura");
            postUserParms(intent);
        });
        humidity_btn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), LocDataActivity.class);
            intent.putExtra("type", "humidity");
            intent.putExtra("type_pt", "Umidade");
            postUserParms(intent);
        });
    }
    public void postUserParms(Intent intent){
        intent.putExtra("user_key", user_key);
        intent.putExtra("u_id", u_id);
        intent.putExtra("d_id", d_id);
        intent.putExtra("ip", ip);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}
