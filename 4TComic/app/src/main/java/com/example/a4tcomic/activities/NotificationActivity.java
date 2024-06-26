package com.example.a4tcomic.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a4tcomic.R;
import com.example.a4tcomic.adapters.NotificationAdapter;
import com.example.a4tcomic.db.ChaptersDB;

public class NotificationActivity extends AppCompatActivity {

    private ImageButton btnHomePage, btnArchive, btnNotification, btnSetting;
    private RecyclerView recyclerNotification;
    private NotificationAdapter notificationAdapter;
    private ChaptersDB chaptersDB;
    private String userId; // Thêm userId của người dùng hiện tại

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
        recyclerNotification = findViewById(R.id.recyclerNotification);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("id", "");

        notificationAdapter = new NotificationAdapter(this, userId);
        recyclerNotification.setLayoutManager(new LinearLayoutManager(this));
        recyclerNotification.setAdapter(notificationAdapter);

        btnHomePage.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, HomePageActivity.class);
            startActivity(settingIntent);
            finish();
        });

        btnSetting.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, PersonalActivity.class);
            startActivity(settingIntent);
            finish();
        });

        btnArchive.setOnClickListener(v -> {
            Intent archiveIntent = new Intent(this, BookcaseActivity.class);
            startActivity(archiveIntent);
            finish();
        });
    }
}
