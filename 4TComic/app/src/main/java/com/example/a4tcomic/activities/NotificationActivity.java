package com.example.a4tcomic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;

public class NotificationActivity extends AppCompatActivity {

    private ImageButton btnHomePage, btnArchive, btnNotification, btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnHomePage = findViewById(R.id.btnHomePage);
        btnArchive = findViewById(R.id.btnArchive);
        btnNotification = findViewById(R.id.btnNotification);
        btnSetting = findViewById(R.id.btnSetting);

        btnHomePage.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, HomePageActivity.class);
            startActivity(settingIntent);
        });

        btnNotification.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, NotificationActivity.class);
            startActivity(settingIntent);
        });

        btnSetting.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, PersonalActivity.class);
            startActivity(settingIntent);
        });

        btnArchive.setOnClickListener(v -> {
            Intent archiveIntent = new Intent(this, BookcaseActivity.class);
            startActivity(archiveIntent);
        });
    }
}