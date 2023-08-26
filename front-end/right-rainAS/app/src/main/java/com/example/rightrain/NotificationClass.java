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
    // Strings do banco de dados das notificações
    private static final String NAME_BD = "bd_notf";
    private static final String TABLE_NAME = "tb_notf";
    private static final String CODE_COLUMN = "id";
    private static final String NOTF_COLUMN = "Notf";
    ListView listViewNotf;
    ArrayList<String> linhas;
    ArrayList<Integer> arrayIds;
    SQLiteDatabase db;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // BaseActivity Configuration
        ActivityNotificationsBinding activityNotificationsBinding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(activityNotificationsBinding.getRoot());

        // Listing notifications
        listViewNotf = findViewById(R.id.avisos_list);
        linhas = new ArrayList<>();
        ListNotf();
        if(linhas.isEmpty()){
            Toast.makeText(this, "Nenhum aviso registrado!", Toast.LENGTH_LONG).show();
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

        // Configuring Button
        Button clean_btn = findViewById(R.id.botao_limpar);
        clean_btn.setOnClickListener(v->{
            DeleteNotf();
        });
    }
    public void ListNotf(){
        try {
            arrayIds = new ArrayList<>();
            db = openOrCreateDatabase(NAME_BD, MODE_PRIVATE, null);
            String query = "SELECT " + CODE_COLUMN + ", " + NOTF_COLUMN + " FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, linhas
            );
            listViewNotf.setAdapter(adapter);
            cursor.moveToFirst();
            while(cursor!=null){
                linhas.add(cursor.getString(1));
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
            db = openOrCreateDatabase(NAME_BD, MODE_PRIVATE, null);
            String sql = "DELETE FROM " + TABLE_NAME + " WHERE " + CODE_COLUMN +" =?";
            SQLiteStatement stmt = db.compileStatement(sql);
            for (int i = 0; i < linhas.size(); i++) {
                stmt.bindLong(1, arrayIds.get(i));
                stmt.executeUpdateDelete();
            }
            ListNotf();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}