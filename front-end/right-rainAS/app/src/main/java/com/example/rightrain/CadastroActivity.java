package com.example.rightrain;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CadastroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        ImageView arrow = findViewById(R.id.imageView2);
        EditText phoneField = findViewById(R.id.edit_phone);
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        arrow.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }
}
