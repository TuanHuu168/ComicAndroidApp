package com.example.a4tcomic.activities.personal;

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
import com.example.a4tcomic.activities.account.ForgotPasswordActivity;
import com.example.a4tcomic.activities.account.LoginActivity;
import com.example.a4tcomic.db.UsersDB;
import com.example.a4tcomic.models.User;

public class AccountActivity extends AppCompatActivity {

    private TextView changePassword, tv_email_user, deleteAccount;
    private ImageButton btn_return;
    private User curUser;
    private String id_user;
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

        tv_email_user = findViewById(R.id.tv_email_user);
        changePassword = findViewById(R.id.btn_change_account);
        deleteAccount = findViewById(R.id.btn_delete_account);
        btn_return = findViewById(R.id.btn_return);

        curUser = (User) getIntent().getSerializableExtra("currentUser");
        id_user = curUser.getId();

        tv_email_user.setText(curUser.getEmail());

        btn_return.setOnClickListener(v -> finish());

        changePassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            intent.putExtra("curUser",curUser);
            startActivity(intent);
        });

        deleteAccount.setOnClickListener(v -> {
            setDialog();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UsersDB usersDB = new UsersDB();
        usersDB.getUserById(id_user, user -> {
            curUser = user;
        });

    }

    private void delAcc(){
        UsersDB usersDB = new UsersDB();
        usersDB.deleteUserByUser(curUser.getId());
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_dialog_warning);
        builder.setMessage(R.string.mes_delete_account);

        builder.setPositiveButton(R.string.string_no, (dialog, which) -> {
            dialog.dismiss();
        });
        builder.setNeutralButton(R.string.delete_account, (dialog, which) -> {
            delAcc();
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
    }
}