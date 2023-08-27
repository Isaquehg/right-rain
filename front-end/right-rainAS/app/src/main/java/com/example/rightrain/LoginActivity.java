package com.example.rightrain;

import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    private ProgressBar loadingPB;
    public String ip = "http://3.21.168.204:8000";
    private String name = "null";
    private String user_key = "null";
    private String u_id = "null";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadingPB = findViewById(R.id.idLoadingPB);
        Button bot_access = findViewById(R.id.botao_acessar);
        Button bot_signup = findViewById(R.id.bot_create);
        EditText userEdit = findViewById(R.id.user_edit);
        EditText passEdit = findViewById(R.id.pass_edit);

        // Access button should start main screen
        bot_access.setOnClickListener(v -> {
            postDataUsingVolley(userEdit.getText().toString(), passEdit.getText().toString());
        });
        // Sign-up button should access sign-up screen
        bot_signup.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            intent.putExtra("ip", ip);
            startActivity(intent);
        });
    }

    private void postDataUsingVolley(String email, String password) {
        // url to post our data
        String url = ip + "/token";
        loadingPB.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", email);
            jsonObject.put("password", password);
        }catch (Exception e){
            e.printStackTrace();
        }

        // Requests
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                        try {
                            user_key = response.getString("access_token");
                            u_id = response.getString("u_id");
                            name = response.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("ip", ip);
                        intent.putExtra("user_key", user_key);
                        intent.putExtra("u_id", u_id);
                        intent.putExtra("name", name);
                        intent.putExtra("email", email);
                        intent.putExtra("first_login", true);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjReq);
    }
}
