package com.example.a4tcomic.activities.personal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;

public class PersonalActivity extends AppCompatActivity {

    private TextView tv_edit_profile;
    private Button btn_account, btn_setting, btn_admin, btn_logout;
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
            // Xử lý sự kiện khi Button được nhấp vào
        });

        btn_admin.setOnClickListener(v -> {
            // Xử lý sự kiện khi Button được nhấp vào
        });

        btn_logout.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_dialog_logout);
            builder.setMessage(R.string.message_logout);

            builder.setPositiveButton(R.string.string_no, (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setNeutralButton(R.string.string_yes, (dialog, which) -> {
                this.finish();
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