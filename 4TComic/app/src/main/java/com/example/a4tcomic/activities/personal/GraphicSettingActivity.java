package com.example.a4tcomic.activities.personal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;
import com.example.a4tcomic.activities.PersonalActivity;

import java.util.Locale;

public class GraphicSettingActivity extends AppCompatActivity {
    Switch switchDarkMode;
    Spinner splanguageChange;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageButton btn_back;
    private boolean isSpinnerInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_graphic_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(this, PersonalActivity.class);
            startActivity(intent);
        });

        switchDarkMode = findViewById(R.id.switchDarkMode);
        splanguageChange = findViewById(R.id.splanguageChange);

        sharedPreferences = getSharedPreferences("SettingsPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        switchDarkMode.setChecked(sharedPreferences.getBoolean("DarkMode", false));
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("DarkMode", isChecked);
            editor.apply();
            setDarkMode(isChecked);
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_options, R.layout.spinner_color);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        splanguageChange.setAdapter(adapter);

        String lang = sharedPreferences.getString("Language", "vi");
        if (lang != null) {
            int spinnerPosition = adapter.getPosition(lang);
            splanguageChange.setSelection(spinnerPosition);
        }

        splanguageChange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSpinnerInitialized) {
                    isSpinnerInitialized = true;
                } else {
                    String selectedLang = parent.getItemAtPosition(position).toString();
                    editor.putString("Language", selectedLang);
                    editor.apply();
                    setLocale(selectedLang);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });



    }

    private void setDarkMode(boolean isEnabled) {
        int nightMode = isEnabled ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        Intent intent = new Intent(this, GraphicSettingActivity.class);
        startActivity(intent);
        finish();
    }
}
