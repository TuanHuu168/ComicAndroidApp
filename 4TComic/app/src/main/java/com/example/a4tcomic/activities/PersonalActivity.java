package com.example.a4tcomic.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;
import com.example.a4tcomic.activities.account.LoginActivity;
import com.example.a4tcomic.activities.admin.AdminActivity;
import com.example.a4tcomic.activities.personal.AccountActivity;
import com.example.a4tcomic.activities.personal.GraphicSettingActivity;
import com.example.a4tcomic.activities.personal.ProfileActivity;
import com.example.a4tcomic.activities.personal.UploadComicActivity;

public class PersonalActivity extends AppCompatActivity {

    private TextView tv_edit_profile;
    private Button btn_account, btn_setting, btn_admin, btn_logout, btn_upload_story;
    private ImageButton btnHomePage, btnArchive, btnNotification, btnSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tham chiếu đến Views
        tv_edit_profile = findViewById(R.id.tv_edit_profile);
        btn_account = findViewById(R.id.btn_account);
        btn_setting = findViewById(R.id.btn_setting);
        btn_admin = findViewById(R.id.btn_admin);
        btn_logout = findViewById(R.id.btn_logout);
        btn_upload_story = findViewById(R.id.btn_upload_story);

        btnHomePage = findViewById(R.id.btnHomePage);
        btnArchive = findViewById(R.id.btnArchive);
        btnNotification = findViewById(R.id.btnNotification);
        btnSetting = findViewById(R.id.btnSetting);

        // Chuyển trang bottom
        btnHomePage.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, HomePageActivity.class);
            startActivity(settingIntent);
            finish();
        });

        btnNotification.setOnClickListener(v -> {
            Intent settingIntent = new Intent(this, NotificationActivity.class);
            startActivity(settingIntent);
            finish();
        });

        btnArchive.setOnClickListener(v -> {
            Intent archiveIntent = new Intent(this, BookcaseActivity.class);
            startActivity(archiveIntent);
            finish();
        });

        // Sự kiện
        tv_edit_profile.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        btn_account.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        });

        btn_setting.setOnClickListener(v -> {
            Intent intent = new Intent(this, GraphicSettingActivity.class);
            startActivity(intent);
        });

        btn_admin.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        });

        btn_upload_story.setOnClickListener(v -> {
            Intent intent = new Intent(this, UploadComicActivity.class);
            startActivity(intent);
        });

        btn_logout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_dialog_logout);
            builder.setMessage(R.string.message_logout);

            builder.setPositiveButton(R.string.string_no, (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setNeutralButton(R.string.string_yes, (dialog, which) -> {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button neutralButton = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);

            // Dat mau chu va do bong cho cac nut
            positiveButton.setTextColor(Color.parseColor("#50CAFF"));
            positiveButton.setTextSize(16.0f);
            positiveButton.setShadowLayer(7.0f, 0.0f, 8.0f, Color.parseColor("#80808080"));

            neutralButton.setTextColor(Color.parseColor("#FFA5BB"));
            neutralButton.setTextSize(16.0f);
            neutralButton.setShadowLayer(7.0f, 0.0f, 8.0f, Color.parseColor("#80808080"));
        });

    }

}