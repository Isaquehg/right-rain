package com.example.rightrain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    String user_key = "null";
    String u_id = "null";
    ProgressBar loadingPB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadingPB = findViewById(R.id.idLoadingPB);
        Button bot_acess = findViewById(R.id.botao_acessar);
        Button bot_signup = findViewById(R.id.bot_create);
        EditText userEdit = findViewById(R.id.user_edit);
        EditText passEdit = findViewById(R.id.pass_edit);

        // Acess button should start main screen
        bot_acess.setOnClickListener(v -> {
            postDataUsingVolley(userEdit.getText().toString(), passEdit.getText().toString());
        });
        // Sign-up button should acess sign-up screen
        bot_signup.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void postDataUsingVolley(String username, String password) {
        // url to post our data
        String url = "http://18.191.252.222:8000/token";
        loadingPB.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        }catch (Exception e){
            e.printStackTrace();
        }

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Login sucedido!", Toast.LENGTH_SHORT).show();
                        try {
                            user_key = response.getString("access_token");
                            u_id = response.getString("u_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("user_key", user_key);
                        intent.putExtra("u_id", u_id);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Usuário ou senha inválidos!: ", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjReq);
    }
}
