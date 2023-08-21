package com.example.rightrain;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rightrain.databinding.ActivityNotificationsBinding;

import java.util.ArrayList;

public class NotificationClass extends DrawerBaseActivity {
    // Strings do banco de dados das notificações
    private static final String BANCO_NOME = "bd_aviso";
    private static final String NOME_TABELA = "tb_aviso";
    private static final String COLUNA_CODIGO = "id";
    private static final String COLUNA_AVISO = "AvisoLogin";
    ListView listViewWarnings;
    ArrayList<String> linhas;
    ArrayList<Integer> arrayIds;
    SQLiteDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Configuração da atividade base
        ActivityNotificationsBinding activityNotificationsBinding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(activityNotificationsBinding.getRoot());
        // Listando avisos
        listViewWarnings = findViewById(R.id.avisos_list);
        ListarAvisos();
        if(linhas.isEmpty()){
            Toast.makeText(this, "Nenhum aviso registrado!", Toast.LENGTH_LONG).show();
        }
        // Configuração do botao
        Button clean_btn = findViewById(R.id.botao_limpar);
        clean_btn.setOnClickListener(v->{
            LimparAvisos();
        });
    }
    public void ListarAvisos(){
        try {
            arrayIds = new ArrayList<>();
            db = openOrCreateDatabase(BANCO_NOME, MODE_PRIVATE, null);
            String query = "SELECT " + COLUNA_CODIGO + ", " + COLUNA_AVISO + " FROM " + NOME_TABELA;
            Cursor meuCursor = db.rawQuery(query, null);
            linhas = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, linhas
            );
            listViewWarnings.setAdapter(adapter);
            meuCursor.moveToFirst();
            while(meuCursor!=null){
                linhas.add(meuCursor.getString(1));
                arrayIds.add(meuCursor.getInt(0));
                meuCursor.moveToNext();
            }
            meuCursor.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void LimparAvisos(){
        try{
            db = openOrCreateDatabase(BANCO_NOME, MODE_PRIVATE, null);
            String sql = "DELETE FROM " + NOME_TABELA + " WHERE " + COLUNA_CODIGO +" =?";
            SQLiteStatement stmt = db.compileStatement(sql);
            for (int i = 0; i < linhas.size(); i++) {
                stmt.bindLong(1, arrayIds.get(i));
                stmt.executeUpdateDelete();
            }
            ListarAvisos();
            db.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}