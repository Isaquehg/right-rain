package com.example.rightrain;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.rightrain.databinding.ActivityDevicesBinding;

public class DevicesActivity extends DrawerBaseActivity {
    private String user_key;
    private String u_id;
    private String d_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Activity base setup
        ActivityDevicesBinding activityDevicesBinding = ActivityDevicesBinding.inflate(getLayoutInflater());
        setContentView(activityDevicesBinding.getRoot());
        // Buttons
        Button title = findViewById(R.id.name_loc_btn);
        Button soil_btn = findViewById(R.id.soil_button);
        Button temperature_btn = findViewById(R.id.temperature);
        Button humidity_btn = findViewById(R.id.humidity);
        // Strings - MainActivity
        user_key = getIntent().getStringExtra("user_key");
        u_id = getIntent().getStringExtra("u_id");
        d_id = getIntent().getStringExtra("d_id");
        String d_name = getIntent().getStringExtra("d_name");
        // BaseActivity allocation
        title.setText(d_name);

        soil_btn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), LocDataActivity.class);
            intent.putExtra("type", "soil_humidity");
            intent.putExtra("type_pt", "Umidade do solo");
            postUserParms(intent);
        });
        temperature_btn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), LocDataActivity.class);
            intent.putExtra("type", "temperature");
            intent.putExtra("type_pt", "Temperatura");
            postUserParms(intent);
        });
        humidity_btn.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), LocDataActivity.class);
            intent.putExtra("type", "air_humidity");
            intent.putExtra("type_pt", "Umidade do ar");
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
