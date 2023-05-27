package com.example.rightrain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class fetchData extends Thread {
    String data = "";
    ArrayList<String> userList;
    @Override
    public void run() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/Giv314/json_teste/main/index.json");
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            InputStream is = hc.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String line;

            while((line = bf.readLine()) != null){
                data = data + line;
            }
            if(!data.isEmpty()){
                JSONObject jsonObject = new JSONObject(data);
                JSONArray users = jsonObject.getJSONArray("user");
                userList.clear();
                for (int i = 0; i < users.length(); i++) {
                    org.json.JSONObject names = users.getJSONObject(i);
                    String name = names.getString("name");
                    userList.add(name);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
