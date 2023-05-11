package com.example.rightrain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class fetchData extends Thread {
    @Override
    public void run() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/Giv314/json_teste/main/index.json");
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            InputStream is = hc.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String line;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
