package com.example.rightrain;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class DrawerBaseActivity extends AppCompatActivity {
    private NavigationView navigationView;
    @Override
    public void setContentView(View view) {
        DrawerLayout drawerLayout;
        // Configuração das views
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = drawerLayout.findViewById(R.id.activityContainer);
        frameLayout.addView(view);
        super.setContentView(drawerLayout);
        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar1);
        // Configuração do drawer
        navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.logout) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                if(id == R.id.notifications){
                    Intent intent = new Intent(getApplicationContext(), NotificationClass.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        // Configuração do botão - notificações
        ImageView notButton = drawerLayout.findViewById(R.id.notification_btn);
        notButton.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), NotificationClass.class);
            startActivity(intent);
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        }

    public void allocateDrawerParms(String name, String email){
        View header = navigationView.getHeaderView(0);
        TextView nameTextView;
        TextView emailTextView;
        nameTextView = navigationView.getHeaderView(0).findViewById(R.id.name);
        emailTextView = header.findViewById(R.id.email);
        nameTextView.setText(name);
        emailTextView.setText(email);
    }
}
