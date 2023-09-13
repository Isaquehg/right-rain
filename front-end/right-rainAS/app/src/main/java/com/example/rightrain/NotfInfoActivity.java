package com.example.rightrain;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.apache.commons.math3.stat.regression.SimpleRegression;

public class NotfInfoActivity extends AppCompatActivity {
    private static final String NAME_DB = "bd_notf";
    private static final String DATE_COLUMN = "Date";
    private static final String HOUR_COLUMN = "Hour";
    private static final String USER_COLUMN = "User";
    private static final String TABLE_NAME = "tb_notf";
    private String name;
    private String date;
    private String hour;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notf_info);
        // Getting the position user selected
        int position = getIntent().getIntExtra("position", 0);
        content = findViewById(R.id.info_tView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.new_login));
        getNotf(position);
        if(name != null) {
            content.setText(getString(R.string.content_info, name, date, hour));
        }else{
            content.setText(getString(R.string.unknown_login));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    public void getNotf(int position){
        try {
            SQLiteDatabase db = openOrCreateDatabase(NAME_DB, MODE_PRIVATE, null);
            String query = "SELECT " + DATE_COLUMN + ", " + HOUR_COLUMN + ", " + USER_COLUMN + " FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToPosition(position);
            name = cursor.getString(cursor.getColumnIndex(USER_COLUMN));
            date = cursor.getString(cursor.getColumnIndex(DATE_COLUMN));
            hour = cursor.getString(cursor.getColumnIndex(HOUR_COLUMN));
            cursor.close();
            db.close();
        }catch (Exception e){
            content.setText(getString(R.string.unknown_login));
            e.printStackTrace();
        }
    }
}
