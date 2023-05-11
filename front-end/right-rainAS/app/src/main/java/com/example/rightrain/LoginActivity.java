package com.example.rightrain;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button bot_acess = findViewById(R.id.botao_acessar);
        Button bot_signup = findViewById(R.id.bot_create);

        // Acess button should start main screen
        bot_acess.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });
        // Sign-up button should acess sign-up screen
        bot_signup.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), CadastroActivity.class);
            startActivity(intent);
        });
    }
}
