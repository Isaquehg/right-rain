package com.example.rightrain;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class RegisterActivity extends AppCompatActivity {
    String ip;
    ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ip = getIntent().getStringExtra("ip");

        ImageView arrow = findViewById(R.id.imageView2);
        loadingPB = findViewById(R.id.idLoadingPB);
        // Campos
        EditText nameField = findViewById(R.id.edit_nome);
        EditText emailField = findViewById(R.id.edit_email);
        EditText phoneField = findViewById(R.id.edit_phone);
        EditText passField = findViewById(R.id.edit_pass);
        EditText repeatPass = findViewById(R.id.edit_repeat_pass);
        // Register button
        Button register_btn = findViewById(R.id.bot_create);

        // Change phone field format
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        arrow.setOnClickListener(v -> {
            finish();
        });
        register_btn.setOnClickListener(v->{
            if(repeatPass.getText().toString().equals(passField.getText().toString())) {
                String name = nameField.getText().toString();
                String email = emailField.getText().toString();
                String pass = passField.getText().toString();
                String phone = phoneField.getText().toString();
                postDataUsingVolley(name, email, pass, phone);
            }else{
                Toast.makeText(this, getString(R.string.password_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postDataUsingVolley(String username, String email, String password, String number) {
        String url = ip + "/register";
        loadingPB.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", username);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("number", number);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingPB.setVisibility(View.GONE);
                        try {
                            Toast.makeText(RegisterActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this, getString(R.string.create_user_error), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjReq);
    }
}
