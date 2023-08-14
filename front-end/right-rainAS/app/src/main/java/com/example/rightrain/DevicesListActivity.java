package com.example.rightrain;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.widget.SearchView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rightrain.databinding.ActivityDevicesBinding;
import com.example.rightrain.databinding.ActivityDevicesListBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DevicesListActivity extends DrawerBaseActivity{
    private ArrayList<String> locList;
    private ListView de_list1;
    private ArrayAdapter<String> listAdapter;
    private RequestQueue mQueue;
    private ArrayList<String> d_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configuração da atividade base
        ActivityDevicesListBinding activityDevicesListBinding = ActivityDevicesListBinding.inflate(getLayoutInflater());
        setContentView(activityDevicesListBinding.getRoot());
        allocateDrawerParms(name, email);
        // Configs da activity
        de_list1 = findViewById(R.id.de_list);
        mQueue = Volley.newRequestQueue(this);
        getData();
        de_list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DevicesActivity.class);
                intent.putExtra("u_id", u_id);
                intent.putExtra("d_id", d_id.get(position));
                intent.putExtra("d_name", locList.get(position));
                intent.putExtra("user_key", user_key);
                intent.putExtra("ip", ip);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        // Configurações da barra de busca
        SearchView searchView = findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void getData(){
        String url = ip + "/home/" + u_id;
        d_id = new ArrayList<>();
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    locList = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        d_id.add(jsonObject.getString("d_id"));
                        locList.add(jsonObject.getString("d_name"));
                    }
                    listAdapter = new ArrayAdapter<String>(DevicesListActivity.this, android.R.layout.simple_list_item_1, locList);
                    de_list1.setAdapter(listAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                java.util.Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + user_key);
                return headers;
            }
        };
        mQueue.add(request);
    }
}


