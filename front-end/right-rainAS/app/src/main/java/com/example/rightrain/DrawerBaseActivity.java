package com.example.rightrain;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class DrawerBaseActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        LinearLayout linearLayout = drawerLayout.findViewById(R.id.linearLayout);
        linearLayout.addView(view);
        super.setContentView(drawerLayout);
        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        // Dados
        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        TextView nameText = drawerLayout.findViewById(R.id.name);
        TextView emailText = drawerLayout.findViewById(R.id.email);
        nameText.setText(name);
        emailText.setText(email);
        // Configuração do drawer
        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        // Configuração dos botões
        ImageView bt_menu = drawerLayout.findViewById(R.id.bot_menu);
        bt_menu.setOnClickListener(v->{
            drawerLayout.openDrawer(GravityCompat.START);
        });
        ImageView notButton = drawerLayout.findViewById(R.id.notification_btn);
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
    }
}
