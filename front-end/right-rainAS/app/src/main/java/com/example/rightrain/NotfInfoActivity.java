package com.example.rightrain;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NotfInfoActivity extends AppCompatActivity {
    private static final String NAME_DB = "bd_notf";
    private static final String DATE_COLUMN = "Date";
    private static final String HOUR_COLUMN = "Hour";
    private static final String USER_COLUMN = "User";
    private static final String TABLE_NAME = "tb_notf";
    String name;
    String date;
    String hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notf_info);
        // Getting the position user selected
        int position = getIntent().getIntExtra("position", 0);
        TextView content = findViewById(R.id.info_tView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Novo Login!");
        getNotf(position);
        content.setText("O usuário " + name + " fez login na data " + date + " às " + hour);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    public void getNotf(int position){
        SQLiteDatabase db = openOrCreateDatabase(NAME_DB, MODE_PRIVATE, null);
        String query = "SELECT " + DATE_COLUMN + ", " + HOUR_COLUMN + ", " + USER_COLUMN + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToPosition(position);
        name = cursor.getString(cursor.getColumnIndex(USER_COLUMN));
        date = cursor.getString(cursor.getColumnIndex(DATE_COLUMN));
        hour = cursor.getString(cursor.getColumnIndex(HOUR_COLUMN));
        cursor.close();
        db.close();
    }
}
