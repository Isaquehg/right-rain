package com.example.rightrain;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.github.mikephil.charting.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Preference preference = (Preference) this.findPreference("about");
        assert preference != null;
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                File file = new File("android.resource://com.example.rightrain/assets/Right-Rain.pdf");
                if(file.exists()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = FileProvider.getUriForFile(getPreferenceManager().getContext(), "com.example.rightrain" + ".provider", file);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.setAction(Intent.ACTION_SEND);
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(), "Arquivo Não encontrado!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }
}
