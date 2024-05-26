package com.example.a4tcomic.personal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.a4tcomic.R;

public class AccountActivity extends AppCompatActivity {

    private TextView changePassword, deleteAccount;
    private ImageButton btn_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        changePassword = findViewById(R.id.btn_change_account);
        deleteAccount = findViewById(R.id.btn_delete_account);
        btn_return = findViewById(R.id.btn_return);

        btn_return.setOnClickListener(v -> finish());

        changePassword.setOnClickListener(v -> {

        });

        deleteAccount.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_dialog_warning);
            builder.setMessage(R.string.mes_delete_account);

            builder.setPositiveButton(R.string.string_no, (dialog, which) -> {
                dialog.dismiss();
            });
            builder.setNeutralButton(R.string.delete_account, (dialog, which) -> {
                this.finish();
            });
            Dialog dialog = builder.create();
            dialog.show();
        });

    }
}