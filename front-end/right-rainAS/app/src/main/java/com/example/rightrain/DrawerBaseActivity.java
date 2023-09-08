package com.example.rightrain;

import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class DrawerBaseActivity extends AppCompatActivity {
    private NavigationView navigationView;
    public String ip;
    public String u_id;
    public String name;
    public String email;
    public String user_key;
    boolean closeDrawer = false;
    @Override
    public void setContentView(View view) {
        DrawerLayout drawerLayout;
        // Views configurations
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = drawerLayout.findViewById(R.id.activityContainer);
        frameLayout.addView(view);
        super.setContentView(drawerLayout);
        Toolbar toolbar = drawerLayout.findViewById(R.id.toolbar1);
        // Drawer configuration
        navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.bringToFront();
        // LoginActivity Strings shared by activities that extends this one.
        if(ip == null && u_id == null && name == null && email == null && user_key == null){
            user_key = getIntent().getStringExtra("user_key");
            ip = getIntent().getStringExtra("ip");
            u_id = getIntent().getStringExtra("u_id");
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
        }
        allocateDrawerParms(name, email);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.logout) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    finish();
                    startActivity(intent);
                }
                if(id == R.id.notifications){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(getApplicationContext(), NotificationClass.class);
                    putExtrasIntent(intent);
                }
                if(id == R.id.mainscreen){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
                if(id == R.id.senslist){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(getApplicationContext(), DevicesListActivity.class);
                    putExtrasIntent(intent);
                }
                if(id == R.id.settings){
                    drawerLayout.closeDrawer(GravityCompat.START);
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        // Notification button configuration
        ImageView notButton = drawerLayout.findViewById(R.id.notification_btn);
        notButton.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), NotificationClass.class);
            putExtrasIntent(intent);
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        }

        // Allocate strings to the header
    public void allocateDrawerParms(String name, String email){
        View header = navigationView.getHeaderView(0);
        TextView nameTextView;
        TextView emailTextView;
        nameTextView = navigationView.getHeaderView(0).findViewById(R.id.name);
        emailTextView = header.findViewById(R.id.email);
        nameTextView.setText(name);
        emailTextView.setText(email);
    }

    // Necessary strings to the header
    public void putExtrasIntent(Intent intent){
        intent.putExtra("u_id", u_id);
        intent.putExtra("user_key", user_key);
        intent.putExtra("ip", ip);
        intent.putExtra("name", name);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}
