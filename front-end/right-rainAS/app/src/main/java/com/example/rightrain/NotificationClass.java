package com.example.rightrain;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.rightrain.databinding.ActivityNotificationsBinding;

import java.util.ArrayList;

public class NotificationClass extends DrawerBaseActivity {
    // Strings for SQL Database (Notification)
    private static final String NAME_DB = "bd_notf";
    private static final String NAME_TABLE = "tb_notf";
    private static final String COLUMN_CODE = "id";
    private static final String NOTIFICATION_COLUMN = "Notf";
    public static ArrayList<String> lines;
    private ArrayList<Integer> arrayIds;
    private SQLiteDatabase db;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // BaseActivity Configuration
        ActivityNotificationsBinding activityNotificationsBinding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(activityNotificationsBinding.getRoot());

        // Listing notifications
        ListView listViewNotf;
        listViewNotf = findViewById(R.id.avisos_list);
        lines = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, lines
        );
        listViewNotf.setAdapter(adapter);
        ListNotf();
        if(lines.isEmpty()){
            Toast.makeText(this, getString(R.string.no_notifications), Toast.LENGTH_LONG).show();
        }
        // Configuring what happens if user clicks in an element of a listView
        listViewNotf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NotfInfoActivity.class);
                intent.putExtra("position", position);
                putExtrasIntent(intent);
            }
        });

        // Configuring clean button
        Button clean_btn = findViewById(R.id.botao_limpar);
        clean_btn.setOnClickListener(v->{
            DeleteNotf();
        });
    }
    public void ListNotf(){
        try {
            arrayIds = new ArrayList<>();
            db = openOrCreateDatabase(NAME_DB, MODE_PRIVATE, null);
            String query = "SELECT " + COLUMN_CODE + ", " + NOTIFICATION_COLUMN + " FROM " + NAME_TABLE;
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while(cursor!=null){
                lines.add(cursor.getString(1));
                arrayIds.add(cursor.getInt(0));
                cursor.moveToNext();
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void DeleteNotf(){
        try{
            db = openOrCreateDatabase(NAME_DB, MODE_PRIVATE, null);
            String sql = "DELETE FROM " + NAME_TABLE + " WHERE " + COLUMN_CODE +" =?";
            SQLiteStatement stmt = db.compileStatement(sql);
            for (int i = 0; i < lines.size(); i++) {
                stmt.bindLong(1, arrayIds.get(i));
                stmt.executeUpdateDelete();
            }
            lines.clear();
            adapter.clear();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}